/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

def aptitude = UbuntuResources.aptitudeCommand.asFile(tmp)
def useradd = UbuntuResources.useraddCommand.asFile(tmp)
def groupadd = UbuntuResources.groupaddCommand.asFile(tmp)
def passwd = UbuntuResources.passwdCommand.asFile(tmp)
def sshkeygen = UbuntuResources.sshkeygenCommand.asFile(tmp)
def groupfile = UbuntuResources.groupsFile.asFile(tmp)
def passwdfile = UbuntuResources.passwdFile.asFile(tmp)
def sshdconfigFile = UbuntuResources.sshdconfigFile.asFile(tmp)

profile "ubuntu_10_04", {
    remote {
        service "openssh"
        install_command "export DEBIAN_FRONTEND=noninteractive\n$aptitude update && $aptitude -y install"
        user_add_command useradd
        group_add_command groupadd
        key_gen_command sshkeygen
        groups_file groupfile
        users_file passwdfile
        change_password_command passwd
        sshd_configuration_directory sshdconfigFile.parentFile
        authorized_keys_file_pattern "$tmp/<user.home>/.ssh/authorized_keys"
    }
}
