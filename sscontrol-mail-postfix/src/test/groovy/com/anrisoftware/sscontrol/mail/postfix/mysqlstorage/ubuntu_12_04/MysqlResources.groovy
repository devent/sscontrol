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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.mail.postfix.resources.ResourcesUtils
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_12_04.UbuntuResources

/**
 * Postfix/MySQL storage Ubuntu 12.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MysqlResources {

    mailScript("Mail.groovy", MysqlResources.class.getResource("MailMysql.groovy")),
    mailResetScript("Mail.groovy", MysqlResources.class.getResource("MailMysqlReset.groovy")),
    profile("UbuntuProfile.groovy", MysqlResources.class.getResource("MysqlUbuntuProfile.groovy")),
    saslRestartCommand("/etc/init.d/saslauthd", MysqlResources.class.getResource("echo_command.txt")),
    saslauthdFile("/etc/default/saslauthd", MysqlResources.class.getResource("saslauthd.txt")),
    saslSmtpdDir("/etc/postfix/sasl", null),
    chrootSaslauthdDirectory("/var/spool/postfix/var/run/saslauthd", null),
    smtpPamDirectory("/etc/pam.d", null),
    mainConfigExpected("/etc/postfix/main.cf", MysqlResources.class.getResource("main_cf_expected.txt")),
    masterConfigExpected("/etc/postfix/master.cf", MysqlResources.class.getResource("master_cf_expected.txt")),
    mailnameExpected("/etc/mailname", MysqlResources.class.getResource("mailname_expected.txt")),
    mailboxExpected("/etc/postfix/mysql_mailbox.cf", MysqlResources.class.getResource("mysql_mailbox_cf_expected.txt")),
    aliasExpected("/etc/postfix/mysql_alias.cf", MysqlResources.class.getResource("mysql_alias_cf_expected.txt")),
    domainsExpected("/etc/postfix/mysql_domains.cf", MysqlResources.class.getResource("mysql_domains_cf_expected.txt")),
    saslauthdExpected("/etc/default/saslauthd", MysqlResources.class.getResource("saslauthd_expected.txt")),
    smtpdConfExpected("/etc/postfix/sasl/smtpd.conf", MysqlResources.class.getResource("smtpd_conf_expected.txt")),
    smtpdPamExpected("/etc/pam.d/smtp", MysqlResources.class.getResource("smtpd_pam_expected.txt")),
    aptitudeOut("/usr/bin/aptitude.out", MysqlResources.class.getResource("aptitude_out.txt")),
    chownOut("bin/chown.out", MysqlResources.class.getResource("chown_out.txt")),
    chmodOut("bin/chmod.out", MysqlResources.class.getResource("chmod_out.txt")),
    mysqlOut("/usr/bin/mysql.out", MysqlResources.class.getResource("mysql_out.txt")),
    mysqlResetDomainsOut("/usr/bin/mysql.out", MysqlResources.class.getResource("mysql_resetdomains_out.txt")),
    postaliasOut("/usr/sbin/postalias.out", MysqlResources.class.getResource("postalias_out.txt")),
    useraddOut("/sbin/useradd.out", MysqlResources.class.getResource("useradd_out.txt")),
    usermodOut("/sbin/usermod.out", MysqlResources.class.getResource("usermod_out.txt")),
    groupaddOut("/sbin/groupadd.out", MysqlResources.class.getResource("groupadd_out.txt")),

    static void copyMysqlFiles(File parent) {
        UbuntuResources.mysqlCommand.createCommand parent
        saslRestartCommand.createCommand parent
        saslauthdFile.createFile parent
        saslSmtpdDir.asFile parent mkdirs()
        smtpPamDirectory.asFile parent mkdirs()
    }

    ResourcesUtils resources

    MysqlResources(String path, URL resource) {
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
