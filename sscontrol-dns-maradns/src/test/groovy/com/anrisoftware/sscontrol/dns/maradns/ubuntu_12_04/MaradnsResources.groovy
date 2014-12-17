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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_12_04

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
    maradnsScript("Dns.groovy", MaradnsResources.class.getResource("Dns.groovy")),
    maradnsRecursiveScript("Dns.groovy", MaradnsResources.class.getResource("DnsRecursive.groovy")),
    aptitudeCommand("/usr/bin/aptitude", MaradnsResources.class.getResource("echo_command.txt")),
    restartCommand("/etc/init.d/maradns", MaradnsResources.class.getResource("echo_command.txt")),
    confDir("/etc/maradns", null),
    mararc("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc.txt")),
    mararcExpected("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc_expected.txt")),
    mararcRecursiveExpected("/etc/maradns/mararc", MaradnsResources.class.getResource("mararc_recursive_expected.txt")),
    dbAnrisoftwareExpected("/etc/maradns/db.anrisoftware.com", MaradnsResources.class.getResource("db_anrisoftware_com_expected.txt")),
    dbExample1Expected("/etc/maradns/db.example1.com", MaradnsResources.class.getResource("db_example1_com_expected.txt")),
    dbExample2Expected("/etc/maradns/db.example2.com", MaradnsResources.class.getResource("db_example2_com_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", MaradnsResources.class.getResource("aptitude_out_expected.txt")),
    restartOutExpected("/etc/init.d/maradns.out", MaradnsResources.class.getResource("restart_out_expected.txt")),

    static copyUbuntuFiles(File parent) {
        aptitudeCommand.createCommand parent
        restartCommand.createCommand parent
        confDir.asFile parent mkdirs()
        mararc.createFile parent
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
