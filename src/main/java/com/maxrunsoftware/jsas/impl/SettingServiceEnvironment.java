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

import com.maxrunsoftware.jsas.Constant;
import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.Util;

public class SettingServiceEnvironment implements SettingService {

	@Override
	public int getDirectoryCacheTime() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_DIRCACHETIME, SettingService.super.getDirectoryCacheTime());
	}

	@Override
	public String getDirectory() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_DIR, SettingService.super.getDirectory());
	}

	@Override
	public int getPort() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_PORT, SettingService.super.getPort());
	}

	@Override
	public int getMaxThreads() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_MAXTHREADS, SettingService.super.getMaxThreads());
	}

	@Override
	public int getMinThreads() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_MINTHREADS, SettingService.super.getMinThreads());
	}

	@Override
	public int getIdleTimeout() {
		return Util.getEnvironmentVariable(Constant.ENV_JSAS_IDLETIMEOUT, SettingService.super.getIdleTimeout());
	}

}
