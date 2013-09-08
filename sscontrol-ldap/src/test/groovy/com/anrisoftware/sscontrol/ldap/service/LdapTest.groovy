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
package com.anrisoftware.sscontrol.ldap.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.ldap.service.LdapResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * @see LdapServiceImpl
 * @see Ldap
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LdapTest extends LdapTestUtil {

	@Test
	void "ldap script"() {
		loader.loadService ubuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService ldapScript, profile
		LdapServiceImpl service = registry.getService("ldap")[0]
		service.organization.name == "ubuntutest"
		service.organization.domain.size() == 2
		service.organization.domain[0] == "ubuntutest"
		service.organization.domain[1] == "com"
		service.organization.admin.name == "admin"
		service.organization.admin.password == "adminpass"
		service.organization.admin.domain.size() == 2
		service.organization.admin.domain[0] == "ubuntutest"
		service.organization.admin.domain[1] == "com"
		service.indices.size() == 2
		service.scripts.each { String it ->
			assert it.startsWith("# LDIF Export for o=deventorg,dc=ubuntutest,dc=com")
		}
	}
}
