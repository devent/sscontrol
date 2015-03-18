/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu_12_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Ubuntu 12.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Ubuntu_12_04_Resources {

    profile("UbuntuProfile.groovy", Ubuntu_12_04_Resources.class.getResource("UbuntuProfile.groovy")),
    sourceService("Source.groovy", Ubuntu_12_04_Resources.class.getResource("Source.groovy")),
    // commands
    gitoliteInstallCommand("/usr/local/gitolite/install", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    gitoliteCommand("/usr/local/bin/gitolite", Ubuntu_12_04_Resources.class.getResource("echo_command.txt")),
    // files
    groupsFile("/etc/group", Ubuntu_12_04_Resources.class.getResource("group.txt")),
    usersFile("/etc/passwd", Ubuntu_12_04_Resources.class.getResource("passwd.txt")),
    gitoliteArchive("/tmp/gitolite-3.6.2.tar.gz", Ubuntu_12_04_Resources.class.getResource("/com/anrisoftware/sscontrol/source/gitolite/archive/gitolite-3.6.2.tar.gz")),
    gitoliteRcFile("/var/git/.gitolite.rc", Ubuntu_12_04_Resources.class.getResource("/com/anrisoftware/sscontrol/source/gitolite/archive/gitoliterc.txt")),
    adminKey("/tmp/admin.pub", Ubuntu_12_04_Resources.class.getResource("adminpub.txt")),
    prefixDirectory("/usr/local/gitolite", null),
    gitoliteDataDirectory("/var/git", null),
    // expected
    runcommandsLogExpected("/runcommands.log", Ubuntu_12_04_Resources.class.getResource("runcommands_expected.txt")),
    gitolitercExpected("/var/git/.gitolite.rc", Ubuntu_12_04_Resources.class.getResource("gitoliterc_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", Ubuntu_12_04_Resources.class.getResource("aptitude_out_excepted.txt")),
    gitoliteInstallOutExpected("/usr/local/gitolite/install.out", Ubuntu_12_04_Resources.class.getResource("gitoliteinstall_out_expected.txt")),
    gitoliteOutExpected("/usr/local/bin/gitolite.out", Ubuntu_12_04_Resources.class.getResource("gitolite_out_expected.txt")),
    suOutExpected("/bin/su.out", Ubuntu_12_04_Resources.class.getResource("su_out_expected.txt")),
    tarOutExpected("/bin/tar.out", Ubuntu_12_04_Resources.class.getResource("tar_out_expected.txt")),
    chmodOutExpected("/bin/chmod.out", Ubuntu_12_04_Resources.class.getResource("chmod_out_expected.txt")),
    chownOutExpected("/bin/chown.out", Ubuntu_12_04_Resources.class.getResource("chown_out_expected.txt")),

    static void copyUbuntu_12_04_Files(File parent) {
        // commands
        gitoliteInstallCommand.createCommand parent
        gitoliteCommand.createCommand parent
        // files
        groupsFile.createFile parent
        usersFile.createFile parent
        gitoliteArchive.createFile parent
        gitoliteRcFile.createFile parent
        gitoliteDataDirectory.asFile parent mkdirs()
    }

    static void setupUbuntu_12_04_Properties(def profile, File parent) {
        def entry = profile.getEntry("source")
        // files
        entry.groups_file groupsFile.asFile(parent)
        entry.users_file usersFile.asFile(parent)
        entry.gitolite_default_data gitoliteDataDirectory.asFile(parent)
        entry.gitolite_default_prefix prefixDirectory.asFile(parent)
        entry.gitolite_archive gitoliteArchive.asFile(parent)
        entry.gitolite_archive_hash "md5:dcf2bc265e5c9ec19918abe5920d0fc8"
    }

    ResourcesUtils resources

    Ubuntu_12_04_Resources(String path, URL resource) {
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
