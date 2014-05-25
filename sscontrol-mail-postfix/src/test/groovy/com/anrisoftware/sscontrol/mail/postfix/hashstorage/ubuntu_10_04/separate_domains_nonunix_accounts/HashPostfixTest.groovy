/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.separate_domains_nonunix_accounts

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.separate_domains_nonunix_accounts.HashUbuntuResources.*
import static com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuTestUtil

/**
 * Postfix/Hash/storage Ubuntu 10.04 for
 * shared domains, unix accounts.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HashPostfixTest extends UbuntuTestUtil {

	@Test
	void "separate domains, nonunix accounts"() {
		copyUbuntuFiles tmpdir

		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent mailnameExpected.asFile(tmpdir), mailnameExpected
		assertStringContent maincfExpected.replaced(tmpdir, tmpdir, "/tmp"), maincfExpected.toString()
		assertFileContent aliasDomainsExpected.asFile(tmpdir), aliasDomainsExpected
		assertFileContent aliasMapsExpected.asFile(tmpdir), aliasMapsExpected
		assertFileContent mailboxMapsExpected.asFile(tmpdir), mailboxMapsExpected
		assertStringContent chownOut.replaced(tmpdir, tmpdir, "/tmp"), chownOut.toString()
		assertFileContent useraddOut.asFile(tmpdir), useraddOut
		assertFileContent groupaddOut.asFile(tmpdir), groupaddOut
		assertStringContent postaliasOut.replaced(tmpdir, tmpdir, "/tmp"), postaliasOut.toString()
		assert mailboxBaseDir.asFile(tmpdir).isDirectory()
	}

	@Test
	void "separate domains, nonunix accounts, reset domains"() {
		copyUbuntuFiles tmpdir

		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailResetDomainsScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertFileContent aliasDomainsExpected.asFile(tmpdir), aliasDomainsExpected
		assertFileContent aliasMapsExpected.asFile(tmpdir), aliasMapsExpected
		assertFileContent mailboxMapsExpected.asFile(tmpdir), mailboxMapsExpected
		assertStringContent postaliasOut.replaced(tmpdir, tmpdir, "/tmp"), postaliasOut.toString()
	}
}
