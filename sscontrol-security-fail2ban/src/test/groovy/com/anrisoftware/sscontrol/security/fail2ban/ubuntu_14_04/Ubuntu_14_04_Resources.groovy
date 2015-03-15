/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.security.resources.ResourcesUtils

/**
 * <i>Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_14_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_14_04_Resources.class.getResource("UbuntuProfile.groovy")),
    securityService("Security.groovy", Ubuntu_14_04_Resources.class.getResource("Security.groovy")),
    // expected
    aptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_14_04_Resources.class.getResource("aptitude_out_excepted.txt")),
    fail2banRestartCommand("/etc/init.d/fail2ban", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    fail2banRestartOutExpected("/etc/init.d/fail2ban.out", Ubuntu_14_04_Resources.class.getResource("fail2ban_out_expected.txt")),
    fail2banDirectory("/etc/fail2ban", null),
    fail2banConf("/etc/fail2ban/fail2ban.conf", Ubuntu_14_04_Resources.class.getResource("fail2ban_conf.txt")),
    fail2banConfExpected("/etc/fail2ban/fail2ban.local", Ubuntu_14_04_Resources.class.getResource("fail2ban_local_expected.txt")),
    fail2banJailConf("/etc/fail2ban/jail.conf", Ubuntu_14_04_Resources.class.getResource("jail_conf.txt")),
    ufwSshConfExpected("/etc/fail2ban/action.d/ufw-ssh.conf", Ubuntu_14_04_Resources.class.getResource("ufw_ssh_conf_expected.txt")),
    ufwSshddosConfExpected("/etc/fail2ban/action.d/ufw-ssh-ddos.conf", Ubuntu_14_04_Resources.class.getResource("ufw_ssh_ddos_conf_expected.txt")),
    jailConfExpected("/etc/fail2ban/jail.local", Ubuntu_14_04_Resources.class.getResource("jail_local_expected.txt")),
    ufwCommand("/usr/sbin/ufw", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),
    ufwOutExpected("/usr/sbin/ufw.out", Ubuntu_14_04_Resources.class.getResource("ufw_out.txt")),
    rsyslogConfigFile("/etc/rsyslog.conf", Ubuntu_14_04_Resources.class.getResource("rsyslog_conf.txt")),
    rsyslogConfigExpected("/etc/rsyslog.conf", Ubuntu_14_04_Resources.class.getResource("rsyslog_conf_expected.txt")),
    rsyslogRestartCommand("/sbin/restart", Ubuntu_14_04_Resources.class.getResource("echo_command.txt")),

    static void copyUbuntu_14_04_Files(File parent) {
        fail2banRestartCommand.createCommand parent
        rsyslogRestartCommand.createCommand parent
        ufwCommand.createCommand parent
        fail2banDirectory.asFile parent mkdirs()
        fail2banConf.createFile parent
        fail2banJailConf.createFile parent
        rsyslogConfigFile.createFile parent
    }

    static void setupUbuntu_14_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("security")
        entry.fail2ban_restart_command fail2banRestartCommand.asFile(parent)
        entry.fail2ban_configuration_directory fail2banDirectory.asFile(parent)
        entry.ufw_command ufwCommand.asFile(parent)
        entry.rsyslog_restart_command rsyslogRestartCommand.asFile(parent)
        entry.rsyslog_configuration_file rsyslogConfigFile.asFile(parent)
    }

    ResourcesUtils resources

    Ubuntu_14_04_Resources(String path, URL resource) {
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
