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
		deployUsers(auth, auth.users)
		auth.groups.each { AuthGroup group ->
			deployGroups(domain, auth, group)
		}
	}

	private deployUsers(Auth auth, List users) {
		def worker = script.scriptCommandFactory.create(
				script.commandsTemplate, "createPasswordFile",
				"command", script.htpasswdCommand,
				"file", passwordFile(auth),
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	private deployGroups(Domain domain, Auth auth, AuthGroup group) {
		appendUsers(auth, group.users)
		def string = script.configTemplate.getText true, "groupFile", "auth", auth
		FileUtils.write groupFile(domain, auth), string
	}

	private appendUsers(Auth auth, List users) {
		def worker = script.scriptCommandFactory.create(
				script.commandsTemplate, "appendPasswordFile",
				"command", script.htpasswdCommand,
				"file", passwordFile(auth),
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	String passwordFile(Auth auth) {
		new File(script.sitesDirectory, "auth/${auth.name}.passwd").absolutePath
	}

	File groupFile(Domain domain, Auth auth) {
		new File(script.sitesDirectory, "${domain.name}/auth/${auth.name}.group")
	}
}
