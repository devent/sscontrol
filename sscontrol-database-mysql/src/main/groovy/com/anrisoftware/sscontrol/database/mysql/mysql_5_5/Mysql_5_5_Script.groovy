/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.database.mysql.mysql_5_5

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils

import com.anrisoftware.globalpom.exec.api.ProcessTask
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.database.mysql.linux.MysqlScript
import com.anrisoftware.sscontrol.database.service.DatabaseService
import com.anrisoftware.sscontrol.database.statements.Database
import com.anrisoftware.sscontrol.database.statements.User
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>MySQL 5.5</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Mysql_5_5_Script extends MysqlScript {

    @Inject
    Mysql_5_5_ScriptLogger logg

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    TemplateResource mysqldConfTemplates

    TemplateResource adminPasswordTemplate

    TemplateResource createDatabasesTemplate

    TemplateResource createUsersTemplate

    TemplateResource importScriptTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create("Mysql_5_5")
        mysqldConfTemplates = templates.getResource("mysqld_configuration")
        adminPasswordTemplate = templates.getResource("admin_password")
        createDatabasesTemplate = templates.getResource("create_databases")
        createUsersTemplate = templates.getResource("create_users")
        importScriptTemplate = templates.getResource("import_script")
    }

    /**
     * Deploys the <i>mysqld</i> configuration.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void deployMysqldConfiguration(DatabaseService service) {
        def config = []
        config << mysqldConfTemplates.getText(true, "configHeader")
        config << mysqldConfTemplates.getText(true, "mysqldConfigHeader")
        config << mysqldConfTemplates.getText(true, "bindAddressConfig", "addresses", service.bindingAddresses)
        config << mysqldConfTemplates.getText(true, "bindPortConfig", "addresses", service.bindingAddresses)
        def levels = service.debugLogging("level")
        def files = service.debugLogging("file")
        if (levels["general"] > 0) {
            config << mysqldConfTemplates.getText(true, "generalLogConfig", "level", levels["general"])
            config << mysqldConfTemplates.getText(true, "generalLogFileConfig", "file", files["general"])
        }
        if (levels["error"] > 0) {
            if (StringUtils.isBlank(files["error"])) {
                config << mysqldConfTemplates.getText(true, "logErrorConfig", "level", levels["error"])
            } else {
                config << mysqldConfTemplates.getText(true, "logErrorFileConfig", "file", files["error"])
            }
        }
        if (levels["slow-queries"] > 0) {
            if (StringUtils.isBlank(files["slow-queries"])) {
                config << mysqldConfTemplates.getText(true, "logSlowQueriesConfig", "level", levels["slow-queries"])
            } else {
                config << mysqldConfTemplates.getText(true, "logSlowQueriesFileConfig", "file", files["slow-queries"])
            }
        }
        FileUtils.writeLines mysqldFile, charset.name(), config
        logg.mysqldConfigurationDeployed this, mysqldFile
    }

    /**
     * Restarts the <i>database</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log,
                runCommands: runCommands,
                command: restartCommand,
                services: restartServices,
                this, threads)()
    }

    /**
     * Sets the administrator password if none is set.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void setupAdministratorPassword(DatabaseService service) {
        if (service.adminPassword == null) {
            return
        }
        ProcessTask worker = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                mysqladminCommand: mysqladminCommand,
                password: service.adminPassword,
                checkExitCodes: false,
                this, threads,
                adminPasswordTemplate, "checkAdminPassword")()
        if (worker.exitValue != 0) {
            worker = scriptExecFactory.create(
                    log: log,
                    runCommands: runCommands,
                    mysqladminCommand: mysqladminCommand,
                    password: service.adminPassword,
                    this, threads,
                    adminPasswordTemplate, "setupAdminPassword")()
            logg.adminPasswordSet this, worker
        }
    }

    /**
     * Creates the databases from the service.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void createDatabases(DatabaseService service) {
        service.databases.each { Database db ->
            db.characterSet == null ? db.database(db.name, charset: defaultCharacterSet) : db.characterSet
            db.collate == null ? db.database(db.name, collate: defaultCollate) : db.collate
        }
        def worker = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                mysqlCommand: mysqlCommand,
                password: service.adminPassword,
                databases: service.databases,
                this, threads,
                createDatabasesTemplate, "createDatabases")()
        logg.databasesCreated this, worker
    }

    /**
     * Creates the users from the service.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void createUsers(DatabaseService service) {
        checkUsers service.users
        service.users.each { User user ->
            user.server == null ? user.user(user.name, server: defaultUserServer) : user.server
        }
        def worker = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                mysqlCommand: mysqlCommand,
                password: service.adminPassword,
                users: service.users,
                this, threads,
                createUsersTemplate, "createUsers")()
        logg.usersCreated this, worker
    }

    /**
     * Checks the database users.
     */
    void checkUsers(List users) {
        int maxuserLength = maxUserNameLength
        users.each { User user ->
            logg.checkUserNameLength this, user, maxuserLength
        }
    }

    /**
     * Execute database scripts.
     *
     * @param service
     *            the {@link DatabaseService} database service.
     */
    void importScripts(DatabaseService service) {
        service.databases.each { Database database ->
            database.scriptImportings.each { URI it ->
                importScript service, database, it
            }
        }
    }

    final void importScript(DatabaseService service, Database database, URI uri) {
        def unpackCommand = unpackCommand(uri.path)
        def file = new File(tmpDirectory, FilenameUtils.getBaseName(uri.path))
        def input = uri.toURL().openStream()
        IOUtils.copy input, new FileOutputStream(file)
        def worker = scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                mysqlCommand: mysqlCommand,
                password: service.adminPassword,
                database: database,
                file: file,
                unpackCommand: unpackCommand,
                archiveType: archiveType(uri.path),
                this, threads, importScriptTemplate, "importScript")()
        file.delete()
        logg.scriptExecuted this, worker, uri
    }

    /**
     * Returns maximum user name length, for example: {@code "16"}.
     *
     * <ul>
     * <li>profile property key {@code max_user_name_length}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMaxUserNameLength() {
        profileNumberProperty "max_user_name_length", defaultProperties
    }
}
