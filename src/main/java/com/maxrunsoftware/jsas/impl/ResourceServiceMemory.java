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

import java.util.HashMap;
import java.util.Map;

import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.Util;

public class ResourceServiceMemory implements ResourceService {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceServiceMemory.class);

	public class ResourceKey {
		private final String password;
		private final String resourceName;

		public ResourceKey(String password, String resourceName) {
			this.password = checkNotNull(password);
			this.resourceName = checkNotNull(resourceName);
		}

		public String getPassword() {
			return password;
		}

		public String getResourceName() {
			return resourceName;
		}

		public boolean equals(ResourceKey o) {
			if (o == this)
				return true;
			if (o == null)
				return false;
			if (!password.equals(o.password))
				return false;
			if (!resourceName.toLowerCase().equals(o.resourceName.toLowerCase()))
				return false;
			return true;
		}

		@Override
		public boolean equals(Object o) {
			return equals(Util.as(o, ResourceKey.class));
		}

		@Override
		public int hashCode() {
			int result = password.hashCode();
			result = 31 * result + resourceName.toLowerCase().hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "[" + password + ", " + resourceName + "]";
		}
	}

	private final Map<ResourceKey, Resource> map = new HashMap<ResourceKey, Resource>();

	public void addResource(String password, String resourceName, Resource resource) {
		LOG.debug("Adding resource " + resourceName);
		map.put(new ResourceKey(password, resourceName), resource);
	}

	@Override
	public Resource getResource(String password, String resourceName) {
		LOG.debug("Getting resource " + resourceName);
		return map.get(new ResourceKey(password, resourceName));
	}
}
