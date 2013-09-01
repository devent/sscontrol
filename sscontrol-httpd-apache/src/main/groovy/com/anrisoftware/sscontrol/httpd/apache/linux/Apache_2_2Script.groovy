/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux

import static org.apache.commons.io.FileUtils.*

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates

/**
 * Configures Apache 2.2 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Apache_2_2Script extends ApacheScript {

	/**
	 * The {@link Templates} for the script.
	 */
	Templates apacheTemplates

	/**
	 * Resource containing the Apache configuration templates.
	 */
	TemplateResource configTemplate

	@Override
	def run() {
		apacheTemplates = templatesFactory.create "Apache_2_2"
		configTemplate = apacheTemplates.getResource "config"
		super.run()
		deployDefaultConfig()
		deployDomainsConfig()
		deployConfig()
	}

	def deployDefaultConfig() {
		def string = configTemplate.getText true, "defaultConfiguration"
		FileUtils.write defaultConfigFile, string
	}

	def deployDomainsConfig() {
		def string = configTemplate.getText true, "domainsConfiguration", "service", service
		FileUtils.write domainsConfigFile, string
	}

	def deployConfig() {
		service.domains.each {
			def string = configTemplate.getText true, it.class.simpleName, "properties", this, "domain", it
			FileUtils.write new File(configurationDir, "sites-available/${it.fileName}"), string
		}
	}
}
