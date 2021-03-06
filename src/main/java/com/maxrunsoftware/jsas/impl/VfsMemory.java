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

import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import com.maxrunsoftware.jsas.Vfs;
import com.maxrunsoftware.jsas.VfsFile;

public class VfsMemory implements Vfs {

	public static class VfsFileMemory implements VfsFile {

		private final String name;
		private final byte[] data;
		private final String path;

		public VfsFileMemory(String name, byte[] data, String path) {
			this.name = name;
			this.data = data;
			this.path = path;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public byte[] getData() {
			return data;
		}

		@Override
		public String getPath() {
			return path;
		}

	}

	private final Map<String, VfsFileMemory> files = new CaseInsensitiveMap<String, VfsFileMemory>();

	public Map<String, VfsFileMemory> getFiles() {
		return files;
	}

	@Override
	public VfsFile[] getFiles(String directory) {
		return getFiles().values().toArray(VfsFile[]::new);
	}

}
