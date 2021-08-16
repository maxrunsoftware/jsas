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
package com.maxrunsoftware.jsas;

import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

public interface SettingService {
	public int getPort();

	public String getDirectory();

	public int getMaxThreads();

	public int getMinThreads();

	public int getIdleTimeout();

	default Map<String, Object> toMap() {
		var map = new CaseInsensitiveMap<String, Object>();
		map.put("Port", getPort());
		map.put("Directory", getDirectory());
		map.put("MaxThreads", getMaxThreads());
		map.put("MinThreads", getMinThreads());
		map.put("IdleTimeout", getIdleTimeout());
		return map;
	}
}
