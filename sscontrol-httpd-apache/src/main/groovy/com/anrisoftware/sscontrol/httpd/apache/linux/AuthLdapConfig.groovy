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

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdap
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Server LDAP/authentication configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthLdapConfig extends AuthConfig {

	@Inject
	AuthLdapConfigLogger log

	/**
	 * File/auth configuration.
	 */
	TemplateResource authConfigTemplate

	/**
	 * File/auth commands.
	 */
	TemplateResource authCommandsTemplate

	def deployAuth(Domain domain, AuthLdap auth, List serviceConfig) {
		super.deployAuth(domain, auth, serviceConfig)
		authConfigTemplate = apacheTemplates.getResource "authldap_config"
		createDomainConfig domain, auth, serviceConfig
		enableMods "authnz_ldap"
	}

	void createDomainConfig(Domain domain, AuthLdap auth, List serviceConfig) {
		def config = authConfigTemplate.getText(true, "domainAuth",
				"domain", domain,
				"properties", script,
				"auth", auth)
		serviceConfig << config
		log.domainConfigCreated script, auth
	}
}
