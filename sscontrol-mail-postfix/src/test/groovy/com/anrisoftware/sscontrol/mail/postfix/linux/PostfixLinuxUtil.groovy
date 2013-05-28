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
package com.anrisoftware.sscontrol.mail.postfix.linux

import static com.anrisoftware.globalpom.utils.TestUtils.*
import groovy.util.logging.Slf4j

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass

import com.anrisoftware.sscontrol.core.modules.CoreModule
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.anrisoftware.sscontrol.mail.postfix.ubuntu.UbuntuModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Utilities and resources to test the postfix service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class PostfixLinuxUtil {

	static mailService = PostfixLinuxUtil.class.getResource("MailService.groovy")

	static mailSharedUnixAccounts = PostfixLinuxUtil.class.getResource("MailSharedUnixAccounts.groovy")

	static mailname = PostfixLinuxUtil.class.getResource("mailname.txt")

	static maincf = PostfixLinuxUtil.class.getResource("maincf.txt")

	static mastercf = PostfixLinuxUtil.class.getResource("mastercf.txt")

	static echoCommand = PostfixLinuxUtil.class.getResource("echo_command.txt")

	static mailnameExpected = PostfixLinuxUtil.class.getResource("mailname_expected.txt")

	static maincfExpected = PostfixLinuxUtil.class.getResource("maincf_expected.txt")

	static maincfSharedUnixAccountsExpected = PostfixLinuxUtil.class.getResource("maincf_shared_unix_accounts_expected.txt")

	static mastercfExpected = PostfixLinuxUtil.class.getResource("mastercf_expected.txt")

	Injector injector

	File tmpdir

	Map variables

	File aptitudeFile

	File restartFile

	File mailnameFile

	File maincfFile

	File mastercfFile

	@Before
	void createTemp() {
		tmpdir = File.createTempDir this.class.simpleName, null
		aptitudeFile = new File(tmpdir, "/usr/bin/aptitude")
		restartFile = new File(tmpdir, "/sbin/restart")
		mailnameFile = new File(tmpdir, "/etc/mailname")
		maincfFile = new File(tmpdir, "/etc/postfix/main.cf")
		mastercfFile = new File(tmpdir, "/etc/postfix/master.cf")
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
		Guice.createInjector(new CoreModule(), new CoreResourcesModule(),
				new UbuntuModule())
	}
}
