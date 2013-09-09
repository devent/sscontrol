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
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService
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

	TemplateResource phpldapadminConfigTemplate

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
		phpldapadminConfigTemplate = phpldapadminTemplates.getResource "config"
		phpldapadminCommandsTemplate = phpldapadminTemplates.getResource "commands"
	}

	@Override
	void deployService(Domain domain, WebService service, List serviceConfig) {
		fcgiConfig.script = script
		installPackages phpldapadminPackages
		fcgiConfig.enableFcgi()
		fcgiConfig.deployConfig domain
		downloadPhpldapadmin domain
		//deployConfiguration service
		//changeOwnerConfiguration domain
		//createDomainConfig domain, service, serviceConfig
	}

	void downloadPhpldapadmin(Domain domain) {
		def name = new File(phpmyadminSource.toURI()).name
		def target = new File(tmpDirectory, name)
		def extension = FilenameUtils.getExtension target.absolutePath
		def outputDir = phpmyadminConfigurationDir(domain)
		if (!phpmyadminConfigFile(domain).isFile()) {
			FileUtils.copyURLToFile phpmyadminSource, target
			unpack([file: target, type: extension, output: outputDir, command: tarCommand])
		}
	}

	void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
		def config = phpldapadminConfigTemplate.getText(true, "domainConfig",
				"domain", domain,
				"service", service,
				"properties", script,
				"fcgiProperties", fcgiConfig)
		serviceConfig << config
	}

	void importTables(PhpmyadminService service) {
		def worker = scriptCommandFactory.create(
				phpldapadminCommandsTemplate, "importTables",
				"zcatCommand", script.zcatCommand,
				"mysqlCommand", mysqlCommand,
				"admin", service.adminUser,
				"user", service.controlUser,
				"script", databaseScriptFile)()
		log.importTables script, worker
	}

	/**
	 * Deploys the phpldapadmin configuration.
	 */
	void deployConfiguration(PhpmyadminService service) {
		deployConfiguration configurationTokens(), phpldapadminConfiguration, phpldapadminConfigurations(service), configurationFile
	}

	/**
	 * Returns the phpmyadmin configurations.
	 */
	List phpmyadminConfigurations(PhpmyadminService service) {
		[
			configDbuser(service),
			configDbpassword(service),
			configDbserver(service),
			configDbport(service),
			configDbname(service),
			configDbadmin(service)
		]
	}

	def configDbuser(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbuser_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbuser", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbpassword(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbpassword_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbpassword", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbserver(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbserver_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbserver", "server", service.server)
		new TokenTemplate(search, replace)
	}

	def configDbport(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbport_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbport", "server", service.server)
		new TokenTemplate(search, replace)
	}

	def configDbname(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbname_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbname", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbadmin(PhpmyadminService service) {
		def search = phpldapadminConfigTemplate.getText(true, "configDbadmin_search")
		def replace = phpldapadminConfigTemplate.getText(true, "configDbadmin", "admin", service.adminUser)
		new TokenTemplate(search, replace)
	}

	def changeOwnerConfiguration(Domain domain) {
		def user = domain.domainUser
		script.changeOwner user: "root", group: user.group, files: [
			localBlowfishFile,
			localConfigFile,
			localDatabaseConfigFile
		]
	}

	def reconfigureService() {
		def worker = scriptCommandFactory.create(
				phpldapadminCommandsTemplate, "reconfigure",
				"command", reconfigureCommand)()
		log.reconfigureService script, worker
	}

	/**
	 * Phpldapadmin/configuration directory, for
	 * example {@code "/etc/phpldapadmin"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_configuration_directory"}</li>
	 * </ul>
	 */
	File phpmyadminConfigurationDir(def domain) {
		String path = profileProperty "phpldapadmin_configuration_directory"
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
	 */
	File phpmyadminConfigFile(Domain domain) {
		propertyFile("phpldapadmin_configuration_file", defaultProperties, phpmyadminConfigurationDir(domain)) as File
	}

	/**
	 * Phpldapadmin/source, for
	 * example {@code "http://downloads.sourceforge.net/project/phpldapadmin/phpldapadmin-php5/1.2.3/phpldapadmin-1.2.3.tgz"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpldapadmin_source"}</li>
	 * </ul>
	 */
	URL getPhpmyadminSource() {
		script.profileProperty("phpldapadmin_source") as URL
	}
}
