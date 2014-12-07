/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.importdb

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

/**
 * <i>Ubuntu 10.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_10_04_Resources {

    mysqlEmptyCommand("/usr/bin/mysql", Ubuntu_10_04_Resources.class.getResource("mysql_empty_command.txt")),
    mysqlNonEmptyCommand("/usr/bin/mysql", Ubuntu_10_04_Resources.class.getResource("mysql_nonempty_command.txt")),
    mysqlEmptyOutExpected("/usr/bin/mysql.out", Ubuntu_10_04_Resources.class.getResource("mysql_empty_out_expected.txt")),
    mysqlNonEmptyOutExpected("/usr/bin/mysql.out", Ubuntu_10_04_Resources.class.getResource("mysql_nonempty_out_expected.txt")),
    scriptMysql("/tmp/script_mysql", Ubuntu_10_04_Resources.class.getResource("script_mysql.txt")),

    static void copyUbuntu_10_04_Files(File parent) {
        mysqlEmptyCommand.createCommand parent
        mysqlNonEmptyCommand.createCommand parent
        scriptMysql.createFile parent
    }

    ResourcesUtils resources

    Ubuntu_10_04_Resources(String path, URL resource) {
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
