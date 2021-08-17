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

import com.maxrunsoftware.jsas.Util;

public class ResourceData extends ResourceBase {

	private final byte[] data;

	public ResourceData(String name, byte[] data, String filename) {
		super(name, filename, false);
		this.data = Util.copy(checkNotNull(data));
	}

	@Override
	public byte[] getData() {
		return Util.copy(data);
	}

	@Override
	public String getText() {
		return null;
	}
}
