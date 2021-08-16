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
import java.io.IOException;
import java.nio.file.Files;

import com.maxrunsoftware.jsas.Util;

public class ResourceFile extends ResourceBase {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceFile.class);

	private final File file;
	private byte[] data;
	private boolean dataLoaded;

	public ResourceFile(File file, String filename, String name, boolean isText) {
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
			try {
				LOG.debug("Reading file " + file.getAbsolutePath());
				data = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				LOG.warn("Error reading file " + file.getAbsolutePath(), e);
			}
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
