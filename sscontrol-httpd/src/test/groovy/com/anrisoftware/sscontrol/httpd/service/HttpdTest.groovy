/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.service.HttpdResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthRequireGroup
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthRequireValidUser

/**
 * @see HttpdServiceImpl
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdTest extends HttpdTestUtil {

	@Test
	void "httpd script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService httpdScript, profile
		HttpdServiceImpl service = registry.getService("httpd")[0]
		service.domains.size() == 5
		service.virtualDomains.size() == 4
	}

	@Test
	void "httpd auth file"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService authFileScript, profile
		HttpdServiceImpl service = registry.getService("httpd")[0]
		service.domains.size() == 2

		def auth = service.domains[1].auths[0]
		assert auth.locations.contains("private")
		assert auth.provider == AuthProvider.file
		assert auth.name == "private"

		def require = auth.requires[0]
		assert require.class == AuthRequireValidUser

		require = auth.requires[1]
		assert require.class == AuthRequireGroup
		assert require.name == "admin"

		def user = auth.users[0]
		assert user.name == "foo"
		assert user.password == "foopassword"
		assert user.group == "admin"

		user = auth.users[1]
		assert user.name == "bar"
		assert user.password == "barpassword"
	}
}
