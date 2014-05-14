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
package com.anrisoftware.sscontrol.database.mysql.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.database.mysql.resource.ResourcesUtils

/**
 * MySQL/Ubuntu 12.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MysqlUbuntuResources {

    profile("UbuntuProfile.groovy", MysqlUbuntuResources.class.getResource("UbuntuProfile.groovy")),
    databaseScript("Database.groovy", MysqlUbuntuResources.class.getResource("Database.groovy")),
    databaseNonLoggingScript("Database.groovy", MysqlUbuntuResources.class.getResource("DatabaseNonLogging.groovy")),
    aptitudeCommand("/usr/bin/aptitude", MysqlUbuntuResources.class.getResource("echo_command.txt")),
    restartCommand("/sbin/restart", MysqlUbuntuResources.class.getResource("echo_command.txt")),
    mysqlCommand("/usr/bin/mysql", MysqlUbuntuResources.class.getResource("echo_command.txt")),
    mysqladminCommand("/usr/bin/mysqladmin", MysqlUbuntuResources.class.getResource("echo_command.txt")),
    mysqldExpected("/etc/mysql/conf.d/sscontrol_mysqld.cnf", MysqlUbuntuResources.class.getResource("mysqld_cnf_expected.txt")),
    mysqldNonLoggingExpected("/etc/mysql/conf.d/sscontrol_mysqld.cnf", MysqlUbuntuResources.class.getResource("mysqld_cnf_nonlogging_expected.txt")),
    confDir("/etc/mysql/conf.d", null),
    mysqlOut("/usr/bin/mysql.out", MysqlUbuntuResources.class.getResource("mysql_out_expected.txt")),
    restartOut("/sbin/restart.out", MysqlUbuntuResources.class.getResource("restart_out_expected.txt")),
    aptitudeOut("/usr/bin/aptitude.out", MysqlUbuntuResources.class.getResource("aptitude_out_expected.txt")),
    postfixtables("/tmp/postfixtables.sql", MysqlUbuntuResources.class.getResource("postfixtables.txt")),

    static copyMysqlFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        mysqlCommand.createCommand parent
        mysqladminCommand.createCommand parent
        confDir.asFile parent mkdirs()
    }

    ResourcesUtils resources

    MysqlUbuntuResources(String path, URL resource) {
        this.resources = new ResourcesUtils(path: path, resource: resource)
    }

    String getPath() {
        resources.path
    }

    URL getResource() {
        resources.resource
    }

    File asFile(File parent) {
        resources.asFile parent
    }

    void createFile(File parent) {
        resources.createFile parent
    }

    void createCommand(File parent) {
        resources.createCommand parent
    }

    String replaced(File parent, def search, def replace) {
        resources.replaced parent, search, replace
    }

    String toString() {
        resources.toString()
    }
}
