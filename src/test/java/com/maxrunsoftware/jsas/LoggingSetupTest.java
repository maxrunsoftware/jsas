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

import org.junit.Test;

public class LoggingSetupTest extends TestBase {

	@Test
	public void testInitialize() {
		LoggingSetup.initialize(LoggingSetup.LEVEL_TRACE);
		LoggingSetup.initialize(LoggingSetup.LEVEL_DEBUG);
		LoggingSetup.initialize(LoggingSetup.LEVEL_INFO);
		LoggingSetup.initialize(LoggingSetup.LEVEL_WARN);
		LoggingSetup.initialize(LoggingSetup.LEVEL_ERROR);
		LoggingSetup.initialize(99);

		LoggingSetup.initialize("trace");
		LoggingSetup.initialize("debug");
		LoggingSetup.initialize("info");
		LoggingSetup.initialize("warn");
		LoggingSetup.initialize("ErrOr");
		LoggingSetup.initialize("somethingelse");
		LoggingSetup.initialize(null);

	}

}
