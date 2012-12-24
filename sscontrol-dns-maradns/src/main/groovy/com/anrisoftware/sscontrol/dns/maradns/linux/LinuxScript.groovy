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
package com.anrisoftware.sscontrol.dns.maradns.linux

import javax.inject.Inject
import javax.inject.Named

import org.apache.commons.io.FileUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.Service
import com.anrisoftware.sscontrol.dns.statements.DnsZone
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Deploys the MaraDNS service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScript extends Script {

	@Inject
	LinuxScriptLogger log

	@Inject
	@Named("maradns-linux-default-properties")
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
	TemplateResource configuration

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
		templates = templatesFactory.create "Maradns_$name"
		commandTemplates = templatesFactory.create "ScriptCommandTemplates"
		configuration = templates.getResource("configuration")
		enableUniverseRepository()
		installPackages(profile.packages)
		deployConfiguration()
		restartService()
	}

	/**
	 * Enables the "universe" repository where MaraDNS can be found.
	 */
	def enableUniverseRepository() {
		installPackages(system.packages)
		def template = commandTemplates.getResource("command")
		def command = system.get("enable_repository_command", "lucid", "universe")
		def worker = scriptCommandWorkerFactory.create(
						template, "command", command)()
		log.installPackagesDone this, worker, profile
	}

	/**
	 * Install needed packages for the dns service.
	 */
	def installPackages(def packages) {
		def template = commandTemplates.getResource("install")
		def worker = scriptCommandWorkerFactory.create(
						template, "installCommand", system.install_command,
						"packages", packages)()
		log.installPackagesDone this, worker, profile
	}

	/**
	 * Deploy the MaraDNS configuration.
	 */
	def deployConfiguration() {
		deployCsvHash()
		deployBindAddress()
		deployZones()
	}

	def deployCsvHash() {
		def search = configuration.getText(true, "csv_hash_search")
		def replace = configuration.getText(true, "csv_hash")
		def tokenTemplate = new TokenTemplate(search, replace)
		def worker = tokensTemplateWorkerFactory.create(tokens, tokenTemplate, mararcConfiguration)()
		FileUtils.write(mararcFile, worker.text, system.charset)
	}

	def deployBindAddress() {
		def search = configuration.getText(true, "bind_address_search")
		def replace = configuration.getText(true, "bind_address", "service", service)
		def tokenTemplate = new TokenTemplate(search, replace)
		def worker = tokensTemplateWorkerFactory.create(tokens, tokenTemplate, mararcConfiguration)()
		FileUtils.write(mararcFile, worker.text, system.charset)
	}

	def deployZones() {
		service.zones.each { DnsZone it ->
			def search = configuration.getText(true, "zone_search", "zone", it)
			def replace = configuration.getText(true, "zone", "zone", it)
			def tokenTemplate = new TokenTemplate(search, replace)
			def worker = tokensTemplateWorkerFactory.create(tokens, tokenTemplate, mararcConfiguration)()
			FileUtils.write(mararcFile, worker.text, system.charset)
		}
	}

	def restartService() {
	}

	/**
	 * Returns the template tokens for the dns configuration.
	 */
	TokenMarker getTokens() {
		new TokenMarker("# SSCONTROL-$serviceName", "# SSCONTROL-$serviceName-END\n")
	}

	/**
	 * Returns path of the MaraDNS configuration directory.
	 */
	File getConfigurationDir() {
		def file = profile.configuration_dir as File
		file != null ? file : defaultProperties.getFileProperty("configuration_dir")
	}

	/**
	 * Returns the file of the {@code mararc} configuration file.
	 */
	File getMararcFile() {
		def file = defaultProperties.getProperty("configuration_file", "mararc")
		new File(configurationDir, file)
	}

	/**
	 * Returns the current {@code mararc} configuration.
	 */
	String getMararcConfiguration() {
		if (mararcFile.isFile()) {
			FileUtils.readFileToString(mararcFile, system.charset)
		} else {
			log.noMararcConfigurationFound this, mararcFile
			""
		}
	}

	/**
	 * Returns the name of the service.
	 */
	String getServiceName() {
		service.name
	}

	/**
	 * Restarts the hostname service.
	 */
	def restartHostnameService() {
		def template = templates.getResource("restart_hostname_command")
		def worker = scriptCommandWorkerFactory.create(template, "prefix", system.prefix)()
		log.info "Restart done with output '{}'.", worker.out
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
