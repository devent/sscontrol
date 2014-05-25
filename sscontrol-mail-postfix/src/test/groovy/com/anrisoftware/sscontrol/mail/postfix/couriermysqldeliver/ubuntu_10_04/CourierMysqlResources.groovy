/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.mail.postfix.couriermysqldeliver.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.mail.postfix.resources.ResourcesUtils
import com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources

/**
 * Courier/MySQL delivery Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum CourierMysqlResources {

    mailScript("Mail.groovy", CourierMysqlResources.class.getResource("MailCourierMysql.groovy")),
    profile("UbuntuProfile.groovy", CourierMysqlResources.class.getResource("CourierMysqlUbuntuProfile.groovy")),
    courierImapRestartCommand("/etc/init.d/courier-imap", UbuntuResources.class.getResource("echo_command.txt")),
    courierImapRestartOut("/etc/init.d/courier-imap.out", CourierMysqlResources.class.getResource("courier_imap_out.txt")),
    courierAuthdaemonRestartCommand("/etc/init.d/courier-authdaemon", UbuntuResources.class.getResource("echo_command.txt")),
    courierAuthdaemonRestartOut("/etc/init.d/courier-authdaemon.out", CourierMysqlResources.class.getResource("courier_authdaemon_out.txt")),
    configDir("/etc/courier", null),
    authdaemonConfig("/etc/courier/authdaemonrc", CourierMysqlResources.class.getResource("authdaemonrc.txt")),
    authdaemonConfigExpected("/etc/courier/authdaemonrc", CourierMysqlResources.class.getResource("authdaemonrc_expected.txt")),
    authmysqlConfig("/etc/courier/authmysqlrc", CourierMysqlResources.class.getResource("authmysqlrc.txt")),
    authmysqlConfigExpected("/etc/courier/authmysqlrc", CourierMysqlResources.class.getResource("authmysqlrc_expected.txt")),
    imapdFile("/etc/courier/imapd", CourierMysqlResources.class.getResource("imapd.txt")),
    imapdConfigExpected("/etc/courier/imapd", CourierMysqlResources.class.getResource("imapd_expected.txt")),
    imapdSslFile("/etc/courier/imapd-ssl", CourierMysqlResources.class.getResource("imapd_ssl.txt")),
    imapdSslConfigExpected("/etc/courier/imapd-ssl", CourierMysqlResources.class.getResource("imapd_ssl_expected.txt")),

    static copyCourierFiles(File parent) {
        configDir.asFile parent mkdirs()
        courierImapRestartCommand.createCommand parent
        courierAuthdaemonRestartCommand.createCommand parent
        authdaemonConfig.createFile parent
        authmysqlConfig.createFile parent
        imapdFile.createFile parent
        imapdSslFile.createFile parent
    }

    ResourcesUtils resources

    CourierMysqlResources(String path, URL resource) {
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
