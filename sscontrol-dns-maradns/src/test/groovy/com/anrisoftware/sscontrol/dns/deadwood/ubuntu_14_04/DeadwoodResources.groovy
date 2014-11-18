/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
    addRepositoryCommand("/usr/bin/add-apt-repository", DeadwoodResources.class.getResource("echo_command.txt")),
    confDir("/etc/maradns/deadwood", null),
    sourcesListFile("/etc/apt/sources.list", DeadwoodResources.class.getResource("sources_list.txt")),
    sourcesListExpected("/etc/apt/sources.list", DeadwoodResources.class.getResource("sources_list_expected.txt")),
    deadwoodrc("/etc/maradns/deadwood/dwood3rc", DeadwoodResources.class.getResource("dwood3rc.txt")),
    deadwoodrcExpected("/etc/maradns/deadwood/dwood3rc", DeadwoodResources.class.getResource("dwood3rc_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", DeadwoodResources.class.getResource("aptitude_out_expected.txt")),
    restartOutExpected("/etc/init.d/deadwood.out", DeadwoodResources.class.getResource("restart_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        addRepositoryCommand.createCommand parent
        confDir.asFile parent mkdirs()
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
