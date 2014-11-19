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
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dns.core.resources.ResourcesUtils

/**
 * <i>Deadwood Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DeadwoodResources {

    profile("UbuntuProfile.groovy", DeadwoodResources.class.getResource("UbuntuProfile.groovy")),
    deadwoodScript("Dns.groovy", DeadwoodResources.class.getResource("Dns.groovy")),
    aptitudeCommand("/usr/bin/aptitude", DeadwoodResources.class.getResource("echo_command.txt")),
    restartCommand("/etc/init.d/deadwood", DeadwoodResources.class.getResource("echo_command.txt")),
    groupAddCommand("/usr/sbin/groupadd", DeadwoodResources.class.getResource("echo_command.txt")),
    userAddCommand("/usr/sbin/useradd", DeadwoodResources.class.getResource("echo_command.txt")),
    userIdCommand("/usr/bin/id", DeadwoodResources.class.getResource("id_command.txt")),
    chmodCommand("/bin/chmod", DeadwoodResources.class.getResource("echo_command.txt")),
    chownCommand("/bin/chown", DeadwoodResources.class.getResource("echo_command.txt")),
    updateRcCommand("/usr/sbin/update-rc.d", DeadwoodResources.class.getResource("echo_command.txt")),
    deadwoodCommand("/usr/sbin/deadwood", DeadwoodResources.class.getResource("echo_command.txt")),
    groupsFile("/etc/group", DeadwoodResources.class.getResource("group.txt")),
    usersFile("/etc/passwd", DeadwoodResources.class.getResource("passwd.txt")),
    confDir("/etc/maradns/deadwood", null),
    duendeLoggingDir("/etc/maradns/logger", null),
    sourcesListFile("/etc/apt/sources.list", DeadwoodResources.class.getResource("sources_list.txt")),
    sourcesListExpected("/etc/apt/sources.list", DeadwoodResources.class.getResource("sources_list_expected.txt")),
    deadwoodrc("/etc/maradns/deadwood/dwood3rc", DeadwoodResources.class.getResource("dwood3rc.txt")),
    scriptFile("/etc/init.d/deadwood", null),
    // expected
    deadwoodrcExpected("/etc/maradns/deadwood/dwood3rc", DeadwoodResources.class.getResource("dwood3rc_expected.txt")),
    deadwoodRunScriptExpected("/etc/init.d/deadwood", DeadwoodResources.class.getResource("deadwood_run_script_expected.txt")),
    updateRcOutExpected("/usr/sbin/update-rc.d.out", DeadwoodResources.class.getResource("updaterc_out_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", DeadwoodResources.class.getResource("aptitude_out_expected.txt")),
    deadwoodOutExpected("/etc/init.d/deadwood.out", DeadwoodResources.class.getResource("deadwood_out_expected.txt")),
    groupAddOutExpected("/usr/sbin/groupadd.out", DeadwoodResources.class.getResource("groupadd_out_expected.txt")),
    userAddOutExpected("/usr/sbin/useradd.out", DeadwoodResources.class.getResource("useradd_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", DeadwoodResources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", DeadwoodResources.class.getResource("chown_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        groupAddCommand.createCommand parent
        userAddCommand.createCommand parent
        userIdCommand.createCommand parent
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        updateRcCommand.createCommand parent
        deadwoodCommand.createCommand parent
        groupsFile.createFile parent
        usersFile.createFile parent
        confDir.asFile parent mkdirs()
        duendeLoggingDir.asFile parent mkdirs()
        sourcesListFile.createFile parent
        deadwoodrc.createFile parent
    }

    ResourcesUtils resources

    DeadwoodResources(String path, URL resource) {
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
