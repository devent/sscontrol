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
def restart = UbuntuResources.restartCommand.asFile(tmp)
def useradd = UbuntuResources.useraddCommand.asFile(tmp)
def groupadd = UbuntuResources.groupaddCommand.asFile(tmp)
def chown = UbuntuResources.chownCommand.asFile(tmp)
def chmod = UbuntuResources.chmodCommand.asFile(tmp)
def chpasswd = UbuntuResources.chpasswdCommand.asFile(tmp)
def sshkeygen = UbuntuResources.sshkeygenCommand.asFile(tmp)
def groupfile = UbuntuResources.groupsFile.asFile(tmp)
def passwdfile = UbuntuResources.passwdFile.asFile(tmp)
def sshdconfigFile = UbuntuResources.sshdconfigFile.asFile(tmp)
def localBinDirectory = UbuntuResources.localBinDirectory.asFile(tmp)

profile "ubuntu_10_04", {
    remote {
        service "openssh"
        install_command "export DEBIAN_FRONTEND=noninteractive\n$aptitude update && $aptitude -y install"
        restart_command restart
        user_add_command useradd
        group_add_command groupadd
        chown_command chown
        chmod_command chmod
        key_gen_command sshkeygen
        change_password_command chpasswd
        sshd_configuration_directory sshdconfigFile.parentFile
        local_bin_directory localBinDirectory
        groups_file groupfile
        users_file passwdfile
        home_pattern "$tmp/home/<user.name>"
        ssh_key_pattern "$tmp/home/<user.name>/.ssh/id_rsa"
    }
}
