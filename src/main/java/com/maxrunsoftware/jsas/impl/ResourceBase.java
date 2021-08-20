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

import com.maxrunsoftware.jsas.Resource;

public abstract class ResourceBase implements Resource {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceFile.class);

	private final String name;
	private final boolean text;
	private String description;
	private final String filename;

	protected ResourceBase(String name, String filename, boolean isText) {
		this.name = checkNotNull(name);
		this.text = isText;
		this.filename = checkNotNull(filename);
		LOG.debug(getClass().getSimpleName() + "(" + name + ", " + filename + ", " + isText + ")");
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isText() {
		return text;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	public static String toString(Resource resource) {
		var sb = new StringBuilder();

		sb.append(resource.getClass().getName() + " [");
		sb.append("Name: " + resource.getName());
		sb.append(", Description: " + resource.getDescription());
		sb.append(", Filename: " + resource.getFilename());
		sb.append(", IsText: " + resource.isText());
		var d = resource.getData();
		if (d == null) {
			sb.append(", Data: null");
		} else {
			sb.append(", Data: byte[" + d.length + "]");
		}
		sb.append(", Text: " + resource.getText());
		sb.append("]");

		return sb.toString();
	}

	@Override
	public String toString() {
		return toString(this);
	}
}
