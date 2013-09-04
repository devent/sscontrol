/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux

import static java.lang.String.format

import java.text.DecimalFormat

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

class DomainConfig {

	int domainNumber

	ApacheScript script

	Map domainUsers

	DomainConfig() {
		this.domainNumber = 0
		this.domainUsers = [:]
	}

	def deployDomain(Domain domain) {
		domainNumber++
		def group = new DecimalFormat(groupPattern).format(domainNumber)
		def user = new DecimalFormat(userPattern).format(domainNumber)
		addSiteGroup group
		addSiteUser domain, user, group
		createWebDir domain, user, group
		domainUsers.put domain.name, [group: group, user: user]
	}

	private createWebDir(Domain domain, String user, String group) {
		webDir(domain).mkdirs()
		changeOwner user: user, group: group, files: webDir(domain)
	}

	private addSiteUser(Domain domain, String user, String group) {
		int uid = minimumUid + domainNumber
		def home = domainDir domain
		def shell = "/bin/false"
		addUser user, group, uid, home, shell
	}

	private addSiteGroup(String group) {
		int gid = minimumGid + domainNumber
		addGroup group, gid
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
