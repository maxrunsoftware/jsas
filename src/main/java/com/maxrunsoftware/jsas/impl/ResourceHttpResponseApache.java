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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.maxrunsoftware.jsas.Constant;
import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.Util;

public class ResourceHttpResponseApache implements Resource {

	private static class HeadersParser {
		public final Map<String, List<String>> headers;

		public HeadersParser(CloseableHttpResponse response) {
			var map = new CaseInsensitiveMap<String, List<String>>();
			for (var header : response.getHeaders()) {
				var name = Util.trimOrNull(header.getName());
				var val = Util.trimOrNull(header.getValue());

				if (name != null && val != null) {
					if (!map.containsKey(name)) {
						map.put(name, new ArrayList<String>());
					}
					map.get(name).add(val);
				}
			}
			headers = map;
		}

		public String getFirst(String name) {
			var list = headers.get(name);
			if (list == null) {
				return null;
			}
			if (list.size() == 0) {
				return null;
			}
			return list.get(0);
		}

	}

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ResourceHttpResponseApache.class);

	private final String name;
	private final String description;
	private final String contentType;
	private final boolean isText;
	private final String text;
	private final byte[] data;
	private final String filename;

	public ResourceHttpResponseApache(CloseableHttpResponse response) throws IOException {
		name = "" + response.getCode();
		description = response.getReasonPhrase();

		var headers = new HeadersParser(response);

		// Debug headers
		for (var headerKey : headers.headers.keySet()) {
			var headerVals = headers.headers.get(headerKey);
			for (int i = 0; i < headerVals.size(); i++) {
				if (headerVals.size() == 1) {
					LOG.debug("[HEADER] " + headerKey + ": " + headerVals.get(i));
				} else {
					LOG.debug("[HEADER] " + headerKey + "[" + i + "]: " + headerVals.get(i));
				}
			}
		}

		// get contentType
		contentType = Util.coalesce(headers.getFirst("Content-Type"), "application/octet-stream").toLowerCase();

		// get filename;
		String fn = null;
		var cd = headers.getFirst("content-disposition");
		if (cd != null) {
			var parts = Util.split(cd, "filename=");
			if (parts.length == 2) {
				var part = parts[1];
				part = part.replace("\"", "");
				part = part.replace("'", "");
				part = Util.trimOrNull(part);
				fn = part;
			}
		}
		filename = fn;

		// get isText
		var istext = false;
		for (var mimeText : Constant.MIME_TEXT) {
			if (contentType.contains(mimeText)) {
				istext = true;
				break;
			}
		}
		isText = istext;

		HttpEntity httpEntity = response.getEntity();
		if (isText) {
			try {
				text = EntityUtils.toString(httpEntity);
				data = null;
			} catch (ParseException e) {
				throw new IOException("Error parsing text stream from HttpEntity", e);
			}
		} else {
			text = null;
			data = EntityUtils.toByteArray(httpEntity);

		}

	}

	@Override
	public String getDescription() {
		return description;
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
	public String getText() {
		return text;
	}

	@Override
	public boolean isText() {
		return isText;
	}

	@Override
	public String getFilename() {
		return filename;
	}

}
