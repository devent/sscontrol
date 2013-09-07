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
package com.anrisoftware.sscontrol.ldap.openldap.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.ldap.dbindex.IndexTypeRenderer

/**
 * Configures OpenLDAP 2.4 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Openldap_2_4Script extends OpenldapScript {

	@Inject
	Openldap_2_4ScriptLogger log

	@Inject
	Openldap_2_4Config openldapConfig

	/**
	 * The {@link Templates} for the script.
	 */
	Templates openldapTemplates

	/**
	 * Resource containing the OpenLDAP configuration templates.
	 */
	TemplateResource ldapConfigTemplate

	/**
	 * Resource containing the OpenLDAP commands templates.
	 */
	TemplateResource ldapCommandsTemplate

	@Override
	def run() {
		openldapTemplates = templatesFactory.create "Openldap_2_4", [
			IndexType: new IndexTypeRenderer()
		]
		ldapConfigTemplate = openldapTemplates.getResource "config"
		ldapCommandsTemplate = openldapTemplates.getResource "commands"
		openldapConfig.script = this
		super.run()
		deployDefaultConfig()
		restartServices()
	}

	def deployDefaultConfig() {
		openldapConfig.deployConfig()
	}
}
