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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;

public final class Util {
	public static final String httpAuthorizationEncode(String username, String password) {
		final var up = username + ":" + password;
		final var encodedBytes = up.getBytes(StandardCharsets.UTF_8);
		final var str = Base64.getEncoder().encodeToString(encodedBytes);
		final var strFull = "Basic " + str;
		return strFull;
	}

	public static final ImmutablePair<String, String> httpAuthorizationDecode(String authorization) {
		if (authorization == null)
			return null;
		if (!authorization.toLowerCase().startsWith("basic"))
			return null;

		final var base64Credentials = authorization.substring("Basic".length()).trim();
		final var credDecoded = Base64.getDecoder().decode(base64Credentials);
		final var credentials = new String(credDecoded, StandardCharsets.UTF_8);
		final var values = credentials.split(":", 2);
		return new ImmutablePair<String, String>(values[0], values[1]);
	}

	public static final byte[] copy(byte[] bytes) {
		if (bytes == null)
			return null;
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
		if (s == null)
			return null;
		s = s.trim();
		if (s.length() == 0)
			return null;
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
			if (val != null)
				return val;
		}
		return null;
	}

}
