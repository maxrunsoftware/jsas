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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.maxrunsoftware.jsas.ResourceService;
import com.maxrunsoftware.jsas.Servlet;
import com.maxrunsoftware.jsas.SettingService;
import com.maxrunsoftware.jsas.WebServer;

public class WebServerJetty implements WebServer {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WebServerJetty.class);

	private Server server;

	private final SettingService settings;
	private final ResourceService resources;

	@Inject
	public WebServerJetty(SettingService settings, ResourceService resources) {
		this.settings = checkNotNull(settings);
		this.resources = checkNotNull(resources);
	}

	@Override
	public void start() throws Exception {

		var maxThreads = settings.getMaxThreads();
		LOG.debug("maxThreads: " + maxThreads);

		var minThreads = settings.getMinThreads();
		LOG.debug("minThreads: " + minThreads);

		var idleTimeout = settings.getIdleTimeout();
		LOG.debug("idleTimeout: " + idleTimeout);

		var port = settings.getPort();
		LOG.debug("port: " + port);

		QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

		server = new Server(threadPool);

		HttpConfiguration httpConfig = new HttpConfiguration();
		httpConfig.setSendServerVersion(false);
		HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);
		ServerConnector connector = new ServerConnector(server, httpFactory);

		connector.setPort(port);

		server.setConnectors(new Connector[] { connector });

		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");

		// Option 2: Using ServletContext attribute
		context.setAttribute(ResourceService.class.getName(), resources);
		context.addServlet(Servlet.class, "/*");

		server.setHandler(context);

		// ServletHandler servletHandler = new ServletHandler();
		// server.setHandler(servletHandler);
		// servletHandler.addServletWithMapping(Servlet.class, "/*");

		LOG.info("Starting server on port " + port);
		server.start();
		server.join();

	}

	@Override
	public void stop() throws Exception {
		LOG.info("Stopping server");
		server.stop();

	}
}
