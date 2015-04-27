/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.archive_2_3

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>FrontAccounting 2.3</i> archive resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum Frontaccounting_2_3_ArchiveResources {

    // files
    frontaccountingArchive("/tmp/frontaccounting-2.3.24.tar.gz", Frontaccounting_2_3_ArchiveResources.class.getResource("frontaccounting-2.3.24.tar.gz")),
    frontaccountingDefaultSample(null, Frontaccounting_2_3_ArchiveResources.class.getResource("configdefaultphp.txt")),
    frontaccountingDbConfig(null, Frontaccounting_2_3_ArchiveResources.class.getResource("configdbphp.txt")),

    static void copyFrontaccountingArchiveFiles(File parent) {
        // files
        frontaccountingArchive.createFile parent
    }

    static void setupFrontaccountingArchiveProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        entry.frontaccounting_archive frontaccountingArchive.asFile(parent)
        entry.frontaccounting_archive_hash "md5:9f9703cbdb21a2ba26e61e04c4022ba1"
    }

    ResourcesUtils resources

    Frontaccounting_2_3_ArchiveResources(String path, URL resource) {
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
