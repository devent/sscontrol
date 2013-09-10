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
package com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.CoreResources.*
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
class DomainsTest extends UbuntuTestUtil {

	@Test
	void "apache domains"() {
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
		assert test1comWeb.asFile(tmpdir).isDirectory()
		assertFileContent test1comCrt.asFile(tmpdir), test1comCrt
		assertFileContent test1comKey.asFile(tmpdir), test1comKey
		assertFileContent ensiteOut.asFile(tmpdir), ensiteOut
		assertFileContent enmodOut.asFile(tmpdir), enmodOut
		assertStringContent useraddOut.replaced(tmpdir, tmpdir, "/tmp"), useraddOut.toString()
		assertStringContent groupaddOut.replaced(tmpdir, tmpdir, "/tmp"), groupaddOut.toString()
		assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
	}
}