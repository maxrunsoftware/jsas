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

import javax.inject.Inject;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.Vfs;

public class ResourceServiceDisk implements ResourceService {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceServiceDisk.class);
	private final Object locker = new Object();
	private ResourceServiceDiskDirectory dirCache;

	private final SettingService settings;
	private final Vfs vfs;

	@Inject
	public ResourceServiceDisk(SettingService settings, Vfs vfs) {
		this.settings = checkNotNull(settings);
		this.vfs = checkNotNull(vfs);
	}

	@Override
	public Resource getResource(String password, String resourceName) {
		checkNotNull(password);
		checkNotNull(resourceName);
		LOG.debug("Getting resource " + resourceName);

		synchronized (locker) {
			if (dirCache == null || dirCache.isExpired()) {
				dirCache = new ResourceServiceDiskDirectory(settings.getDirectory(), settings.getDirectoryCacheTime(),
						vfs);
			}

			return dirCache.getResource(password, resourceName);
		}

	}
}
