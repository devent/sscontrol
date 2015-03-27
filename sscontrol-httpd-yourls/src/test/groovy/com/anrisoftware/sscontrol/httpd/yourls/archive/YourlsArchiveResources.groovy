/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.archive

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Yourls</i> archive resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum YourlsArchiveResources {

    // files
    yourlsArchive("/tmp/YOURLS-1.7.tar.gz", YourlsArchiveResources.class.getResource("YOURLS-1.7.tar.gz")),
    yourlsConfigSample(null, YourlsArchiveResources.class.getResource("configsamplephp.txt")),

    static void copyYourlsArchiveFiles(File parent) {
        // files
        yourlsArchive.createFile parent
    }

    static void setupYourlsArchiveProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // piwik
        entry.yourls_archive yourlsArchive.asFile(parent)
        entry.yourls_archive_hash "md5:bc63a84659356d66488458253510b195"
    }

    ResourcesUtils resources

    YourlsArchiveResources(String path, URL resource) {
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
