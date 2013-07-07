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

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
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
class PostfixLinuxBase {

	static mailService = PostfixLinuxBase.class.getResource("MailService.groovy")

	static mailSharedUnixAccounts = PostfixLinuxBase.class.getResource("MailSharedUnixAccounts.groovy")

	static mailSeparateDomainsUnixAccounts = PostfixLinuxBase.class.getResource("MailSeparateDomainsUnixAccounts.groovy")

	static mailSeparateDomainsNonUnixAccounts = PostfixLinuxBase.class.getResource("MailSeparateDomainsNonUnixAccounts.groovy")

	static mailname = PostfixLinuxBase.class.getResource("mailname.txt")

	static maincf = PostfixLinuxBase.class.getResource("maincf.txt")

	static mastercf = PostfixLinuxBase.class.getResource("mastercf.txt")

	static echoCommand = PostfixLinuxBase.class.getResource("echo_command.txt")

	static mailnameExpected = PostfixLinuxBase.class.getResource("mailname_expected.txt")

	static maincfExpected = PostfixLinuxBase.class.getResource("maincf_expected.txt")

	static maincfSharedUnixAccountsExpected = PostfixLinuxBase.class.getResource("maincf_shared_unix_accounts_expected.txt")

	static maincfSeparateDomainsUnixAccountsExpected = PostfixLinuxBase.class.getResource("maincf_separate_domains_unix_accounts_expected.txt")

	static maincfSeparateDomainsNonUnixAccountsExpected = PostfixLinuxBase.class.getResource("maincf_separate_domains_nonunix_accounts_expected.txt")

	static mastercfExpected = PostfixLinuxBase.class.getResource("mastercf_expected.txt")

	static aliasDomainsExpected = PostfixLinuxBase.class.getResource("alias_domains_expected.txt")

	static aliasMapsExpected = PostfixLinuxBase.class.getResource("alias_maps_expected.txt")

	static aliasMapsNonUnixExpected = PostfixLinuxBase.class.getResource("alias_maps_nonunix_expected.txt")

	static mailboxMapsExpected = PostfixLinuxBase.class.getResource("mailbox_maps_expected.txt")

	static mailboxMapsNonUnixExpected = PostfixLinuxBase.class.getResource("mailbox_maps_nonunix_expected.txt")

	static ubuntu1004Profile = PostfixLinuxBase.class.getResource("Ubuntu_10_04Profile.groovy")

	static Injector injector

	static ServiceLoaderFactory loaderFactory

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	ServicesRegistry registry

	SscontrolServiceLoader loader

	File tmpdir

	Map variables

	File aptitudeFile

	File restartFile

	File mailnameFile

	File maincfFile

	File mastercfFile

	File postmapFile

	File aliasDomainsFile

	File aliasMapsFile

	File mailboxMapsFile

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder("postfix-linux")
		aptitudeFile = new File(tmpdir, "/usr/bin/aptitude")
		restartFile = new File(tmpdir, "/sbin/restart")
		mailnameFile = new File(tmpdir, "/etc/mailname")
		maincfFile = new File(tmpdir, "/etc/postfix/main.cf")
		mastercfFile = new File(tmpdir, "/etc/postfix/master.cf")
		postmapFile = new File(tmpdir, "/usr/sbin/postmap")
		aliasDomainsFile = new File(tmpdir, "/etc/postfix/alias_domains")
		aliasMapsFile = new File(tmpdir, "/etc/postfix/alias_maps")
		mailboxMapsFile = new File(tmpdir, "/etc/postfix/mailbox_maps")
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
		Guice.createInjector(new CoreModule(), new CoreResourcesModule(),
				new UbuntuModule())
	}

	@BeforeClass
	static void setupToStringStyle() {
		toStringStyle
	}
}
