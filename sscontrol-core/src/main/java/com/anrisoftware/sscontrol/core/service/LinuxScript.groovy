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
package com.anrisoftware.sscontrol.core.service

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.core.api.Service
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorkerFactory
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenMarker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorker
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokensTemplateWorkerFactory

/**
 * Provides utilities methods for a Linux service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class LinuxScript extends Script {

	/**
	 * The name of the script.
	 */
	String name

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

	@Inject
	private LinuxScriptLogger log

	@Inject
	TemplatesFactory templatesFactory

	@Inject
	ScriptCommandWorkerFactory scriptCommandFactory

	@Inject
	TokensTemplateWorkerFactory tokensTemplateFactory

	Templates commandTemplates

	@Override
	def run() {
		commandTemplates = templatesFactory.create("ScriptCommandTemplates")
		installSystemPackages()
	}

	/**
	 * Installs the system packages.
	 */
	void installSystemPackages() {
		def packages = systemListProperty "system_packages", defaultProperties
		!packages.empty ? installPackages(packages) : null
	}

	/**
	 * Enables the specified repository.
	 *
	 * @param distribution
	 * 			  the name of the distribution.
	 *
	 * @param repository
	 * 			  the name of the repository to enable.
	 *
	 */
	void enableRepository(String distribution, String repository) {
		def template = commandTemplates.getResource("command")
		def command = enableRepositoryCommand distribution, repository
		def worker = scriptCommandFactory.create(template, "command", command)()
		log.enableRepositoryDone this, worker, distribution, repository
	}

	/**
	 * Returns the command to enable additional repositories.
	 *
	 * @param distribution
	 * 			  the name of the distribution.
	 *
	 * @param repository
	 * 			  the name of the repository to enable.
	 *
	 * <ul>
	 * <li>system property key {@code enable_repository_command}</li>
	 * </ul>
	 */
	String enableRepositoryCommand(String distribution, String repository) {
		systemProperty "enable_repository_command", defaultProperties, distribution, repository
	}

	/**
	 * Installs the specified packages.
	 *
	 * @param packages
	 * 			  the {@link List} of the package names to install.
	 */
	void installPackages(List packages) {
		def template = commandTemplates.getResource("install")
		def worker = scriptCommandFactory.create(template,
						"installCommand", installCommand,
						"packages", packages)()
		log.installPackagesDone this, worker, packages
	}

	/**
	 * Returns the install command.
	 *
	 * <ul>
	 * <li>system property key {@code install_command}</li>
	 * </ul>
	 */
	String getInstallCommand() {
		systemProperty "install_command", defaultProperties
	}

	/**
	 * Returns the configuration on the server.
	 */
	String currentConfiguration(File file) {
		if (file.isFile()) {
			FileUtils.readFileToString file, system.charset
		} else {
			log.noConfigurationFound this, file
			""
		}
	}

	/**
	 * Deploys the configuration to the configuration file.
	 */
	def deployConfiguration(TokenMarker tokenMarker, String currentConfiguration, List configurations, File file) {
		configurations = configurations.flatten()
		TokensTemplateWorker worker
		String configuration = currentConfiguration
		configurations.each {
			worker = tokensTemplateFactory.create(tokenMarker, it, configuration)()
			configuration = worker.getText()
		}
		FileUtils.write file, configuration, system.charset
		log.deployConfigurationDone this, file, configuration
	}



	/**
	 * Returns the configuration template tokens.
	 *
	 * @param comment
	 * 			  the string that marks the beginning of a comment in the
	 * 			  configuration. Defaults to '#'.
	 */
	TokenMarker configurationTokens(def comment = "#") {
		new TokenMarker("$comment SSCONTROL-$name", "$comment SSCONTROL-$name-END\n")
	}

	/**
	 * Restart the service.
	 *
	 * @param restartCommand
	 * 			  the restart command for the service.
	 */
	void restartService(String restartCommand) {
		def template = commandTemplates.getResource("command")
		def worker = scriptCommandFactory.create(template, "command", restartCommand)()
		log.restartServiceDone this, worker
	}

	/**
	 * Returns the default properties for the service.
	 */
	abstract def getDefaultProperties()

	/**
	 * Returns a system profile property. If the profile property was not set
	 * return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param defaultProperties
	 * 			  the {@link ContextProperties} containing the default
	 * 			  properties.
	 *
	 * @param args
	 * 			  optional the arguments to the key.
	 *
	 * @return the value of the system profile property or the default property
	 * if the profile property was not set.
	 */
	def systemProperty(String key, ContextProperties defaultProperties, Object... args) {
		def property = system.get(key, args)
		property != null ? property : String.format(defaultProperties.getProperty(key), args)
	}

	/**
	 * Returns a list of the system profile property. If the profile property
	 * was not set return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param defaultProperties
	 * 			  the {@link ContextProperties} containing the default
	 * 			  properties.
	 *
	 * @return the {@link List} of the system profile property or
	 * the default property if the profile property was not set.
	 */
	List systemListProperty(String key, ContextProperties defaultProperties) {
		def property = profile.getList(key)
		property.empty ? defaultProperties.getListProperty(key, ",") : property
	}

	/**
	 * Returns a profile property. If the profile property was not set
	 * return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param defaultProperties
	 * 			  the {@link ContextProperties} containing the default
	 * 			  properties.
	 *
	 * @return the value of the profile property or the default property
	 * if the profile property was not set.
	 */
	def profileProperty(String key, ContextProperties defaultProperties) {
		def property = profile[key]
		property != null ? property : defaultProperties.getProperty(key)
	}

	/**
	 * Returns a list profile property. If the profile property was not set
	 * return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param defaultProperties
	 * 			  the {@link ContextProperties} containing the default
	 * 			  properties.
	 *
	 * @return the {@link List} of the profile property or the default property
	 * if the profile property was not set.
	 */
	List profileListProperty(String key, ContextProperties defaultProperties) {
		def property = profile.getList(key)
		property.empty ? defaultProperties.getListProperty(key, ",") : property
	}

	/**
	 * Set properties of the script.
	 */
	@Override
	void setProperty(String property, Object newValue) {
		metaClass.setProperty(this, property, newValue)
	}

	@Override
	String toString() {
		"${service.toString()}: $name"
	}
}
