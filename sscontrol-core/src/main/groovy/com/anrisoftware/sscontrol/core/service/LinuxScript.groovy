/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service

import static org.apache.commons.lang3.StringUtils.*

import java.nio.charset.Charset
import java.text.Format

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
		def packages = systemPackages
		!packages.empty ? installPackages(packages) : null
	}

	/**
	 * Returns the system packages to install.
	 *
	 * <ul>
	 * <li>profile property {@code system_packages}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	List getSystemPackages() {
		profileListProperty "system_packages", defaultProperties
	}

	/**
	 * Enables the specified repository.
	 *
	 * @param repository
	 * 			  the repository.
	 */
	void enableRepository(String repository) {
		def template = commandTemplates.getResource("command")
		def command = "$enableRepositoryCommand $repository"
		def worker = scriptCommandFactory.create(template, "command", command)()
		log.enableRepositoryDone this, worker, repository
	}

	/**
	 * Enables the specified Debian repositories.
	 *
	 * @param distribution
	 * 			  the name of the distribution.
	 *
	 * @param repositories
	 * 			  the list with the repositories.
	 */
	void enableDebRepositories(String distribution, List repositories) {
		repositories.each {
			if (!containsDebRepository(distribution, it)) {
				enableRepository debRepository(distribution, it)
			}
		}
	}

	/**
	 * Tests if the system already have the repository enabled.
	 *
	 * @param distribution
	 * 			  the name of the distribution.
	 *
	 * @param repository
	 * 			  the name of the repository.
	 *
	 * @return {@code true} if the repository is already enabled.
	 */
	boolean containsDebRepository(String distribution, String repository) {
		def template = commandTemplates.getResource("list_repositories")
		def worker = scriptCommandFactory.create(
				template, packagingType, "configurationDir", packagingConfigurationDir)()
		split(worker.out, '\n').find { it.endsWith "$distribution $repository" } != null
	}

	/**
	 * Returns the packaging type. The packaging type is the packaging system
	 * used on the system, like apt or yum.
	 *
	 * <ul>
	 * <li>property key {@code packaging_type}
	 * </ul>
	 */
	String getPackagingType() {
		profileProperty "packaging_type", defaultProperties
	}

	/**
	 * Returns the configuration directory of the packaging system for the
	 * system.
	 *
	 * <ul>
	 * <li>property key {@code packaging_configuration_directory}
	 * </ul>
	 */
	File getPackagingConfigurationDir() {
		profileProperty("packaging_configuration_directory", defaultProperties) as File
	}

	/**
	 * Returns the command to enable additional repositories.
	 *
	 * <ul>
	 * <li>property key {@code enable_repository_command}
	 * </ul>
	 */
	String getEnableRepositoryCommand() {
		profileProperty "enable_repository_command", defaultProperties
	}

	/**
	 * Returns the repository string for a Debian repository.
	 * Example for the Ubuntu Linux distribution:
	 * {@code "deb http://archive.ubuntu.com/ubuntu lucid universe"}.
	 *
	 * @param distribution
	 * 			  the name of the distribution.
	 *
	 * @param repository
	 * 			  the name of the repository.
	 *
	 * <ul>
	 * <li>property key {@code repository_string}
	 * </ul>
	 */
	String debRepository(String distribution, String repository) {
		profileProperty "repository_string", defaultProperties, distribution, repository
	}

	/**
	 * Installs the specified packages.
	 *
	 * @param packages
	 * 			  optionally, the {@link List} of the package names to install.
	 *
	 * @see #getPackages()
	 */
	void installPackages(List packages = packages) {
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
	 * <li>property key {@code install_command}</li>
	 * </ul>
	 */
	String getInstallCommand() {
		profileProperty "install_command", defaultProperties
	}

	/**
	 * Returns the configuration on the server.
	 */
	String currentConfiguration(File file) {
		if (file.isFile()) {
			FileUtils.readFileToString file, charset
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
		FileUtils.write file, configuration, charset
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
	 * Restart the services.
	 *
	 * @param services
	 * 			  optionally, a {@link List} of services to restart.
	 *
	 * @see #getRestartCommand()
	 * @see #getRestartServices()
	 */
	void restartServices(List services = restartServices) {
		def template = commandTemplates.getResource("restart")
		def worker = scriptCommandFactory.create(template,
				"restartCommand", restartCommand,
				"services", services)()
		log.restartServiceDone this, worker
	}

	/**
	 * Returns the default properties for the service, as in example:
	 *
	 * <pre>
	 * 	&#64;Inject
	 *	&#64;Named("my-properties")
	 *	ContextProperties myProperties
	 *
	 *	&#64;Override
	 *	def getDefaultProperties() {
	 *		myProperties
	 *	}
	 * </pre>
	 */
	abstract def getDefaultProperties()

	/**
	 * Returns the default character set.
	 *
	 * <ul>
	 * <li>property key {@code charset}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	Charset getCharset() {
		Charset.forName profileProperty("charset", defaultProperties)
	}

	/**
	 * Returns the restart command for the postfix service.
	 *
	 * <ul>
	 * <li>property key {@code restart_command}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getRestartCommand() {
		profileProperty "restart_command", defaultProperties
	}

	/**
	 * Returns the services to restart.
	 *
	 * <ul>
	 * <li>profile property {@code restart_services}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	List getRestartServices() {
		profileListProperty "restart_services", defaultProperties
	}

	/**
	 * Returns the service packages.
	 *
	 * <ul>
	 * <li>profile property {@code "packages"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	List getPackages() {
		profileListProperty "packages", defaultProperties
	}

	/**
	 * Returns the path of the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "configuration_directory"}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	File getConfigurationDir() {
		profileProperty("configuration_directory", defaultProperties) as File
	}

	/**
	 * Returns the change owner command.
	 *
	 * <ul>
	 * <li>property key {@code chown_command}</li>
	 * </ul>
	 *
	 * @see #getDefaultProperties()
	 */
	String getChownCommand() {
		profileProperty "chown_command", defaultProperties
	}

	/**
	 * Returns a profile property. If the profile property was not set
	 * return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param p
	 * 			  the {@link ContextProperties} containing the property values.
	 *
	 * @return the value of the profile property or the default property
	 * if the profile property was not set.
	 */
	def profileProperty(String key, ContextProperties p) {
		def property = profile[key]
		property != null ? property : p.getProperty(key)
	}

	private propertyKey(String key, ContextProperties p) {
		def property = p.getProperty(key)
		log.checkPropertyKey this, property, key
		return property
	}

	/**
	 * Returns a list profile property. If the profile property was not set
	 * return the default value from the default properties.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param p
	 * 			  the {@link ContextProperties} containing the property values.
	 *
	 * @return the {@link List} of the profile property or the default property
	 * if the profile property was not set.
	 */
	List profileListProperty(String key, ContextProperties p) {
		log.checkProperties this, p, key
		def property = profile.getList(key)
		property.empty ? p.getListProperty(key, ",") : property
	}

	/**
	 * Returns a list profile property. If the profile property was not set
	 * return the default value from the default properties. The specified
	 * format is used to create the list items.
	 *
	 * @param key
	 * 			  the key of the profile property.
	 *
	 * @param p
	 * 			  the {@link ContextProperties} containing the property values.
	 *
	 * @param format
	 * 		  	  the {@link Format} that is used to parse the string
	 * 			  properties and create the list items.
	 *
	 * @return the {@link List} of the profile property or the default property
	 * if the profile property was not set.
	 */
	List profileTypedListProperty(String key, ContextProperties p, Format format) {
		def property = profile.getList(key)
		property.empty ? p.getTypedListProperty(key, format, ",") : asTypedList(property, format)
	}

	List asTypedList(List<String> strings, Format format) {
		List list = new ArrayList()
		strings.each {
			list << format.parseObject(it)
		}
		return list
	}

	/**
	 * Set properties of the script.
	 */
	@Override
	void setProperty(String property, Object newValue) {
		metaClass.setProperty(this, property, newValue)
	}

	/**
	 * Runs the specified script in the context of the current script.
	 */
	void runScript(LinuxScript script) {
		def that = this
		script.name = name
		script.system = system
		script.profile = profile
		script.service = service
		script.metaClass.propertyMissing = { name -> that."$name" }
		script.metaClass.propertyMissing = { name, value -> that."$name($value)" }
		script.run()
	}

	@Override
	String toString() {
		"${service.toString()}: $name"
	}
}
