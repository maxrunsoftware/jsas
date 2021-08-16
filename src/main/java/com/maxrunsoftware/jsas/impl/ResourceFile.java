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

import com.maxrunsoftware.jsas.Util;
import com.maxrunsoftware.jsas.VfsFile;

public class ResourceFile extends ResourceBase {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceFile.class);

	private final VfsFile file;
	private byte[] data;
	private boolean dataLoaded;

	public ResourceFile(VfsFile file, String filename, String name, boolean isText) {
		super(name, filename, isText);
		this.file = checkNotNull(file);
	}

	@Override
	public byte[] getData() {
		if (isText())
			return null;
		return readFile();
	}

	private byte[] readFile() {
		if (!dataLoaded) {
			dataLoaded = true;
			LOG.debug("Reading file " + file.getPath());
			data = file.getData();
		}
		return data;
	}

	@Override
	public String getText() {
		if (!isText())
			return null;
		var bytes = readFile();
		if (bytes == null)
			return null;
		return Util.asString(bytes);
	}
}
