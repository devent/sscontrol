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
package com.anrisoftware.sscontrol.scripts.changefile

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.scripts.resources.ResourcesUtils

/**
 * Test resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Resources {

    fileFooCommand("/tmp/foo", Resources.class.getResource("echo_command.txt")),
    fileBarCommand("/tmp/bar", Resources.class.getResource("echo_command.txt")),
    // change file mode
    chmodCommand("/bin/chmod", Resources.class.getResource("echo_command.txt")),
    chmodOutExpected("/bin/chmod.out", Resources.class.getResource("chmod_out_expected.txt")),
    chmodRecursiveOutExpected("/bin/chmod.out", Resources.class.getResource("chmod_recursive_out_expected.txt")),
    // change file owner
    chownCommand("/bin/chown", Resources.class.getResource("echo_command.txt")),
    chownOutExpected("/bin/chown.out", Resources.class.getResource("chown_out_expected.txt")),
    chownRecursiveOutExpected("/bin/chown.out", Resources.class.getResource("chown_recursive_out_expected.txt")),

    static void copyTestFiles(File parent) {
        chmodCommand.createCommand parent
        chownCommand.createCommand parent
        fileFooCommand.createFile parent
        fileBarCommand.createFile parent
    }

    ResourcesUtils resources

    Resources(String path, URL resource) {
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
