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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.shared_domains_unix_accounts

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.shared_domains_unix_accounts.HashUbuntuResources.*
import static com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuTestUtil

/**
 * Postfix/Hash/storage Ubuntu 10.04 shared domains, unix accounts.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HashPostfixTest extends UbuntuTestUtil {

	@Test
	void "shared domains, unix accounts"() {
		copyUbuntuFiles tmpdir

		loader.loadService profile.resource, null
		def profile = registry.getService("profile")[0]
		loader.loadService mailScript.resource, profile

		registry.allServices.each { it.call() }
		log.info "Run service again to ensure that configuration is not set double."
		registry.allServices.each { it.call() }

		assertStringContent maincfExpected.replaced(tmpdir, tmpdir, "/tmp"), maincfExpected.toString()
		assertFileContent mailnameExpected.asFile(tmpdir), mailnameExpected
		assertFileContent aliases.asFile(tmpdir), aliases
		assertStringContent postaliasOut.replaced(tmpdir, tmpdir, "/tmp"), postaliasOut.toString()
	}
}
