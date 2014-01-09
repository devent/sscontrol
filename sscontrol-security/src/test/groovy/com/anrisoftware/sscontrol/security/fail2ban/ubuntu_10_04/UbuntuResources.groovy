/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.security.resources.ResourcesUtils

/**
 * Remote Access/Ubuntu 10.04 resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum UbuntuResources {

    profile("UbuntuProfile.groovy", UbuntuResources.class.getResource("UbuntuProfile.groovy")),
    securityService("Security.groovy", UbuntuResources.class.getResource("Security.groovy")),
    aptitudeCommand("/usr/bin/aptitude", UbuntuResources.class.getResource("echo_command.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", UbuntuResources.class.getResource("aptitude_out_excepted.txt")),
    fail2banRestartCommand("/etc/init.d/fail2ban", UbuntuResources.class.getResource("echo_command.txt")),
    fail2banRestartOutExpected("/etc/init.d/fail2ban.out", UbuntuResources.class.getResource("fail2ban_out_expected.txt")),
    fail2banDirectory("/etc/fail2ban", null),
    fail2banConf("/etc/fail2ban/fail2ban.conf", UbuntuResources.class.getResource("fail2ban_conf.txt")),
    fail2banConfExpected("/etc/fail2ban/fail2ban.local", UbuntuResources.class.getResource("fail2ban_local_expected.txt")),
    fail2banJailConf("/etc/fail2ban/jail.conf", UbuntuResources.class.getResource("jail_conf.txt")),
    ufwSshConfExpected("/etc/fail2ban/action.d/ufw-ssh.conf", UbuntuResources.class.getResource("ufw_ssh_conf_expected.txt")),
    ufwSshddosConfExpected("/etc/fail2ban/action.d/ufw-ssh-ddos.conf", UbuntuResources.class.getResource("ufw_ssh_ddos_conf_expected.txt")),
    jailConfExpected("/etc/fail2ban/jail.local", UbuntuResources.class.getResource("jail_local_expected.txt")),
    ufwCommand("/usr/sbin/ufw", UbuntuResources.class.getResource("echo_command.txt")),
    ufwOutExpected("/usr/sbin/ufw.out", UbuntuResources.class.getResource("ufw_out.txt")),

    static void copyUbuntu_10_04_Files(File parent) {
        aptitudeCommand.createCommand parent
        fail2banDirectory.asFile parent mkdirs()
        fail2banConf.createFile parent
        fail2banJailConf.createFile parent
        fail2banRestartCommand.createCommand parent
        ufwCommand.createCommand parent
    }

    ResourcesUtils resources

    UbuntuResources(String path, URL resource) {
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
