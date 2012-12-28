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
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.Service
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Uses the UFW service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScript extends Script {

	@Inject
	LinuxScriptLogger log

	@Inject
	@Named("ufw-linux-default-properties")
	ContextProperties defaultProperties

	@Inject
	TemplatesFactory templatesFactory

	@Inject
	TokensTemplateWorkerFactory tokensTemplateWorkerFactory

	@Inject
	ScriptCommandWorkerFactory scriptCommandWorkerFactory

	/**
	 * The {@link Templates} for the script.
	 */
	Templates templates

	/**
	 * The command {@link Templates} for the script command worker.
	 */
	Templates commandTemplates

	/**
	 * Resource containing the MaraDNS configuration templates.
	 */
	TemplateResource rules

	/**
	 * Resource containing the zone database file templates.
	 */
	TemplateResource zoneConfiguration

	/**
	 * Resource containing the commands regarding the MaraDNS service.
	 */
	TemplateResource serviceCommands

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

	def run() {
		templates = templatesFactory.create "Ufw_$name"
		commandTemplates = templatesFactory.create "ScriptCommandTemplates"
		rules = templates.getResource("rules")
		installPackages(profile.packages)
		deployConfiguration()
	}

	/**
	 * Install needed packages for the Ufw service.
	 */
	def installPackages(def packages) {
		def template = commandTemplates.getResource("install")
		def worker = scriptCommandWorkerFactory.create(
						template, "installCommand", installCommand,
						"packages", packages)()
		log.installPackagesDone this, worker, profile
	}

	String getInstallCommand() {
		def command = system.install_command
		command != null ? command : defaultProperties.getProperty("install_command")
	}

	/**
	 * Deploy the MaraDNS configuration.
	 */
	def deployConfiguration() {
		deployRules()
	}

	def deployRules() {
		def worker = scriptCommandWorkerFactory.create(rules,
						"service", service,
						"ufwCommand", ufwCommand,
						"prefix", system.prefix)()
		log.deployedRules this, worker
	}

	/**
	 * Returns the ufw command.
	 */
	String getUfwCommand() {
		def command = profile.ufw_command
		command != null ? command : defaultProperties.getProperty("ufw_command")
	}

	/**
	 * Returns the name of the service.
	 */
	String getServiceName() {
		service.name
	}

	String toString() {
		"${service.toString()}: $name"
	}

	/**
	 * Set properties of the script.
	 */
	@Override
	void setProperty(String property, Object newValue) {
		metaClass.setProperty(this, property, newValue)
	}
}
