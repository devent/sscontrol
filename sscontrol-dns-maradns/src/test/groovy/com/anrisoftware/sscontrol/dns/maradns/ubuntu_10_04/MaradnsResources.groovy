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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.dns.core.resources.ResourcesUtils

/**
 * <i>MaraDNS Ubuntu 10.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum MaradnsResources {

    profile("UbuntuProfile.groovy", MaradnsResources.class.getResource("UbuntuProfile.groovy")),
    sourcesListExpected("/etc/apt/sources.list", MaradnsResources.class.getResource("sources_list_expected.txt")),
    aptitudeCommand("/usr/bin/aptitude", MaradnsResources.class.getResource("echo_command.txt")),
    restartCommand("/etc/init.d/maradns", MaradnsResources.class.getResource("echo_command.txt")),
    addRepositoryCommand("/usr/bin/add-apt-repository", MaradnsResources.class.getResource("echo_command.txt")),
    confDir("/etc/maradns", null),
    packagesSourcesFile("/etc/apt/sources.list", MaradnsResources.class.getResource("sources_list.txt")),
    maradnsScript("Dns.groovy", MaradnsResources.class.getResource("Dns.groovy")),
    maradnsRecursiveScript("Dns.groovy", MaradnsResources.class.getResource("DnsRecursive.groovy")),
    mararc("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc.txt")),
    mararcExpected("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc_expected.txt")),
    mararcRecursiveExpected("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc_recursive_expected.txt")),
    dbAnrisoftwareExpected("/etc/maradns/db.anrisoftware.com", MaradnsResources.class.getResource("db.anrisoftware.com.txt")),
    dbExample1Expected("/etc/maradns/db.example1.com", MaradnsResources.class.getResource("db.example1.com.txt")),
    dbExample2Expected("/etc/maradns/db.example2.com", MaradnsResources.class.getResource("db.example2.com.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", MaradnsResources.class.getResource("aptitude_expected.txt")),
    restartOutExpected("/etc/init.d/maradns.out", MaradnsResources.class.getResource("restart_out.txt")),
    aptSources("/etc/apt/sources.list", MaradnsResources.class.getResource("sources.list")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        addRepositoryCommand.createCommand parent
        confDir.asFile parent mkdirs()
        packagesSourcesFile.createFile parent
    }

    ResourcesUtils resources

    MaradnsResources(String path, URL resource) {
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
