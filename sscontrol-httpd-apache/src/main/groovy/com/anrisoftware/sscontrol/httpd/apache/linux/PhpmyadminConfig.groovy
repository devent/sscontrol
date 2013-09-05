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
package com.anrisoftware.sscontrol.httpd.apache.linux

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures the Phpmyadmin service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PhpmyadminConfig {

	@Inject
	PhpmyadminConfigLogger log

	Templates phpmyadminTemplates

	TemplateResource phpmyadminConfigTemplate

	TemplateResource phpmyadminCommandsTemplate

	ApacheScript script

	@Inject
	FcgiConfig fcgiConfig

	void setScript(ApacheScript script) {
		this.script = script
		phpmyadminTemplates = templatesFactory.create "Apache_2_2_Phpmyadmin"
		phpmyadminConfigTemplate = phpmyadminTemplates.getResource "config"
		phpmyadminCommandsTemplate = phpmyadminTemplates.getResource "commands"
	}

	def deployService(Domain domain, PhpmyadminService service, List serviceConfig) {
		fcgiConfig.script = script
		installPackages phpmyadminPackages
		fcgiConfig.enableFcgi()
		fcgiConfig.deployConfig domain
		deployConfiguration service
		reconfigureService()
		importTables service
		createDomainConfig domain, service, serviceConfig
	}

	void createDomainConfig(Domain domain, PhpmyadminService service, List serviceConfig) {
		def config = phpmyadminConfigTemplate.getText(true, "domainConfig",
				"domain", domain,
				"service", service,
				"properties", script,
				"fcgiProperties", fcgiConfig)
		serviceConfig << config
	}

	void importTables(PhpmyadminService service) {
		def worker = scriptCommandFactory.create(
				phpmyadminCommandsTemplate, "importTables",
				"zcatCommand", script.zcatCommand,
				"mysqlCommand", mysqlCommand,
				"admin", service.adminUser,
				"user", service.controlUser,
				"script", databaseScriptFile)()
		log.importTables script, worker
	}

	/**
	 * Deploys the phpmyadmin configuration.
	 */
	void deployConfiguration(PhpmyadminService service) {
		deployConfiguration configurationTokens(), phpmyadminConfiguration, phpmyadminConfigurations(service), configurationFile
	}

	/**
	 * Returns the current {@code phpmyadmin.conf} configuration.
	 */
	String getPhpmyadminConfiguration() {
		currentConfiguration configurationFile
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
		def search = phpmyadminConfigTemplate.getText(true, "configDbuser_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbuser", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbpassword(PhpmyadminService service) {
		def search = phpmyadminConfigTemplate.getText(true, "configDbpassword_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbpassword", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbserver(PhpmyadminService service) {
		def search = phpmyadminConfigTemplate.getText(true, "configDbserver_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbserver", "server", service.server)
		new TokenTemplate(search, replace)
	}

	def configDbport(PhpmyadminService service) {
		def search = phpmyadminConfigTemplate.getText(true, "configDbport_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbport", "server", service.server)
		new TokenTemplate(search, replace)
	}

	def configDbname(PhpmyadminService service) {
		def search = phpmyadminConfigTemplate.getText(true, "configDbname_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbname", "user", service.controlUser)
		new TokenTemplate(search, replace)
	}

	def configDbadmin(PhpmyadminService service) {
		def search = phpmyadminConfigTemplate.getText(true, "configDbadmin_search")
		def replace = phpmyadminConfigTemplate.getText(true, "configDbadmin", "admin", service.adminUser)
		new TokenTemplate(search, replace)
	}

	def reconfigureService() {
		def worker = scriptCommandFactory.create(
				phpmyadminCommandsTemplate, "reconfigure",
				"command", reconfigureCommand)()
		log.reconfigureService script, worker
	}

	/**
	 * Returns the list of needed packages for phpmyadmin.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_packages"}</li>
	 * </ul>
	 */
	List getPhpmyadminPackages() {
		profileListProperty "phpmyadmin_packages", defaultProperties
	}

	/**
	 * Returns the mysql client command, for example {@code /usr/bin/mysql}.
	 *
	 * <ul>
	 * <li>profile property {@code "mysql_command"}</li>
	 * </ul>
	 */
	String getMysqlCommand() {
		profileProperty "mysql_command", defaultProperties
	}

	/**
	 * Phpmyadmin configuration file, for
	 * example {@code "/etc/dbconfig-common/phpmyadmin.conf"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_configuration_file"}</li>
	 * </ul>
	 */
	File getConfigurationFile() {
		profileProperty("phpmyadmin_configuration_file", defaultProperties) as File
	}

	/**
	 * Phpmyadmin database script file, for
	 * example {@code "/usr/share/doc/phpmyadmin/examples/create_tables.sql.gz"}.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_database_script_file"}</li>
	 * </ul>
	 */
	File getDatabaseScriptFile() {
		profileProperty("phpmyadmin_database_script_file", defaultProperties) as File
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
