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

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dns.core.resources.ResourcesUtils

/**
 * <i>MaraDNS/Deadwood Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MaradnsDeadwoodResources {

    profile("UbuntuProfile.groovy", MaradnsDeadwoodResources.class.getResource("UbuntuProfile.groovy")),
    zonesScript("DnsZones.groovy", MaradnsDeadwoodResources.class.getResource("DnsZones.groovy")),
    recursiveScript("DnsRecursive.groovy", MaradnsDeadwoodResources.class.getResource("DnsRecursive.groovy")),
    aptitudeCommand("/usr/bin/aptitude", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    groupAddCommand("/usr/sbin/groupadd", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    userAddCommand("/usr/sbin/useradd", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    userIdCommand("/usr/bin/id", MaradnsDeadwoodResources.class.getResource("id_command.txt")),
    chmodCommand("/bin/chmod", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    updateRcCommand("/usr/sbin/update-rc.d", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    groupsFile("/etc/group", MaradnsDeadwoodResources.class.getResource("group.txt")),
    usersFile("/etc/passwd", MaradnsDeadwoodResources.class.getResource("passwd.txt")),
    scriptFile("/etc/init.d/deadwood", null),
    // Deadwood
    deadwoodRestartCommand("/etc/init.d/deadwood", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    deadwoodCommand("/usr/sbin/deadwood", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    deadwoodConfDir("/etc/maradns/deadwood", null),
    deadwoodrc("/etc/maradns/deadwood/dwood3rc", MaradnsDeadwoodResources.class.getResource("dwood3rc.txt")),
    // MaraDNS
    maradnsRestartCommand("/etc/init.d/maradns", MaradnsDeadwoodResources.class.getResource("echo_command.txt")),
    maradnsConfDir("/etc/maradns", null),
    maradrc("/etc/maradns/mararc", MaradnsDeadwoodResources.class.getResource("mararc.txt")),
    // expected
    deadwoodrcExpected("/etc/maradns/deadwood/dwood3rc", MaradnsDeadwoodResources.class.getResource("dwood3rc_expected.txt")),
    mararcExpected("/etc/maradns/mararc", MaradnsDeadwoodResources.class.getResource("mararc_expected.txt")),
    deadwoodRunScriptExpected("/etc/init.d/deadwood", MaradnsDeadwoodResources.class.getResource("deadwood_run_script_expected.txt")),
    updateRcOutExpected("/usr/sbin/update-rc.d.out", MaradnsDeadwoodResources.class.getResource("updaterc_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", MaradnsDeadwoodResources.class.getResource("aptitude_out_expected.txt")),
    deadwoodOutExpected("/etc/sbin/deadwood.out", MaradnsDeadwoodResources.class.getResource("deadwood_out_expected.txt")),
    groupAddOutExpected("/usr/sbin/groupadd.out", MaradnsDeadwoodResources.class.getResource("groupadd_out_expected.txt")),
    userAddOutExpected("/usr/sbin/useradd.out", MaradnsDeadwoodResources.class.getResource("useradd_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", MaradnsDeadwoodResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", MaradnsDeadwoodResources.class.getResource("chown_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        groupAddCommand.createCommand parent
        userAddCommand.createCommand parent
        userIdCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        updateRcCommand.createCommand parent
        groupsFile.createFile parent
        usersFile.createFile parent
        // Deadwood
        deadwoodRestartCommand.createCommand parent
        deadwoodCommand.createCommand parent
        deadwoodrc.createFile parent
        // MaraDNS
        maradnsRestartCommand.createCommand parent
        maradnsConfDir.asFile parent mkdirs()
        maradrc.createFile parent
    }

    ResourcesUtils resources

    MaradnsDeadwoodResources(String path, URL resource) {
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
