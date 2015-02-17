/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.owncloudarchive

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.sscontrol.testutils.resources.ResourcesUtils

/**
 * <i>Piwik</i> with <i>Nginx</i> proxy resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum OwncloudArchiveResources {

    // owncloud
    owncloudArchive("/tmp/owncloud-7.0.4.tar.bz2", OwncloudArchiveResources.class.getResource("owncloud-7.0.4.tar.bz2")),

    static void copyOwncloudArchiveFiles(File parent) {
        // owncloud
        owncloudArchive.createFile parent
    }

    static void setupOwncloudArchiveProperties(def profile, File parent) {
        def entry = profile.getEntry("httpd")
        // owncloud
        entry.owncloud_archive OwncloudArchiveResources.owncloudArchive.asFile(parent)
        entry.owncloud_archive_hash "md5:62f256a4fe374907cef083b0e0b7b7a1"
    }

    ResourcesUtils resources

    OwncloudArchiveResources(String path, URL resource) {
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
