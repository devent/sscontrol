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

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.httpd.statements.auth.Auth
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthGroup
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

class FileAuthProvider {

	@Inject
	FileAuthProviderLogger log

	Apache_2_2Script script

	void deployAuth(Domain domain, Auth auth) {
		makeAuthDirectory(domain)
		deployUsers(domain, auth, auth.users)
		auth.groups.each { AuthGroup group ->
			deployGroups(domain, auth, group)
		}
	}

	private makeAuthDirectory(Domain domain) {
		new File(script.domainDir(domain), script.authSubdirectory).mkdirs()
	}

	private deployUsers(Domain domain, Auth auth, List users) {
		if (users.empty) {
			return
		}
		def worker = script.scriptCommandFactory.create(
				script.commandsTemplate, "createPasswordFile",
				"command", script.htpasswdCommand,
				"file", passwordFile(domain, auth),
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	private deployGroups(Domain domain, Auth auth, AuthGroup group) {
		appendUsers(domain, auth, group.users)
		def string = script.configTemplate.getText true, "groupFile", "auth", auth
		FileUtils.write groupFile(domain, auth), string
	}

	private appendUsers(Domain domain, Auth auth, List users) {
		if (users.empty) {
			return
		}
		def worker = script.scriptCommandFactory.create(
				script.commandsTemplate, "appendPasswordFile",
				"command", script.htpasswdCommand,
				"file", passwordFile(domain, auth),
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	String passwordFile(Domain domain, Auth auth) {
		new File(script.domainDir(domain), "auth/${auth.name}.passwd").absolutePath
	}

	File groupFile(Domain domain, Auth auth) {
		new File(script.domainDir(domain), "auth/${auth.name}.group")
	}
}
