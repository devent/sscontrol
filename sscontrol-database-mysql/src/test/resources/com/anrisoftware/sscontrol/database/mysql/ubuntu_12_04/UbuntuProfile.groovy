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

def aptitudeCommand = MysqlUbuntuResources.aptitudeCommand.asFile(tmp)
def restartCommand = MysqlUbuntuResources.restartCommand.asFile(tmp)
def mysqladminCommand = MysqlUbuntuResources.mysqladminCommand.asFile(tmp)
def mysqlCommand = MysqlUbuntuResources.mysqlCommand.asFile(tmp)
def confDir = MysqlUbuntuResources.confDir.asFile(tmp)

profile "ubuntu_12_04", {
    database {
        service "mysql"
        install_command "$aptitudeCommand update && $aptitudeCommand install"
        restart_command restartCommand
        packages "mysql-server, mysql-client"
        configuration_directory confDir
        mysqladmin_command mysqladminCommand
        mysql_command mysqlCommand
    }
}
