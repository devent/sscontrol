/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * Uses the UFW service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UfwScript extends LinuxScript {

	@Inject
	UfwScriptLogger log

	@Inject
	@Named("ufw-linux-properties")
	ContextProperties linuxProperties

	/**
	 * The {@link Templates} for the script.
	 */
	Templates ufwTemplates

	/**
	 * Resource containing the MaraDNS configuration templates.
	 */
	TemplateResource rules

	@Override
	def run() {
		super.run()
		ufwTemplates = templatesFactory.create "Ufw"
		rules = ufwTemplates.getResource "rules"
		distributionSpecificConfiguration()
		deployRules()
	}

	/**
	 * Run the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}

	/**
	 * Deploys the firewall rules.
	 */
	void deployRules() {
		def worker = scriptCommandFactory.create(rules,
						"service", service,
						"ufwCommand", ufwCommand,
						"prefix", system.prefix)()
		log.deployedRules this, worker
	}

	/**
	 * Returns the ufw command.
	 *
	 * <ul>
	 * <li>property key {@code ufw_command}</li>
	 * </ul>
	 */
	abstract String getUfwCommand()
}
