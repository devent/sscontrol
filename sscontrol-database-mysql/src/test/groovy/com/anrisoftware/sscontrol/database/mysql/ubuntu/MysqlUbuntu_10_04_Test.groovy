/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.mysql.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.google.inject.Injector

/**
 * Test MySQL on a Ubuntu 10.04 server.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MysqlUbuntu_10_04_Test extends MysqlLinuxUtil {

	static ubuntu1004Profile = resourceURL("Ubuntu_10_04Profile.groovy", MysqlUbuntu_10_04_Test)

	static mysqldExpected = resourceURL("mysqld_cnf.txt", MysqlUbuntu_10_04_Test)

	@Test
	void "database script"() {
		ServicesRegistry registry = injector.getInstance ServicesRegistry
		SscontrolServiceLoader loader = injector.getInstance SscontrolServiceLoader
		loader.loadService(ubuntu1004Profile, variables, registry, null)
		def profile = registry.getService("profile")[0]
		loader.loadService(databaseScript, variables, registry, profile)
		withFiles "database", {
			registry.allServices.each { it.call() }
			assertFiles(it)
			log.info "Run service again to ensure that configuration is not set double."
			registry.allServices.each { it.call() }
			assertFiles(it)
		}, {
			new File(it, "etc/mysql/conf.d").mkdirs()
			copyResourceToCommand aptitudeCommand, new File(it, "/usr/bin/aptitude")
			copyResourceToCommand restartCommand, new File(it, "/etc/init.d/mysql")
			copyResourceToCommand mysqladminCommand, new File(it, "/usr/bin/mysqladmin")
			copyResourceToCommand mysqlCommand, new File(it, "/usr/bin/mysql")
			copyResourceToFile postfixTables, new File(it, "/tmp/postfixtables.sql")
		}, tmp
	}

	private assertFiles(it) {
		assertFileContent(new File(it, "/etc/mysql/conf.d/sscontrol_mysqld.cnf"), mysqldExpected)
	}
}
