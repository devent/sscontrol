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
package com.anrisoftware.sscontrol.httpd.apache.authfile.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.authfile.ubuntu_10_04.AuthfileResources.*
import static com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuTestUtil

/**
 * Test Apache on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class AuthFileTest extends UbuntuTestUtil {

	@Test
	void "auth file"() {
		copyUbuntuFiles tmpdir
		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService httpdScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent defaultConf.asFile(tmpdir), defaultConf
		assertFileContent domainsConf.asFile(tmpdir), domainsConf
		assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
		assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
		assertFileContent privatepasswd.asFile(tmpdir), privatepasswd
		assertFileContent groupOut.asFile(tmpdir), groupOut
		assertFileContent enmodOut.asFile(tmpdir), enmodOut
	}

	@Test
	void "auth file appending"() {
		copyUbuntuFiles tmpdir
		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService httpdAppendingScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent defaultConf.asFile(tmpdir), defaultConf
		assertFileContent domainsConf.asFile(tmpdir), domainsConf
		assertStringContent test1comConf.replaced(tmpdir, tmpdir, "/tmp"), test1comConf.toString()
		assertStringContent test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), test1comSslConf.toString()
		assertFileContent appendingPrivatepasswd.asFile(tmpdir), appendingPrivatepasswd.toString()
		assertFileContent groupOut.asFile(tmpdir), groupOut.toString()
		assertFileContent enmodOut.asFile(tmpdir), enmodOut
	}
}
