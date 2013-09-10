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
package com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.Ubuntu10_04ScriptFactory.PROFILE

import java.util.regex.Pattern

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.linux.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.linux.BasePhpldapadminConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.FcgiConfig
import com.anrisoftware.sscontrol.httpd.apache.linux.ServiceConfig
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.phpldapadmin.PhpldapadminService
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures the phpldapadmin/service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PhpldapadminConfig extends BasePhpldapadminConfig implements ServiceConfig {

	public static final String NAME = "phpldapadmin"

	@Inject
	PhpldapadminConfigLogger log

	Templates phpldapadminTemplates

	TemplateResource adminConfigTemplate

	TemplateResource phpldapadminCommandsTemplate

	@Inject
	FcgiConfig fcgiConfig

	@Override
	String getProfile() {
		PROFILE
	}

	@Override
	String getServiceName() {
		NAME
	}

	@Override
	void setScript(ApacheScript script) {
		super.setScript script
		phpldapadminTemplates = templatesFactory.create "Apache_2_2_Phpldapadmin"
		adminConfigTemplate = phpldapadminTemplates.getResource "config"
		phpldapadminCommandsTemplate = phpldapadminTemplates.getResource "commands"
	}

	@Override
	void deployService(Domain domain, WebService service, List serviceConfig) {
		fcgiConfig.script = script
		installPackages adminPackages
		fcgiConfig.enableFcgi()
		fcgiConfig.deployConfig domain
		downloadPhpldapadmin domain
		deployConfiguration domain, service
		deployServerConfig domain, service
		//changeOwnerConfiguration domain
		//createDomainConfig domain, service, serviceConfig
	}

	void downloadPhpldapadmin(Domain domain) {
		def name = new File(adminSource.toURI()).name
		def target = new File(tmpDirectory, name)
		def extension = FilenameUtils.getExtension target.absolutePath
		def outputDir = adminConfigurationDir(domain)
		def linkTarget = adminLinkedConfigurationDir(domain)
		FileUtils.copyURLToFile adminSource, target
		unpack([file: target, type: extension, output: outputDir, command: tarCommand, override: true])
		linkTarget.delete()
		link([files: outputDir, targets: linkTarget])
	}

	void createDomainConfig(Domain domain, PhpldapadminService service, List serviceConfig) {
		def config = adminConfigTemplate.getText(true, "domainConfig",
				"domain", domain,
				"service", service,
				"properties", script,
				"fcgiProperties", fcgiConfig)
		serviceConfig << config
	}

	/**
	 * Deploys the phpldapadmin/configuration.
	 */
	void deployConfiguration(Domain domain, PhpldapadminService service) {
		def configFile = adminConfigFile(domain)
		if (!configFile.isFile()) {
			FileUtils.copyFile adminExampleConfig(domain), configFile
		}
		def configs = adminConfigurations(domain, service)
		script.deployConfiguration configurationTokens("//"), adminConfiguration(domain), configs, configFile
	}

	/**
	 * Returns the phpldapadmin/configurations.
	 */
	List adminConfigurations(Domain domain, PhpldapadminService service) {
		[
			commentFirstNewServer(),
			commentFirstNewServerName(),
			addIncludeServers(domain),
		]
	}

	def commentFirstNewServer() {
		def search = adminConfigTemplate.getText(true, "firstNewServer_search")
		def replace = adminConfigTemplate.getText(true, "firstNewServer")
		def token = new TokenTemplate(search, replace)
		token.append = false
		token
	}

	def commentFirstNewServerName() {
		def search = adminConfigTemplate.getText(true, "firstNewServerName_search")
		def replace = adminConfigTemplate.getText(true, "firstNewServerName")
		def token = new TokenTemplate(search, replace)
		token.append = false
		token
	}

	def addIncludeServers(def domain) {
		def file = adminServersConfigFile(domain)
		def search = adminConfigTemplate.getText(true, "includeServersConfig_search", "file", Pattern.quote(file.name))
		def replace = adminConfigTemplate.getText(true, "includeServersConfig", "file", file.name)
		def token = new TokenTemplate(search, replace)
		token.append = false
		token.enclose = false
		token
	}

	/**
	 * Deploy the servers configuration.
	 */
	def deployServerConfig(Domain domain, PhpldapadminService service) {
		def string = adminConfigTemplate.getText true, "serversConfig", "service", service
		def file = adminServersConfigFile(domain)
		FileUtils.write file, string
		log.serverConfigdeployed script, file
	}

	/**
	 * Phpldapadmin/configuration directory, for
	 * example {@code "/etc/%s/phpldampadmin-1.2.3"}. The first argument
	 * is replaced with the domain directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_directory"}</li>
	 * </ul>
	 */
	File adminConfigurationDir(def domain) {
		String path = profileProperty "phpldapadmin_configuration_directory"
		new File(String.format(path, domainDir(domain)))
	}

	/**
	 * The linked Phpldapadmin/configuration directory, for
	 * example {@code "/etc/%s/phpldampadmin"}. The first argument
	 * is replaced with the domain directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_linked_configuration_directory"}</li>
	 * </ul>
	 */
	File adminLinkedConfigurationDir(def domain) {
		String path = profileProperty "phpldapadmin_linked_configuration_directory"
		new File(String.format(path, domainDir(domain)))
	}

	/**
	 * Phpldapadmin/configuration file, for
	 * example {@code "config/config.php"}. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_file"}</li>
	 * </ul>
	 *
	 * @see #adminLinkedConfigurationDir(Object)
	 */
	File adminConfigFile(Domain domain) {
		propertyFile("phpldapadmin_configuration_file", defaultProperties, adminLinkedConfigurationDir(domain)) as File
	}

	/**
	 * Phpldapadmin/server configuration file, for
	 * example {@code "config/robobee-servers.php"}. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_servers_configuration_file"}</li>
	 * </ul>
	 *
	 * @see #adminLinkedConfigurationDir(Object)
	 */
	File adminServersConfigFile(Domain domain) {
		propertyFile("phpldapadmin_servers_configuration_file", defaultProperties, adminLinkedConfigurationDir(domain)) as File
	}

	/**
	 * Example phpldapadmin/configuration file, for
	 * example {@code "config/config.php.example"}. If the path is
	 * not absolute then it is assume to be under the configuration directory.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_example_configuration_file"}</li>
	 * </ul>
	 *
	 * @see #adminLinkedConfigurationDir(Object)
	 */
	File adminExampleConfig(Domain domain) {
		propertyFile("phpldapadmin_example_configuration_file", defaultProperties, adminLinkedConfigurationDir(domain)) as File
	}

	/**
	 * Returns the current phpldapadmin/configuration.
	 */
	String adminConfiguration(Domain domain) {
		currentConfiguration adminConfigFile(domain)
	}

	/**
	 * Phpldapadmin/source, for
	 * example {@code "http://downloads.sourceforge.net/project/phpldapadmin/phpldapadmin-php5/1.2.3/phpldapadmin-1.2.3.tgz"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_source"}</li>
	 * </ul>
	 */
	URL getAdminSource() {
		script.profileProperty("phpldapadmin_source") as URL
	}
}
