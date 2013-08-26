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

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * Uses Apache service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ApacheScript extends LinuxScript {

	@Inject
	ApacheScriptLogger log

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
		distributionSpecificConfiguration()
	}

	/**
	 * Run the distribution specific configuration.
	 */
	abstract distributionSpecificConfiguration()

	/**
	 * Returns the ufw tool command.
	 *
	 * <ul>
	 * <li>profile property {@code "ufw_command"}</li>
	 * </ul>
	 */
	abstract String getUfwCommand()
}
