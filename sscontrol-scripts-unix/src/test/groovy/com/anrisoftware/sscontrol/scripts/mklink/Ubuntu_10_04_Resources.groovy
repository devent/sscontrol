/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.mklink

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.scripts.resources.ResourcesUtils

/**
 * <i>Ubuntu 10.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_10_04_Resources {

    lnCommand("/usr/bin/ln", Ubuntu_10_04_Resources.class.getResource("echo_command.txt")),
    lnOutExpected("/usr/bin/ln.out", Ubuntu_10_04_Resources.class.getResource("ln_out_expected.txt")),
    lnOverrideOutExpected("/usr/bin/ln.out", Ubuntu_10_04_Resources.class.getResource("ln_override_out_expected.txt")),
    lnsOutExpected("/usr/bin/ln.out", Ubuntu_10_04_Resources.class.getResource("lns_out_expected.txt")),
    sourceA("/tmp/source_a", Ubuntu_10_04_Resources.class.getResource("source.txt")),
    sourceB("/tmp/source_b", Ubuntu_10_04_Resources.class.getResource("source.txt")),
    targetA("/tmp/source_a_target", null),
    targetB("/tmp/source_b_target", null),

    static void copyUbuntu_10_04_Files(File parent) {
        lnCommand.createCommand parent
        sourceA.createFile parent
        sourceB.createFile parent
    }

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
