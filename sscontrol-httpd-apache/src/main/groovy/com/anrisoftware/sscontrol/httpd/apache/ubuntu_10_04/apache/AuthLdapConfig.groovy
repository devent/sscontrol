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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE;

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.AuthConfig;
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.BasicAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdap
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Auth/LDAP configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthLdapConfig extends BasicAuth implements AuthConfig {

	public static final String NAME = "AuthLdap"

	@Inject
	AuthLdapConfigLogger log

	/**
	 * Auth/ldap configuration.
	 */
	TemplateResource authConfigTemplate

	/**
	 * Auth/ldap commands.
	 */
	TemplateResource authCommandsTemplate

	@Override
	String getProfile() {
		PROFILE
	}

	@Override
	String getAuthName() {
		NAME
	}

	@Override
	void deployAuth(Domain domain, AbstractAuth auth, List serviceConfig) {
		authConfigTemplate = apacheTemplates.getResource "authldap_config"
		setupDefaultProperties auth
		createDomainConfig domain, auth, serviceConfig
		enableMods auth
	}

	def setupDefaultProperties(AbstractAuth auth) {
		auth.type = AuthType.basic
	}

	void createDomainConfig(Domain domain, AuthLdap auth, List serviceConfig) {
		def config = authConfigTemplate.getText(true, "domainAuth",
				"domain", domain,
				"properties", script,
				"auth", auth)
		serviceConfig << config
		log.domainConfigCreated script, auth
	}

	private enableMods(AbstractAuth auth) {
		switch (auth.type) {
			case AuthType.basic:
				enableMods(["authnz_ldap", "auth_basic"])
				break
		}
	}
}
