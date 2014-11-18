/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04

def aptitudeCommand = DeadwoodResources.aptitudeCommand.asFile(tmp)
def restartCommand = DeadwoodResources.restartCommand.asFile(tmp)
def groupAddCommand = DeadwoodResources.groupAddCommand.asFile(tmp)
def userAddCommand = DeadwoodResources.userAddCommand.asFile(tmp)
def userIdCommand = DeadwoodResources.userIdCommand.asFile(tmp)
def chmodCommand = DeadwoodResources.chmodCommand.asFile(tmp)
def chownCommand = DeadwoodResources.chownCommand.asFile(tmp)
def updateRcCommand = DeadwoodResources.updateRcCommand.asFile(tmp)
def deadwoodCommand = DeadwoodResources.deadwoodCommand.asFile(tmp)
def duendeCommand = DeadwoodResources.duendeCommand.asFile(tmp)
def groupsFile = DeadwoodResources.groupsFile.asFile(tmp)
def usersFile = DeadwoodResources.usersFile.asFile(tmp)
def confDir = DeadwoodResources.confDir.asFile(tmp)
def sourcesListFile = DeadwoodResources.sourcesListFile.asFile(tmp)
def scriptFile = DeadwoodResources.scriptFile.asFile(tmp)

profile "ubuntu_14_04", {
    dns {
        service "deadwood"
        install_command "$aptitudeCommand update && $aptitudeCommand install"
        restart_command "$restartCommand restart"
        group_add_command groupAddCommand
        user_add_command userAddCommand
        user_id_command userIdCommand
        chmod_command chmodCommand
        chown_command chownCommand
        update_rc_command updateRcCommand
        deadwood_command deadwoodCommand
        duende_command duendeCommand
        groups_file groupsFile
        users_file usersFile
        configuration_directory confDir
        packages_sources_file sourcesListFile
        deadwood_script_file scriptFile
    }
}
