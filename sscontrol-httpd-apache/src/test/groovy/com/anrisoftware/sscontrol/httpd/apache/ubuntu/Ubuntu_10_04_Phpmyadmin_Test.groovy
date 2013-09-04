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
class Ubuntu_10_04_Phpmyadmin_Test extends UbuntuTestUtil {

	@Test
	void "phpmyadmin"() {
		loader.loadService ubuntu1004Profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService httpdPhpmyadminScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent ubuntu1004DefaultConf.asFile(tmpdir), ubuntu1004DefaultConf
		assertFileContent phpmyadminDomainsConf.asFile(tmpdir), phpmyadminDomainsConf
		assertStringContent phpmyadminTest1comConf.replaced(tmpdir, tmpdir, "/tmp"), phpmyadminTest1comConf.toString()
		assertStringContent phpmyadminTest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), phpmyadminTest1comSslConf.toString()
		assertStringContent phpmyadminPhpadminTest1comSslConf.replaced(tmpdir, tmpdir, "/tmp"), phpmyadminPhpadminTest1comSslConf.toString()
		assertFileContent phpmyadminPhpadminTest1comSslFcgiScript.asFile(tmpdir), phpmyadminPhpadminTest1comSslFcgiScript
		assertStringContent phpmyadminChownOut.replaced(tmpdir, tmpdir, "/tmp"), phpmyadminChownOut.toString()
		assertStringContent phpmyadminChmodOut.replaced(tmpdir, tmpdir, "/tmp"), phpmyadminChmodOut.toString()
	}
}
