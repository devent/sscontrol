/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_12_04

import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.UbuntuResources

def restartCommand = UbuntuResources.restartCommand.asFile(tmp)
def saslSmtpdDir = MysqlResources.saslSmtpdDir.asFile(tmp)
def smtpPamDirectory = MysqlResources.smtpPamDirectory.asFile(tmp)
def saslRestartCommand = MysqlResources.saslRestartCommand.asFile(tmp)

profile "ubuntu_12_04", {
    mail {
        service "postfix"
        storage "mysql"
        auth "sasl"
        install_command UbuntuResources.aptitudeCommand.asFile(tmp)
        restart_command "$restartCommand restart"
        chown_command UbuntuResources.chownCommand.asFile(tmp)
        chmod_command UbuntuResources.chmodCommand.asFile(tmp)
        group_add_command UbuntuResources.groupaddCommand.asFile(tmp)
        user_add_command UbuntuResources.useraddCommand.asFile(tmp)
        user_mod_command UbuntuResources.usermodCommand.asFile(tmp)
        mysql_command UbuntuResources.mysqlCommand.asFile(tmp)
        postalias_command UbuntuResources.postaliasCommand.asFile(tmp)
        postmap_command UbuntuResources.postmapCommand.asFile(tmp)
        sasl_restart_command "$saslRestartCommand restart"
        certificates_directory UbuntuResources.certsDir.asFile(tmp)
        certificates_keys_directory UbuntuResources.certKeysDir.asFile(tmp)
        groups_file UbuntuResources.group.asFile(tmp)
        users_file UbuntuResources.passwd.asFile(tmp)
        mailname_file UbuntuResources.mailname.asFile(tmp)
        configuration_directory UbuntuResources.confDir.asFile(tmp)
        mailbox_base_directory UbuntuResources.mailboxBaseDir.asFile(tmp)
        chroot_saslauthd_directory MysqlResources.chrootSaslauthdDirectory.asFile(tmp)
        smtpd_pam_file "$smtpPamDirectory/smtp"
        sasl_authd_file MysqlResources.saslauthdFile.asFile(tmp)
        sasl_smtpd_file "$saslSmtpdDir/smtpd.conf"
    }
}
