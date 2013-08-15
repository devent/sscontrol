/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.ubuntu.MysqlUbuntu10_04Resources.*
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
		registry.allServices.each { it.call() }

		assertFileContent mailnameFile, mailnameExpected
		def maincfFileString = readFileToString(mainCfFile).replaceAll(/$tmpdir.absolutePath/, "/Ubuntu_10_04.tmp")
		assertStringContent maincfFileString, mainCf.toString()
		assertFileContent mailboxCfFile, mailboxCf.resource
		assertFileContent aliasCfFile, aliasCf.resource
		assertFileContent domainsCfFile, domainsCf.resource
		assertFileContent aptitudeOutFile, aptitudeOut.resource
		assertFileContent mysqlOutFile, mysqlOut.resource
		def postaliasOutFileString = readFileToString(postaliasOutFile).replaceAll(/$tmpdir.absolutePath/, "/Ubuntu_10_04.tmp")
		assertStringContent postaliasOutFileString, postaliasOut.toString()
	}

	File mainCfFile
	File mailboxCfFile
	File aliasCfFile
	File domainsCfFile
	File aptitudeOutFile
	File mysqlOutFile
	File postaliasOutFile

	@Before
	void createTempMysql() {
		mainCfFile = new File(tmpdir, mainCf.fileName)
		mailboxCfFile = new File(tmpdir, mailboxCf.fileName)
		aliasCfFile = new File(tmpdir, aliasCf.fileName)
		domainsCfFile = new File(tmpdir, domainsCf.fileName)
		aptitudeOutFile = new File(tmpdir, aptitudeOut.fileName)
		mysqlOutFile = new File(tmpdir, mysqlOut.fileName)
		postaliasOutFile = new File(tmpdir, postaliasOut.fileName)
	}
}
