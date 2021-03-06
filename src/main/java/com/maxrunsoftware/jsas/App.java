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

import static com.google.common.base.Preconditions.*;

import javax.inject.Inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.maxrunsoftware.jsas.impl.HttpClientApache;
import com.maxrunsoftware.jsas.impl.ResourceServiceDisk;
import com.maxrunsoftware.jsas.impl.SettingServiceEnvironment;
import com.maxrunsoftware.jsas.impl.VfsDisk;
import com.maxrunsoftware.jsas.impl.WebServerJetty;

public class App {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(App.class);
	private static final SettingService DEFAULTS = new SettingService() {};
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
		LoggingSetup.initialize(Util.getEnvironmentVariable(Constant.ENV_JSAS_LOGGING, DEFAULTS.getLogging()));
		LOG.info("(J)ava (S)imple (A)uthenticator (S)ervice  v" + Version.VALUE + "  dev@maxrunsoftware.com");

		var module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(SettingService.class).to(SettingServiceEnvironment.class);
				bind(ResourceService.class).to(ResourceServiceDisk.class);
				bind(WebServer.class).to(WebServerJetty.class);
				bind(HttpClient.class).to(HttpClientApache.class);
				bind(Vfs.class).to(VfsDisk.class);
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
			var joinThread = Util.getEnvironmentVariable(Constant.ENV_JSAS_JOINTHREAD, DEFAULTS.getJoinThread());
			webServer.start(joinThread);
			if (!joinThread) {
				while (true) {
					System.out.println("Type 'q', 'quit', 'exit' to exit");
					var input = Util.trimOrNull(Util.readLine());
					if (Util.equalsAnyIgnoreCase(input, "q", "quit", "exit")) break;
				}
			}
			webServer.stop();
		} catch (Exception e) {
			LOG.error("Error in web server", e);
		}
	}

	private void handleClient(String[] args) {
		if (args.length != 3) {
			handleClientError();
			return;
		}

		var host = Util.trimOrNull(args[0]);
		LOG.debug("Host: " + host);
		var pass = Util.trimOrNull(args[1]);
		LOG.debug("Password: " + pass);
		var name = Util.trimOrNull(args[2]);
		LOG.debug("ResourceName: " + name);

		if (host == null || pass == null || name == null) {
			handleClientError();
			return;
		}

		var resource = httpClient.get(host, name, pass);
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

	private void handleClientError() {
		LOG.error("Invalid argument format");
		LOG.error("java -jar jsas.jar <url> <password> <resourceName>");
		LOG.error("java -jar jsas.jar https://192.168.0.10 MyPass somefile");
	}

}
