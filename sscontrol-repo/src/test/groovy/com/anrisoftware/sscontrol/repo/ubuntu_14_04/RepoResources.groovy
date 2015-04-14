/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.ubuntu_14_04

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.repo.resources.ResourcesUtils

/**
 * <i>Repo Ubuntu 14.04</i> resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RepoResources {

    profile("UbuntuProfile.groovy", RepoResources.class.getResource("UbuntuProfile.groovy")),
    repoService("Repo.groovy", RepoResources.class.getResource("RepoService.groovy")),
    // commands
    aptitudeCommand("/usr/bin/aptitude", RepoResources.class.getResource("echo_command.txt")),
    // files
    confDir("/etc/apt", new URL("file://")),
    sourcesList("/etc/apt/sources.list", RepoResources.class.getResource("sources_list.txt")),
    // expected
    sourcesListExpected("/etc/apt/sources.list", RepoResources.class.getResource("sources_list_expected.txt")),
    proxyExpected("/etc/apt/apt.conf.d/01proxy-robobee", RepoResources.class.getResource("proxyrobobee_expected.txt")),
    aptitudeOutExpected("/usr/bin/aptitude.out", RepoResources.class.getResource("aptitude_out_expected.txt")),
    aptkeyOutExpected("/usr/bin/apt-key.out", RepoResources.class.getResource("aptkey_out_expected.txt")),

    static copyRepoFiles(File parent) {
        confDir.asFile(parent).mkdirs()
        sourcesList.createFile parent
        aptitudeCommand.createCommand parent
    }

    static void setupRepoProperties(def profile, File parent) {
        def entry = profile.getEntry("repo")
        entry.aptitude_command aptitudeCommand.asFile(parent)
        entry.configuration_directory confDir.asFile(parent)
    }

    ResourcesUtils resources

    RepoResources(String path, URL resource) {
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
