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

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 7575256513512153388L;
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Servlet.class);

	private ResourceService resources;

	@Override
	public void init() throws ServletException {
		this.resources = (ResourceService) getServletContext().getAttribute(ResourceService.class.getName());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		var authUserPass = Util.httpAuthorizationDecode(request.getHeader("AUTHORIZATION"));
		if (authUserPass == null) {
			LOG.info("No credentials provided");
			unauthorized(response);
			return;
		}

		var resourceName = authUserPass.username;
		var password = authUserPass.password;

		var resource = resources.getResource(password, resourceName);
		if (resource == null) {
			LOG.info("Invalid resource: " + password + "   " + resourceName);
			unauthorized(response);
			return;
		}

		LOG.debug("Retrieved resource " + resource);

		authorized(response, resource);
	}

	private void unauthorized(HttpServletResponse response) throws IOException {
		LOG.info("Unauthorized");
		response.setContentType(Constant.CONTENTTYPE_TEXT);
		response.setCharacterEncoding(Constant.ENCODING_UTF8);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().println("UNAUTHORIZED");
	}

	private void authorized(HttpServletResponse response, Resource resource) throws IOException {
		checkNotNull(resource);

		if (resource.isText()) {
			response.setContentType(Constant.CONTENTTYPE_TEXT);
			response.setCharacterEncoding(Constant.ENCODING_UTF8);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().print(resource.getText());
		} else {
			var bytes = resource.getData();
			response.setContentType(Constant.CONTENTTYPE_BINARY);
			response.setHeader("Content-Disposition", "filename=\"" + resource.getFilename() + "\"");
			response.setContentLength(bytes.length);
			response.setStatus(HttpServletResponse.SC_OK);

			OutputStream os = response.getOutputStream();
			try {
				os.write(bytes, 0, bytes.length);
			} finally {
				os.close();
			}
		}

	}

}
