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

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.Util;
import com.maxrunsoftware.jsas.VfsFile;

public class ResourceServiceDiskDirectoryFile {
	private static final List<String> TXT_EXTS = List.of("txt", "html");
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
			.getLogger(ResourceServiceDiskDirectoryFile.class);

	private final String password;
	private final String resourceName;
	private final Resource resource;

	public ResourceServiceDiskDirectoryFile(VfsFile file) {

		var name = FilenameUtils.getBaseName(file.getName());
		LOG.trace(file.getPath() + " [NAME]: " + name);

		var extension = Util.coalesce(FilenameUtils.getExtension(file.getName()), "");
		LOG.trace(file.getPath() + " [EXTENSION]: " + extension);

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
			LOG.trace("Ignoring invalid file format " + file.getPath());
			this.password = null;
			this.resourceName = null;
			this.resource = null;
			return;
		}
		LOG.trace(file.getPath() + " [ResourceDescription]: " + rDescription);
		LOG.trace(file.getPath() + " [ResourcePassword]: " + rPassword);
		LOG.trace(file.getPath() + " [ResourceName]: " + rName);

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

	public String getPassword() {
		return password;
	}

	public String getResourceName() {
		return resourceName;
	}

	public Resource getResource() {
		return resource;
	}

}
