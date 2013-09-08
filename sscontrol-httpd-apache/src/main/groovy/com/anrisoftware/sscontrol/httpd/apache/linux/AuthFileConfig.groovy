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

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.statements.authfile.AuthFile
import com.anrisoftware.sscontrol.httpd.statements.authfile.FileGroup
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Server file/authentication configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileConfig extends AuthConfig {

	@Inject
	AuthFileConfigLogger log

	/**
	 * File/auth configuration.
	 */
	TemplateResource authConfigTemplate

	/**
	 * File/auth commands.
	 */
	TemplateResource authCommandsTemplate

	def deployAuth(Domain domain, AuthFile auth, List serviceConfig) {
		super.deployAuth(domain, auth, serviceConfig)
		authConfigTemplate = apacheTemplates.getResource "authfile_config"
		authCommandsTemplate = apacheTemplates.getResource "authfile_commands"
		createDomainConfig domain, auth, serviceConfig
		makeAuthDirectory(domain)
		removeUsers(domain, auth)
		deployUsers(domain, auth, auth.users)
		auth.groups.each { FileGroup group ->
			deployGroups(domain, auth, group)
		}
	}

	void createDomainConfig(Domain domain, AuthFile auth, List serviceConfig) {
		def config = authConfigTemplate.getText(true, "domainAuth",
				"domain", domain,
				"properties", script,
				"auth", auth)
		serviceConfig << config
	}

	private removeUsers(Domain domain, AuthFile auth) {
		if (!auth.appending) {
			passwordFile(domain, auth).delete()
		}
	}

	private makeAuthDirectory(Domain domain) {
		new File(domainDir(domain), authSubdirectory).mkdirs()
	}

	private deployUsers(Domain domain, AuthFile auth, List users) {
		if (users.empty) {
			return
		}
		def worker = scriptCommandFactory.create(
				authCommandsTemplate, "appendDigestPasswordFile",
				"file", passwordFile(domain, auth),
				"auth", auth,
				"users", users)()
		log.deployAuthUsers script, worker, auth
	}

	private deployGroups(Domain domain, AuthFile auth, FileGroup group) {
		deployUsers(domain, auth, group.users)
		def string = authConfigTemplate.getText true, "groupFile", "auth", auth
		FileUtils.write groupFile(domain, auth), string
	}

	File passwordFile(Domain domain, AuthFile auth) {
		new File(domainDir(domain), "auth/${auth.passwordFileName}")
	}

	File groupFile(Domain domain, AuthFile auth) {
		new File(domainDir(domain), "auth/${auth.location}.group")
	}
}
