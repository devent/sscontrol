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

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.database.statements.Database
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * MySQL 5.1 service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Mysql51Script extends MysqlScript {

	@Inject
	Mysql51ScriptLogger log

	Templates mysqlTemplates

	TemplateResource mysqlConfiguration

	TemplateResource mysqlCommands

	@Override
	def run() {
		super.run()
		mysqlTemplates = templatesFactory.create("Mysql51")
		mysqlConfiguration = mysqlTemplates.getResource("configuration")
		mysqlCommands = mysqlTemplates.getResource("commands")
		deployMysqldConfiguration()
		restartServices()
		setupAdministratorPassword()
		createDatabases()
		createUsers()
		importScripts()
	}

	/**
	 * Deploys the mysqld/configuration.
	 */
	void deployMysqldConfiguration() {
		def replace = mysqlConfiguration.getText(true, "mysqld", "service", service)
		deployConfiguration configurationTokens(), currentMysqldConfiguration, [
			new TokenTemplate("(?s).*", replace)
		], mysqldFile
		log.mysqldConfigurationDeployed this
	}

	/**
	 * Returns the current {@code mysqld} configuration.
	 */
	String getCurrentMysqldConfiguration() {
		currentConfiguration mysqldFile
	}

	/**
	 * Sets the administrator password if none is set.
	 */
	void setupAdministratorPassword() {
		def worker = scriptCommandFactory.create(
				mysqlCommands, "checkAdminPassword",
				"mysqladminCommand", mysqladminCommand,
				"service", service)
		worker.skipExitValue = true
		worker()
		if (worker.exitCode != 0) {
			worker = scriptCommandFactory.create(
					mysqlCommands, "setupAdminPassword",
					"mysqladminCommand", mysqladminCommand,
					"service", service)()
			log.adminPasswordSet this, worker
		}
	}

	/**
	 * Creates the databases from the service.
	 */
	void createDatabases() {
		def worker = scriptCommandFactory.create(
				mysqlCommands, "createDatabases",
				"mysqlCommand", mysqlCommand,
				"service", service)()
		log.databasesCreated this, worker
	}

	/**
	 * Creates the users from the service.
	 */
	void createUsers() {
		def worker = scriptCommandFactory.create(
				mysqlCommands, "createUsers",
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
					def worker = scriptCommandFactory.create(
							mysqlCommands, "importScript",
							"mysqlCommand", mysqlCommand,
							"service", service,
							"script", it, "database", database)()
					log.importScript this, worker
				}
			}
		}
	}
}
