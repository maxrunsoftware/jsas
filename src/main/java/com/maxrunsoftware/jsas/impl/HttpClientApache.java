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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import com.maxrunsoftware.jsas.HttpClient;
import com.maxrunsoftware.jsas.Resource;
import com.maxrunsoftware.jsas.Util;

public class HttpClientApache implements HttpClient {

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(HttpClientApache.class);

	@Override
	public Resource Get(String host, String username, String password) {
		checkNotNull(host);
		HttpGet httpGet = new HttpGet(host);
		if (username != null) { httpGet.addHeader("Authorization", Util.httpAuthorizationEncode(username, password)); }
		try {
			// try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			try (CloseableHttpClient httpclient = createClient()) {
				try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
					return new ResourceHttpResponseApache(response);
				}

			}
		} catch (Exception e) {
			LOG.error("Error retrieving URL " + host, e);
			return null;
		}

	}

	private CloseableHttpClient createClient() throws Exception {

		SSLContextBuilder sshbuilder = new SSLContextBuilder();

		// sshbuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		sshbuilder.loadTrustMaterial(null, new TrustStrategy() {

			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}

		});

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sshbuilder.build(), new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		});

		var pcm = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslsf).build();

		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(null).setConnectionManager(pcm)

				.setDefaultCookieStore(null).build();

		return httpclient;
	}

}
