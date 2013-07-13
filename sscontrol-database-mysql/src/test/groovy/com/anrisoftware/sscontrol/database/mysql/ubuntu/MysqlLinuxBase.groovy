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

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector


/**
 * Loads resources and setups files to test MySQL database service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MysqlLinuxBase {

	static databaseScript = MysqlLinuxBase.class.getResource("Database.groovy")

	static echoCommand = MysqlLinuxBase.class.getResource("echo_command.txt")

	static mysqladminCommand = MysqlLinuxBase.class.getResource("mysqladmin_command.txt")

	static postfixTables = MysqlLinuxBase.class.getResource("postfixtables.txt")

	static ubuntu1004Profile = MysqlLinuxBase.class.getResource("Ubuntu_10_04Profile.groovy")

	static mysqldExpected = MysqlLinuxBase.class.getResource("mysqld_cnf.txt")

	static aptitudeOutExpected = MysqlLinuxBase.class.getResource("aptitude_out.txt")

	static restartOutExpected = MysqlLinuxBase.class.getResource("restart_out.txt")

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	File tmpdir

	Map variables

	ServicesRegistry registry

	SscontrolServiceLoader loader

	File aptitude

	File restart

	File mysqladmin

	File mysql

	File postfixtables

	File confd

	File sscontrolMysqld

	File restartOut

	File aptitudeOut

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder()
		aptitude = new File(tmpdir, "/usr/bin/aptitude")
		restart = new File(tmpdir, "/sbin/restart")
		mysqladmin = new File(tmpdir, "/usr/bin/mysqladmin")
		mysql = new File(tmpdir, "/usr/bin/mysql")
		postfixtables = new File(tmpdir, "/tmp/postfixtables.sql")
		confd = new File(tmpdir, "etc/mysql/conf.d")
		sscontrolMysqld = new File(tmpdir, "/etc/mysql/conf.d/sscontrol_mysqld.cnf")
		restartOut = new File(tmpdir, "/sbin/restart.out")
		aptitudeOut = new File(tmpdir, "/usr/bin/aptitude.out")
		variables = [tmp: tmpdir.absoluteFile]
	}

	@Before
	void createRegistry() {
		registry = injector.getInstance ServicesRegistry
		loader = loaderFactory.create registry, variables
		loader.setParent injector
	}

	@BeforeClass
	static void createFactories() {
		injector = createInjector()
		loaderFactory = injector.getInstance ServiceLoaderFactory
	}

	static Injector createInjector() {
		Guice.createInjector(new CoreModule(), new CoreResourcesModule())
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
