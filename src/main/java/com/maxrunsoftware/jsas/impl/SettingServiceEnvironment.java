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

import java.nio.file.Paths;

import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.Util;

public class SettingServiceEnvironment implements SettingService {
	private String getEnvVar(String name, String defaultValue) {
		var val = System.getenv(name);
		val = Util.trimOrNull(val);
		if (val != null)
			return val;
		return defaultValue;
	}

	private int getEnvVar(String name, int defaultValue) {
		var val = getEnvVar(name, null);
		if (val == null)
			return defaultValue;
		return Integer.parseInt(val);
	}

	@Override
	public String getLogging() {
		return getEnvVar("JSAS_LOGGING", "info");
	}

	@Override
	public int getDirectoryCacheTime() {
		return getEnvVar("JSAS_DIRCACHETIME", 5000);
	}

	@Override
	public String getDirectory() {
		return getEnvVar("JSAS_DIR", Paths.get(".").toAbsolutePath().normalize().toString());
		// return "/Users/user/Temp";
	}

	@Override
	public int getPort() {
		return getEnvVar("JSAS_PORT", 8080);
	}

	@Override
	public int getMaxThreads() {
		return getEnvVar("JSAS_MAXTHREADS", 100);
	}

	@Override
	public int getMinThreads() {
		return getEnvVar("JSAS_MINTHREADS", 10);
	}

	@Override
	public int getIdleTimeout() {
		return getEnvVar("JSAS_IDLETIMEOUT", 120);
	}

}
