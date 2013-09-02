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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry

/**
 * Test Apache on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_10_04_Test extends UbuntuTestUtil {

	@Test
	void "apache domains"() {
		loader.loadService ubuntu1004Profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService httpdScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent ubuntu1004DefaultConf.file(tmpdir), ubuntu1004DefaultConf
		assertFileContent ubuntu1004DomainsConf.file(tmpdir), ubuntu1004DomainsConf
		assertStringContent ubuntu1004Test1comConf.replaced(tmpdir, tmpdir, "/tmp"), ubuntu1004Test1comConf.toString()
		assertStringContent ubuntu1004Test1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), ubuntu1004Test1comSslConf.toString()
		assert ubuntu1004Test1comWeb.file(tmpdir).isDirectory()
		assertFileContent ubuntu1004Test1comCrt.file(tmpdir), ubuntu1004Test1comCrt
		assertFileContent ubuntu1004Test1comKey.file(tmpdir), ubuntu1004Test1comKey
		assertFileContent ubuntu1004Test1comEnsiteOut.file(tmpdir), ubuntu1004Test1comEnsiteOut
	}
}
