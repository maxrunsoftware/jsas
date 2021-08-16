/*
 * Copyright (c) 2021 Max Run Software (dev@maxrunsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maxrunsoftware.jsas.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.Util;

public class ResourceServiceDisk implements ResourceService {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceServiceDisk.class);
	private final Object locker = new Object();
	private DirCache dirCache;
	private static final String[] TXT_EXTS = new String[] { "txt", "html" };
	private final SettingService settings;

	private static class DirCache {
		private final LocalDateTime timestamp;
		private final List<DirFile> dirFiles = new ArrayList<DirFile>();

		private final long millisThreshold;

		public boolean isExpired() {
			var age = timestamp.until(LocalDateTime.now(), ChronoUnit.MILLIS);
			return age > millisThreshold;
		}

		private static class DirFile {
			public String password;
			public String resourceName;
			public Resource resource;

			public DirFile(File file) {
				if (file.isDirectory()) {
					LOG.trace("Ignoring directory " + file.getAbsolutePath());
					return;
				}
				if (!file.exists()) {
					LOG.trace("Ignoring nonexistant file " + file.getAbsolutePath());
					return;
				}

				var name = FilenameUtils.getBaseName(file.getName());
				LOG.trace(file.getAbsolutePath() + " [NAME]: " + name);

				var extension = FilenameUtils.getExtension(file.getName());
				if (extension == null)
					extension = "";
				LOG.trace(file.getAbsolutePath() + " [EXTENSION]: " + extension);

				var nameParts = name.split("_");

				String rDescription = null;
				String rPassword = null;
				String rName = null;
				if (nameParts.length == 2) {
					rPassword = nameParts[0];
					rName = nameParts[1];
				} else if (nameParts.length == 3) {
					rDescription = nameParts[0];
					rPassword = nameParts[1];
					rName = nameParts[2];
				} else {
					LOG.trace("Ignoring invalid file format " + file.getAbsolutePath());
					return;
				}
				LOG.trace(file.getAbsolutePath() + " [ResourceDescription]: " + rDescription);
				LOG.trace(file.getAbsolutePath() + " [ResourcePassword]: " + rPassword);
				LOG.trace(file.getAbsolutePath() + " [ResourceName]: " + rName);

				var isText = false;
				for (var txt : TXT_EXTS) {
					if (extension.toLowerCase().equals(txt)) {
						isText = true;
					}
				}

				var resource = new ResourceFile(file, rName + "." + extension, rName, isText);
				resource.setDescription(rDescription);

				this.password = rPassword;
				this.resourceName = rName.toLowerCase();
				this.resource = resource;
			}

		}

		public DirCache(String directory, long millisThreshold) {
			this.millisThreshold = millisThreshold;
			LOG.debug("Scanning directory " + directory);
			File dir = new File(directory);
			if (!dir.exists()) {
				LOG.warn("Directory does not exist " + dir.getAbsolutePath());
			} else {
				var files = Util.coalesce(dir.listFiles(), new File[0]);
				LOG.debug("Found " + files.length + " items in directory " + dir.getAbsolutePath());
				for (var file : files) {
					var dirFile = new DirFile(file);
					if (dirFile.resource != null) {
						dirFiles.add(dirFile);
					}
				}
			}

			this.timestamp = LocalDateTime.now();
		}

		public Resource getResource(String password, String resourceName) {
			var p = checkNotNull(password);
			var n = checkNotNull(resourceName).toLowerCase();

			for (var dirFile : dirFiles) {
				if (p.equals(dirFile.password) && n.equals(dirFile.resourceName)) {
					return dirFile.resource;
				}
			}

			return null;
		}
	}

	@Inject
	public ResourceServiceDisk(SettingService settings) {
		this.settings = checkNotNull(settings);
	}

	@Override
	public Resource getResource(String password, String resourceName) {
		checkNotNull(password);
		checkNotNull(resourceName);
		LOG.debug("Getting resource " + resourceName);

		synchronized (locker) {
			if (dirCache == null || dirCache.isExpired()) {
				dirCache = new DirCache(settings.getDirectory(), settings.getDirectoryCacheTime());
			}

			return dirCache.getResource(password, resourceName);
		}

	}
}
