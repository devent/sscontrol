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
package com.anrisoftware.sscontrol.database.mysql.mysql_5_1

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils

import com.anrisoftware.globalpom.exec.api.ProcessTask
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.database.mysql.linux.MysqlScript
import com.anrisoftware.sscontrol.database.statements.Database
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * MySQL 5.1 service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Mysql51Script extends MysqlScript {

    @Inject
    Mysql51ScriptLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    RestartServicesFactory restartServicesFactory

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
        restartService()
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
        logg.mysqldConfigurationDeployed this
    }

    /**
     * Restarts the <i>database</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log, command: restartCommand, services: restartServices, this, threads)()
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
        ProcessTask worker = scriptExecFactory.create(
                log: log, mysqladminCommand: mysqladminCommand, password: service.admin.password,
                checkExitCodes: false, this, threads, adminPasswordTemplate, "checkAdminPassword")()
        if (worker.exitValue != 0) {
            worker = scriptExecFactory.create(
                    log: log, mysqladminCommand: mysqladminCommand, password: service.admin.password,
                    this, threads, adminPasswordTemplate, "setupAdminPassword")()
            logg.adminPasswordSet this, worker
        }
    }

    /**
     * Creates the databases from the service.
     */
    void createDatabases() {
        def worker = scriptExecFactory.create(
                log: log, mysqlCommand: mysqlCommand, password: service.admin.password,
                databases: service.databases, defaultCharacterSet: defaultCharacterSet, defaultCollate: defaultCollate,
                this, threads, createDatabasesTemplate, "createDatabases")()
        logg.databasesCreated this, worker
    }

    /**
     * Creates the users from the service.
     */
    void createUsers() {
        def worker = scriptExecFactory.create(
                log: log, mysqlCommand: mysqlCommand, password: service.admin.password,
                users: service.users, defaultUserServer: defaultUserServer,
                this, threads, createUsersTemplate, "createUsers")()
        logg.usersCreated this, worker
    }

    /**
     * Execute database scripts.
     */
    void importScripts() {
        service.databases.each { Database database ->
            database.importingScripts.each { URI it ->
                def unpackCommand = unpackCommand(it.path)
                def file = new File(tmpDirectory, FilenameUtils.getBaseName(it.path))
                def input = it.toURL().openStream()
                IOUtils.copy input, new FileOutputStream(file)
                def worker = scriptExecFactory.create(
                        log: log,
                        mysqlCommand: mysqlCommand,
                        password: service.admin.password,
                        database: database,
                        file: file,
                        unpackCommand: unpackCommand,
                        archiveType: archiveType(it.path),
                        this, threads, importScriptTemplate, "importScript")()
                file.delete()
                logg.scriptExecuted this, worker
            }
        }
    }
}
