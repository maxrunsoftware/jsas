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

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public final class LoggingSetup {

	private LoggingSetup() {}

	public static final int LEVEL_TRACE = 1;
	public static final int LEVEL_DEBUG = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_WARN = 4;
	public static final int LEVEL_ERROR = 5;

	private static Level parseLevel(int level) {
		switch (level) {
			case LEVEL_TRACE:
				return Level.TRACE;
			case LEVEL_DEBUG:
				return Level.DEBUG;
			case LEVEL_INFO:
				return Level.INFO;
			case LEVEL_WARN:
				return Level.WARN;
			case LEVEL_ERROR:
				return Level.ERROR;
			default:
				return Level.INFO;
		}
	}

	private static Level parseLevel(String level) {
		level = Util.trimOrNull(level);
		if (level == null) return Level.INFO;

		level = level.toLowerCase();
		if (level.equals("trace")) return Level.TRACE;
		if (level.equals("debug")) return Level.DEBUG;
		if (level.equals("info")) return Level.INFO;
		if (level.equals("warn")) return Level.WARN;
		if (level.equals("error")) return Level.ERROR;
		return Level.INFO;
	}

	public static void initialize(String level) {
		initialize(parseLevel(level));
	}

	public static void initialize(int level) {
		initialize(parseLevel(level));
	}

	private static void initialize(Level level) {
		// This is the root logger provided by log4j
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(level);

		// Define log pattern layout
		PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

		// Add console appender to root logger
		rootLogger.removeAllAppenders();
		rootLogger.addAppender(new ConsoleAppender(layout));

		var disables = new String[] { "org.apache.hc.client5.http", "org.apache.hc.client5.http.wire", "org.eclipse.jetty", };

		for (var disable : disables) {
			LogManager.getLogger(disable).setLevel(Level.INFO);
		}

	}

}
