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

import java.util.List;

public class Constant {
	private Constant() {}

	public static final List<String> TXT_EXTS = List.of("txt", "html");

	public static final List<String> MIME_TEXT = List.of(
	//@formatter:off
			"text/plain", 
			"text/css", 
			"text/csv", 
			"text/html",
			"text/calendar", 
			"text/javascript", 
			"application/xhtml+xml", 
			"application/xml", 
			"text/xml" 
	//@formatter:on
	);

	public static final String CONTENTTYPE_TEXT = "text/plain";

	public static final String CONTENTTYPE_BINARY = "application/octet-stream";

	public static final String ENCODING_UTF8 = "UTF-8";

	public static final String ENV_JSAS_LOGGING = "JSAS_LOGGING";
	public static final String ENV_JSAS_DIRCACHETIME = "JSAS_DIRCACHETIME";
	public static final String ENV_JSAS_DIR = "JSAS_DIR";
	public static final String ENV_JSAS_PORT = "JSAS_PORT";
	public static final String ENV_JSAS_MAXTHREADS = "JSAS_MAXTHREADS";
	public static final String ENV_JSAS_MINTHREADS = "JSAS_MINTHREADS";
	public static final String ENV_JSAS_IDLETIMEOUT = "JSAS_IDLETIMEOUT";

}
