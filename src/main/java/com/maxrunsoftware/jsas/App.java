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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maxrunsoftware.jsas.impl.HttpClientApache;
import com.maxrunsoftware.jsas.impl.ResourceServiceDisk;
import com.maxrunsoftware.jsas.impl.SettingServiceEnvironment;
import com.maxrunsoftware.jsas.impl.WebServerJetty;

public class App {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(App.class);

	private final HttpClient httpClient;
	private final WebServer webServer;

	@Inject
	public App(HttpClient httpClient, WebServer webServer, SettingService settings) {
		this.httpClient = checkNotNull(httpClient);
		this.webServer = checkNotNull(webServer);

		var map = settings.toMap();
		for (var key : map.keySet()) {
			LOG.debug(key + ": " + map.get(key));
		}
	}

	public static void main(String[] args) {
		LoggingSetup.initialize(System.getenv("JSAS_LOGGING"));
		LOG.info("(J)ava (S)imple (A)uthenticator (S)ervice v" + Version.VALUE + "  dev@maxrunsoftware.com");

		var module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(SettingService.class).to(SettingServiceEnvironment.class);
				bind(ResourceService.class).to(ResourceServiceDisk.class);
				bind(WebServer.class).to(WebServerJetty.class);
				bind(HttpClient.class).to(HttpClientApache.class);
			}
		};
		Injector injector = Guice.createInjector(module);

		var app = injector.getInstance(App.class);
		app.run(args);
	}

	private void run(String[] args) {
		if (args != null && args.length > 0) {
			handleClient(args);
		} else {
			handleServer();
		}

	}

	private void handleServer() {
		try {
			webServer.start();

//			webServer.stop();
		} catch (Exception e) {
			LOG.error("Error in web server", e);
		}
	}

	private void handleClient(String[] args) {
		if (args.length != 3) {
			throw new Error("Invalid arguments");
		}

		var host = args[0];
		var pass = args[1];
		var name = args[2];

		var resource = httpClient.Get(host, name, pass);
		if (resource == null) {
			LOG.warn("Error retrieving response");
		} else {
			if (resource.isText()) {
				LOG.info("Received Text: \n" + resource.getText());
			} else {
				LOG.info("Received Data: data[" + resource.getData().length + "]");
			}
		}

	}

}
