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
		setupDefaultAuth auth
		makeAuthDirectory(domain)
		removeUsers(domain, auth)
		deployUsers(domain, auth, auth.users)
		auth.groups.each { AuthGroup group ->
			deployGroups(domain, auth, group)
		}
	}

	private setupDefaultAuth(Auth auth) {
		auth.type = auth.type == null ? script.defaultAuthType : auth.type
		auth.provider = auth.provider == null ? script.defaultAuthProvider : auth.provider
	}

	private removeUsers(Domain domain, Auth auth) {
		if (!auth.appending) {
			passwordFile(domain, auth).delete()
		}
	}

	private makeAuthDirectory(Domain domain) {
		new File(domainDir(domain), authSubdirectory).mkdirs()
	}

	private deployUsers(Domain domain, Auth auth, List users) {
		if (users.empty) {
			return
		}
		def worker = script.scriptCommandFactory.create(
				apacheCommandsTemplate, "appendDigestPasswordFile",
				"file", passwordFile(domain, auth),
				"auth", auth,
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	private deployGroups(Domain domain, Auth auth, AuthGroup group) {
		deployUsers(domain, auth, group.users)
		def string = configTemplate.getText true, "groupFile", "auth", auth
		FileUtils.write groupFile(domain, auth), string
	}

	File passwordFile(Domain domain, Auth auth) {
		new File(domainDir(domain), "auth/${auth.passwordFileName}")
	}

	File groupFile(Domain domain, Auth auth) {
		new File(domainDir(domain), "auth/${auth.name}.group")
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
