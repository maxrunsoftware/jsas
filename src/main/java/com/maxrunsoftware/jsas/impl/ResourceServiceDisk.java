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

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.Util;

public class ResourceServiceDisk implements ResourceService {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceServiceDisk.class);

	private static final String[] TXT_EXTS = new String[] { "txt", "html" };
	private final SettingService settings;

	@Inject
	public ResourceServiceDisk(SettingService settings) {
		this.settings = checkNotNull(settings);
	}

	@Override
	public Resource getResource(String password, String resourceName) {
		checkNotNull(password);
		checkNotNull(resourceName);
		LOG.debug("Getting resource " + resourceName);

		var directory = settings.getDirectory();
		LOG.debug("Scanning directory " + directory);
		File f = new File(directory);

		if (!f.exists()) {
			LOG.warn("Directory does not exist " + f.getAbsolutePath());
		}

		var files = Util.coalesce(f.listFiles(), new File[0]);
		LOG.debug("Found " + files.length + " items in directory " + f.getAbsolutePath());

		// For each pathname in the pathnames array
		for (var file : files) {
			if (!file.isFile()) {
				LOG.trace("Ignoring directory " + file.getAbsolutePath());
				continue;
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
				// TODO Log invalid name
				continue;
			}
			LOG.trace(file.getAbsolutePath() + " [ResourceDescription]: " + rDescription);
			LOG.trace(file.getAbsolutePath() + " [ResourcePassword]: " + rPassword);
			LOG.trace(file.getAbsolutePath() + " [ResourceName]: " + rName);

			if (!password.equals(rPassword))
				continue;
			if (!resourceName.toLowerCase().equals(rName.toLowerCase()))
				continue;

			var isText = false;
			for (var txt : TXT_EXTS) {
				if (extension.toLowerCase().equals(txt))
					isText = true;
			}

			var resource = new ResourceFile(file, rName + "." + extension, rName, isText);
			resource.setDescription(rDescription);
			return resource;
		}

		return null;

	}
}
