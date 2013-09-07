/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.core.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.ldap.openldap.ubuntu.UbuntuTestUtil

/**
 * Test OpenLDAP on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class CoreTest extends UbuntuTestUtil {

	@Test
	void "ldap core"() {
		UbuntuResources.copyUbuntuFiles tmpdir
		loader.loadService UbuntuResources.profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService CoreResources.ldapScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent CoreResources.dbConf.asFile(tmpdir), CoreResources.dbConf
		//assertFileContent CoreResources.baseConf.asFile(tmpdir), CoreResources.baseConf
		//assertFileContent CoreResources.systemConf.asFile(tmpdir), CoreResources.systemConf
		//assertFileContent CoreResources.ldapConf.asFile(tmpdir), CoreResources.ldapConf
		assertStringContent CoreResources.chmodOut.replaced(tmpdir, tmpdir, "/tmp"), CoreResources.chmodOut.toString()
		assertStringContent CoreResources.ldapaddOut.replaced(tmpdir, tmpdir, "/tmp"), CoreResources.ldapaddOut.toString()
	}
}
