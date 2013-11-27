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
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.firewall.ufw.resources.ResourcesUtils

/**
 * Firewall/Ubuntu resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_10_04_Resources {

    profile("Ubuntu_10_04Profile.groovy", UfwLinuxBase.class.getResource("Ubuntu_10_04Profile.groovy")),
    aptitudeInExpected("/usr/bin/aptitude.in", UfwLinuxBase.class.getResource("aptitude_in.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", UfwLinuxBase.class.getResource("aptitude_out.txt")),
    ufwInAllowExpected("/usr/sbin/ufw.in", UfwLinuxBase.class.getResource("ufw_allow_in.txt")),
    ufwOutAllowExpected("/usr/sbin/ufw.out", UfwLinuxBase.class.getResource("ufw_allow_out.txt")),
    ufwInDenyExpected("/usr/sbin/ufw.in", UfwLinuxBase.class.getResource("ufw_deny_in.txt")),
    ufwOutDenyExpected("/usr/sbin/ufw.out", UfwLinuxBase.class.getResource("ufw_deny_out.txt")),

    ResourcesUtils resources

    Ubuntu_10_04_Resources(String path, URL resource) {
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
