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
package com.anrisoftware.sscontrol.dhclient.linux

import groovy.util.logging.Slf4j

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.Service

/**
 * Setups the dhclient service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class LinuxScript extends Script {

	/**
	 * {@link TemplatesFactory} to create the templates.
	 */
	TemplatesFactory templatesFactory

	/**
	 * The {@link Templates} for the script.
	 */
	Templates templates

	/**
	 * The name of the profile for the script.
	 */
	String name

	/**
	 * {@link Map} of the available {@link Worker} workers.
	 */
	Map workers

	/**
	 * The system {@link ProfileProperties} profile properties.
	 */
	ProfileProperties system

	/**
	 * The service {@link ProfileProperties} profile properties.
	 */
	ProfileProperties profile

	/**
	 * The script {@link Service}.
	 */
	Service service

	@Override
	def run() {
	}

	/**
	 * Set properties of the script.
	 */
	@Override
	void setProperty(String property, Object newValue) {
		metaClass.setProperty(this, property, newValue)
	}

	/**
	 * Returns the name of the service.
	 */
	String getServiceName() {
		service.name
	}

	@Override
	String toString() {
		"${service.toString()}: $name"
	}
}
