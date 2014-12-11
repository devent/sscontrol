/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.maradnsdeadwood.ubuntu_14_04

def aptitudeCommand = MaradnsDeadwoodResources.aptitudeCommand.asFile(tmp)
def deadwoodRestartCommand = MaradnsDeadwoodResources.deadwoodRestartCommand.asFile(tmp)
def maradnsRestartCommand = MaradnsDeadwoodResources.maradnsRestartCommand.asFile(tmp)
def groupAddCommand = MaradnsDeadwoodResources.groupAddCommand.asFile(tmp)
def userAddCommand = MaradnsDeadwoodResources.userAddCommand.asFile(tmp)
def userIdCommand = MaradnsDeadwoodResources.userIdCommand.asFile(tmp)
def chmodCommand = MaradnsDeadwoodResources.chmodCommand.asFile(tmp)
def chownCommand = MaradnsDeadwoodResources.chownCommand.asFile(tmp)
def updateRcCommand = MaradnsDeadwoodResources.updateRcCommand.asFile(tmp)
def deadwoodCommand = MaradnsDeadwoodResources.deadwoodCommand.asFile(tmp)
def groupsFile = MaradnsDeadwoodResources.groupsFile.asFile(tmp)
def usersFile = MaradnsDeadwoodResources.usersFile.asFile(tmp)
def deadwoodConfDir = MaradnsDeadwoodResources.deadwoodConfDir.asFile(tmp)
def maradnsConfDir = MaradnsDeadwoodResources.maradnsConfDir.asFile(tmp)
def sourcesListFile = MaradnsDeadwoodResources.sourcesListFile.asFile(tmp)
def scriptFile = MaradnsDeadwoodResources.scriptFile.asFile(tmp)

profile "ubuntu_14_04", {
    dns {
        service([
            "idzones": "maradns",
            "idrecursive": "deadwood"
        ])
        install_command aptitudeCommand
        deadwood_restart_command "$deadwoodRestartCommand recache"
        maradns_restart_command "$maradnsRestartCommand restart"
        group_add_command groupAddCommand
        user_add_command userAddCommand
        user_id_command userIdCommand
        chmod_command chmodCommand
        chown_command chownCommand
        update_rc_command updateRcCommand
        deadwood_command deadwoodCommand
        groups_file groupsFile
        users_file usersFile
        deadwood_configuration_directory deadwoodConfDir
        maradns_configuration_directory maradnsConfDir
        deadwood_configuration_file "dwood3rc"
        maradns_configuration_file "mararc"
        packages_sources_file sourcesListFile
        deadwood_script_file scriptFile
    }
}
