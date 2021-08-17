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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.Vfs;

public class ResourceServiceDiskDirectory implements ResourceService {
	private final LocalDateTime timestamp;
	private final List<ResourceServiceDiskDirectoryFile> dirFiles = new ArrayList<ResourceServiceDiskDirectoryFile>();

	private final long millisThreshold;

	public boolean isExpired() {
		var age = timestamp.until(LocalDateTime.now(), ChronoUnit.MILLIS);
		return age > millisThreshold;
	}

	public ResourceServiceDiskDirectory(String directory, long millisThreshold, Vfs vfs) {
		this.millisThreshold = millisThreshold;
		var files = vfs.getFiles(directory);

		for (var file : files) {
			var dirFile = new ResourceServiceDiskDirectoryFile(file);
			if (dirFile.getResource() != null) { dirFiles.add(dirFile); }
		}

		this.timestamp = LocalDateTime.now();

	}

	@Override
	public Resource getResource(String password, String resourceName) {
		var p = checkNotNull(password);
		var n = checkNotNull(resourceName).toLowerCase();

		for (var dirFile : dirFiles) {
			if (p.equals(dirFile.getPassword()) && n.equals(dirFile.getResourceName())) return dirFile.getResource();
		}

		return null;
	}

}
