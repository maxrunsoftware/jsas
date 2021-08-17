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

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.maxrunsoftware.jsas.Util;
import com.maxrunsoftware.jsas.Vfs;
import com.maxrunsoftware.jsas.VfsFile;

public class VfsDisk implements Vfs {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(VfsDisk.class);

	private static class VfsFileDisk implements VfsFile {
		private final File file;
		private String path;

		public VfsFileDisk(File file) {
			this.file = checkNotNull(file);
		}

		@Override
		public String getName() {
			return file.getName();
		}

		@Override
		public byte[] getData() {
			try {
				LOG.debug("Reading file " + file.getAbsolutePath());
				return Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				LOG.warn("Error reading file " + file.getAbsolutePath(), e);
				return null;
			}
		}

		@Override
		public String getPath() {
			if (path == null) {
				try {
					path = file.getCanonicalPath();
				} catch (IOException e) {
					LOG.trace("Error in getCanonicalPath() for file " + file.getAbsolutePath());
				}
				if (path == null) { path = file.getAbsolutePath(); }
				if (path == null) { path = file.getName(); }
			}
			return path;
		}

	}

	@Override
	public VfsFile[] getFiles(String directory) {
		checkNotNull(directory);
		LOG.debug("Scanning directory " + directory);
		File dir = new File(directory);
		var list = new ArrayList<VfsFile>();
		if (!dir.exists()) {
			LOG.warn("Directory does not exist " + dir.getAbsolutePath());
			return list.toArray(VfsFile[]::new);
		}

		var files = Util.coalesce(dir.listFiles(), new File[0]);
		LOG.debug("Found " + files.length + " items in directory " + dir.getAbsolutePath());
		for (var file : files) {
			if (file.isDirectory()) {
				LOG.trace("Ignoring directory " + file.getAbsolutePath());
				continue;
			}
			if (!file.exists()) {
				LOG.trace("Ignoring nonexistant file " + file.getAbsolutePath());
				continue;
			}
			var vfsFile = new VfsFileDisk(file);
			list.add(vfsFile);
		}

		return list.toArray(VfsFile[]::new);
	}
}
