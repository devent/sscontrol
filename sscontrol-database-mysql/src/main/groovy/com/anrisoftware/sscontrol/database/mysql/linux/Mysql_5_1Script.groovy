/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database-mysql.
 *
 * sscontrol-database-mysql is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database-mysql is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database-mysql. If not, see <http://www.gnu.org/licenses/>.
 */


package com.anrisoftware.sscontrol.database.mysql.linux

import static org.apache.commons.io.FileUtils.*

import java.util.regex.Pattern

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.database.statements.Database
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Script to configure MySQL 5.1.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Mysql_5_1Script extends LinuxScript {

	@Inject
	Mysql_5_1ScriptLogger log

	Templates mysqlTemplates

	TemplateResource mysqlConfiguration

	TemplateResource zoneConfiguration

	@Override
	def run() {
		super.run()
		mysqlTemplates = templatesFactory.create("Mysql_5_1")
		mysqlConfiguration = mysqlTemplates.getResource("configuration")
		service.setDefaultCharacterSet defaultCharacterSet
		service.setDefaultCollate defaultCollate
		deployMysqldConfiguration()
		restartServices()
		setupAdministratorPassword()
		createDatabases()
		createUsers()
		importScripts()
	}

	/**
	 * Deploys the mysqld configuration.
	 */
	void deployMysqldConfiguration() {
		deployConfiguration configurationTokens(), currentMysqldConfiguration, mysqldConfiguration, mysqldFile
	}

	/**
	 * Returns the mysqld configuration.
	 */
	List getMysqldConfiguration() {
		def replace = mysqlConfiguration.getText(true, "mysqld", "service", service)
		[
			new TokenTemplate(".*", replace, Pattern.DOTALL)
		]
	}

	/**
	 * Sets the administrator password if none is set.
	 */
	void setupAdministratorPassword() {
		def worker = scriptCommandFactory.create(
				mysqlConfiguration, "checkadminpassword",
				"mysqladminCommand", mysqladminCommand,
				"service", service)
		worker.skipExitValue = true
		worker()
		if (worker.exitCode != 0) {
			worker = scriptCommandFactory.create(mysqlConfiguration, "setupadminpassword",
					"mysqladminCommand", mysqladminCommand,
					"service", service)()
			log.adminPasswordSet this, worker
		}
	}

	/**
	 * Creates the databases from the service.
	 */
	void createDatabases() {
		def worker = scriptCommandFactory.create(mysqlConfiguration, "createDatabases",
				"mysqlCommand", mysqlCommand,
				"service", service)()
		log.databasesCreated this, worker
	}

	/**
	 * Creates the users from the service.
	 */
	void createUsers() {
		def worker = scriptCommandFactory.create(mysqlConfiguration, "createUsers",
				"mysqlCommand", mysqlCommand,
				"service", service)()
		log.usersCreated this, worker
	}

	/**
	 * Imports SQL scripts in the databases.
	 */
	void importScripts() {
		def handler = log.errorHandler(this)
		service.databases.each { Database database ->
			database.importScripts(handler).each {
				if (it != null) {
					def worker = scriptCommandFactory.create(mysqlConfiguration, "importScript",
							"mysqlCommand", mysqlCommand,
							"service", service,
							"script", it, "database", database)()
					log.importScript this, worker
				} else {
				}
			}
		}
	}

	/**
	 * Returns the default character set for databases.
	 * <p>
	 * Example: {@code utf-8}
	 *
	 * <ul>
	 * <li>profile property key {@code default_character_set}</li>
	 * </ul>
	 */
	abstract String getDefaultCharacterSet()

	/**
	 * Returns the default collate for databases.
	 * <p>
	 * Example: {@code utf8_general_ci}
	 *
	 * <ul>
	 * <li>profile property key {@code default_collate}</li>
	 * </ul>
	 */
	abstract String getDefaultCollate()

	/**
	 * Returns the mysqladmin command.
	 * <p>
	 * Example: {@code /usr/bin/mysqladmin}
	 *
	 * <ul>
	 * <li>profile property key {@code mysqladmin_command}</li>
	 * </ul>
	 */
	abstract String getMysqladminCommand()

	/**
	 * Returns the mysql command.
	 * <p>
	 * Example: {@code /usr/bin/mysql}
	 *
	 * <ul>
	 * <li>profile property key {@code mysql_command}</li>
	 * </ul>
	 */
	abstract String getMysqlCommand()

	/**
	 * Returns the file of the {@code mysqld} configuration file.
	 * <p>
	 * Example: {@code sscontrol_mysqld.cnf}
	 *
	 * <ul>
	 * <li>profile property key {@code mysqld_configuration_file}</li>
	 * </ul>
	 */
	abstract File getMysqldFile()

	/**
	 * Returns the current {@code mysqld} configuration.
	 */
	String getCurrentMysqldConfiguration() {
		currentConfiguration mysqldFile
	}
}
