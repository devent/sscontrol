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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.separate_domains_nonunix_accounts

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.mail.postfix.resources.ResourcesUtils

/**
 * Postfix/Hash/storage Ubuntu 10.04 resources for
 * shared domains, unix accounts.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum HashUbuntuResources {

	mailScript("Mail.groovy", HashUbuntuResources.class.getResource("MailHash.groovy")),
	profile("UbuntuProfile.groovy", HashUbuntuResources.class.getResource("HashUbuntuProfile.groovy")),
	mailnameExpected("/etc/mailname", HashUbuntuResources.class.getResource("mailname_expected.txt")),
	aliasDomainsExpected("/etc/postfix/alias_domains", HashUbuntuResources.class.getResource("alias_domains_expected.txt")),
	aliasMapsExpected("/etc/postfix/alias_maps", HashUbuntuResources.class.getResource("alias_maps_expected.txt")),
	maincfExpected("/etc/postfix/main.cf", HashUbuntuResources.class.getResource("maincf_expected.txt")),

	ResourcesUtils resources

	HashUbuntuResources(String path, URL resource) {
		this.resources = new ResourcesUtils(path: path, resource: resource)
	}

	String getPath() {
		resources.path
	}

	URL getResource() {
		resources.resource
	}

	File asFile(File parent) {
		resources.asFile parent
	}

	void createFile(File parent) {
		resources.createFile parent
	}

	void createCommand(File parent) {
		resources.createCommand parent
	}

	String replaced(File parent, def search, def replace) {
		resources.replaced parent, search, replace
	}

	String toString() {
		resources.toString()
	}
}
