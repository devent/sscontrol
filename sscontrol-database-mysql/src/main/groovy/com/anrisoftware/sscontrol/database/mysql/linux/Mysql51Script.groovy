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

	TemplateResource mysqldConfTemplates

	TemplateResource adminPasswordTemplate

	TemplateResource createDatabasesTemplate

	TemplateResource createUsersTemplate

	TemplateResource importScriptTemplate

	@Override
	def run() {
		super.run()
		loadTemplate()
		deployMysqldConfiguration()
		restartServices()
		setupAdministratorPassword()
		createDatabases()
		createUsers()
		importScripts()
	}

	def loadTemplate() {
		mysqlTemplates = templatesFactory.create("Mysql51")
		mysqldConfTemplates = mysqlTemplates.getResource("mysqld_configuration")
		adminPasswordTemplate = mysqlTemplates.getResource("admin_password")
		createDatabasesTemplate = mysqlTemplates.getResource("create_databases")
		createUsersTemplate = mysqlTemplates.getResource("create_users")
		importScriptTemplate = mysqlTemplates.getResource("import_script")
	}

	/**
	 * Deploys the mysqld/configuration.
	 */
	void deployMysqldConfiguration() {
		def replace = mysqldConfTemplates.getText(true, "mysqldConfig", "script", this)
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
				adminPasswordTemplate, "checkAdminPassword", "script", this)
		worker.skipExitValue = true
		worker()
		if (worker.exitCode != 0) {
			worker = scriptCommandFactory.create(
					adminPasswordTemplate, "setupAdminPassword", "script", this)()
			log.adminPasswordSet this, worker
		}
	}

	/**
	 * Creates the databases from the service.
	 */
	void createDatabases() {
		def worker = scriptCommandFactory.create(
				createDatabasesTemplate, "createDatabases", "script", this)()
		log.databasesCreated this, worker
	}

	/**
	 * Creates the users from the service.
	 */
	void createUsers() {
		def worker = scriptCommandFactory.create(
				createUsersTemplate, "createUsers", "script", this)()
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
							importScriptTemplate, "importScript",
							"script", this, "database", database, "string", it)()
					log.importScript this, worker
				}
			}
		}
	}
}
