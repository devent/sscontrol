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

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector


class MysqlLinuxUtil {

	static databaseScript = MysqlLinuxUtil.class.getResource("Database.groovy")

	static aptitudeCommand = MysqlLinuxUtil.class.getResource("echo_command.txt")

	static restartCommand = MysqlLinuxUtil.class.getResource("echo_command.txt")

	static mysqladminCommand = MysqlLinuxUtil.class.getResource("mysqladmin_command.txt")

	static mysqlCommand = MysqlLinuxUtil.class.getResource("echo_command.txt")

	static postfixTables = MysqlLinuxUtil.class.getResource("postfixtables.txt")

	Injector injector

	File tmpdir

	Map variables

	File aptitude

	File restart

	File mysqladmin

	File mysql

	File postfixtables

	File confd

	File sscontrolMysqld

	@Before
	void createTemp() {
		tmpdir = File.createTempDir this.class.simpleName, null
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		restart = new File(tmpdir, "/etc/init.d/mysql")
		mysqladmin = new File(tmpdir, "/usr/bin/mysqladmin")
		mysql = new File(tmpdir, "/usr/bin/mysql")
		postfixtables = new File(tmpdir, "/tmp/postfixtables.sql")
		confd = new File(tmpdir, "etc/mysql/conf.d")
		sscontrolMysqld = new File(tmpdir, "/etc/mysql/conf.d/sscontrol_mysqld.cnf")
		variables = [tmp: tmpdir.absoluteFile]
	}

	@After
	void deleteTemp() {
		tmpdir.deleteDir()
	}

	@Before
	void createFactories() {
		injector = createInjector()
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}

	static Injector createInjector() {
		Guice.createInjector(new CoreModule(), new CoreResourcesModule())
	}
}
