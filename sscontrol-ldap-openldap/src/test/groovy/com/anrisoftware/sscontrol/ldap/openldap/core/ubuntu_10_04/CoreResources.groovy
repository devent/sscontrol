/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap-openldap.
 *
 * sscontrol-ldap-openldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap-openldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap-openldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.openldap.core.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum CoreResources {

	ldapScript("Ldap.groovy", CoreResources.class.getResource("Ldap.groovy")),
	ldapaddOut("/usr/bin/ldapadd.out", CoreResources.class.getResource("ldapadd_out.txt")),
	ldapmodifyOut("/usr/bin/ldapmodify.out", CoreResources.class.getResource("ldapmodify_out.txt")),
	dbConf("/etc/ldap/db.ldif", CoreResources.class.getResource("db_ldif.txt")),
	baseConf("/etc/ldap/base.ldif", CoreResources.class.getResource("base_ldif.txt")),
	systemConf("/etc/ldap/config.ldif", UbuntuResources.class.getResource("config_ldif.txt")),
	ldapConf("/etc/ldap/acl.ldif", UbuntuResources.class.getResource("acl_ldif.txt")),
	chmodOut("/bin/chmod.out", UbuntuResources.class.getResource("chmod_out.txt")),
	restartOut("/etc/init.d/slapd.out", UbuntuResources.class.getResource("restart_out.txt")),

	ResourcesUtils resources

	CoreResources(String path, URL resource) {
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
