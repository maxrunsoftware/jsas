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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public final class Util {
	public static final String httpAuthorizationEncode(String username, String password) {
		final var up = username + ":" + password;
		final var encodedBytes = up.getBytes(StandardCharsets.UTF_8);
		final var str = Base64.getEncoder().encodeToString(encodedBytes);
		final var strFull = "Basic " + str;
		return strFull;
	}

	public static class HttpAuthorizationCredential {
		public final String username;
		public final String password;

		private HttpAuthorizationCredential(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}

	public static final HttpAuthorizationCredential httpAuthorizationDecode(String authorization) {
		if (authorization == null) return null;
		if (!authorization.toLowerCase().startsWith("basic")) return null;

		final var base64Credentials = authorization.substring("Basic".length()).trim();
		final var credDecoded = Base64.getDecoder().decode(base64Credentials);
		final var credentials = new String(credDecoded, StandardCharsets.UTF_8);
		final var values = credentials.split(":", 2);
		return new HttpAuthorizationCredential(values[0], values[1]);
	}

	public static final byte[] copy(byte[] bytes) {
		if (bytes == null) return null;
		byte[] dest = new byte[bytes.length];
		System.arraycopy(bytes, 0, dest, 0, bytes.length);
		return dest;
	}

	public static final byte[] asBytes(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	public static final String asString(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static final String trimOrNull(String s) {
		if (s == null) return null;
		s = s.trim();
		if (s.length() == 0) return null;
		return s;
	}

	public static final <T> T as(Object o, Class<T> t) {
		// https://stackoverflow.com/a/1034300
		return t.isInstance(o) ? t.cast(o) : null;
	}

	public static final String[] split(String s, String separator) {
		// https://stackoverflow.com/a/6374137
		return s.split(Pattern.quote(separator));
	}

	@SafeVarargs
	public static final <T> T coalesce(T... values) {
		for (var val : values) {
			if (val != null) return val;
		}
		return null;
	}

	@SafeVarargs
	public static final <T> boolean equalsAny(T sourceObj, T... otherObjs) {
		//@formatter:off
		if (otherObjs == null || otherObjs.length == 0) return false;
		for (T otherObj : otherObjs) {
			if (sourceObj == null) {
				if (otherObj == null) return true;
			} else {
				if (otherObj != null) {
					if (sourceObj.equals(otherObj)) return true; 
				}
			}
		}
		//@formatter:on
		return false;
	}

	@SafeVarargs
	public static final boolean equalsAnyIgnoreCase(String sourceObj, String... otherObjs) {
		//@formatter:off
		if (otherObjs == null || otherObjs.length == 0) return false;
		for (String otherObj : otherObjs) {
			if (sourceObj == null) {
				if (otherObj == null) return true;
			} else {
				if (otherObj != null) {
					if (sourceObj.equalsIgnoreCase(otherObj)) return true; 
				}
			}
		}
		//@formatter:on
		return false;
	}

	public static final String readLine() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final String getEnvironmentVariable(String name) {
		return Util.trimOrNull(System.getenv(name));
	}

	public static final String getEnvironmentVariable(String name, String defaultValue) {
		return coalesce(getEnvironmentVariable(name), defaultValue);
	}

	public static final int getEnvironmentVariable(String name, int defaultValue) {
		var val = getEnvironmentVariable(name, null);
		if (val == null) return defaultValue;
		return Integer.parseInt(val);
	}

	public static final boolean getEnvironmentVariable(String name, boolean defaultValue) {
		var val = getEnvironmentVariable(name, null);
		if (val == null) return defaultValue;
		return parseBoolean(val);
	}

	public static final boolean parseBoolean(String val) {
		if (val == null) throw new IllegalArgumentException("Invalid boolean value: " + val);
		if (equalsAnyIgnoreCase(val, "true", "t", "yes", "y", "1")) return true;
		if (equalsAnyIgnoreCase(val, "false", "f", "no", "n", "0")) return false;
		throw new IllegalArgumentException("Invalid boolean value: " + val);
	}
}
