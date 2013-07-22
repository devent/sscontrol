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
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Before
import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.mail.postfix.linux.PostfixLinuxBase

/**
 * Test mail on a Ubuntu 10.04 server with virtual domains and users
 * stored in a MySQL database.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class MysqlUbuntu_10_04_Test extends PostfixLinuxBase {

	@Test
	void "virtual mysql"() {
		loader.loadService mysqlUbuntu1004Profile, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailMysql, profile

		copyResourceToCommand echoCommand, aptitudeFile
		copyResourceToCommand echoCommand, restartFile
		copyResourceToCommand echoCommand, postmapFile
		copyResourceToCommand echoCommand, postaliasFile
		copyResourceToCommand echoCommand, mysqlFile
		copyURLToFile maincf, maincfFile
		copyURLToFile mastercf, mastercfFile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		//registry.allServices.each { it.call() }

		def maincfFileString = readFileToString(maincfFile).replaceAll(/$tmpdir.absolutePath/, "/Ubuntu_10_04.tmp")
		assertFileContent mailnameFile, mailnameExpected
		assertStringContent maincfFileString, resourceToString(maincfMysqlExpected)
		assertFileContent mailboxCfFile, mailboxCf
		assertFileContent aliasCfFile, aliasCf
		assertFileContent domainsCfFile, domainsCf
	}

	static mailboxCf = PostfixLinuxBase.class.getResource("mysql_mailbox_cf_expected.txt")
	static aliasCf = PostfixLinuxBase.class.getResource("mysql_alias_cf_expected.txt")
	static domainsCf = PostfixLinuxBase.class.getResource("mysql_domains_cf_expected.txt")

	File mailboxCfFile
	File aliasCfFile
	File domainsCfFile

	@Before
	void createTempMysql() {
		mailboxCfFile = new File(tmpdir, "/etc/postfix/mysql_mailbox.cf")
		aliasCfFile = new File(tmpdir, "/etc/postfix/mysql_alias.cf")
		domainsCfFile = new File(tmpdir, "/etc/postfix/mysql_domains.cf")
	}
}
