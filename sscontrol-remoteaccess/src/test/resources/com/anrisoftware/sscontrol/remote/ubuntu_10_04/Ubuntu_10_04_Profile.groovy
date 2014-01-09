/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.ubuntu_10_04

def sshkeygen = Ubuntu_10_04_Resources.sshkeygenCommand.asFile(tmp)
def groupfile = Ubuntu_10_04_Resources.groupsFile.asFile(tmp)
def passwdfile = Ubuntu_10_04_Resources.passwdFile.asFile(tmp)
def sshdconfigFile = Ubuntu_10_04_Resources.sshdconfigFile.asFile(tmp)

profile "ubuntu_10_04", {
    remote {
        service "openssh"
        key_gen_command sshkeygen
        sshd_configuration_directory sshdconfigFile.parentFile
        groups_file groupfile
        users_file passwdfile
        home_pattern "$tmp/home/<user.name>"
        ssh_key_pattern "$tmp/home/<user.name>/.ssh/id_rsa"
    }
}
