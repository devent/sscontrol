/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashFactory
import com.anrisoftware.sscontrol.httpd.apache.wordpress.linux.Wordpress_3_Config
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService

/**
 * Ubuntu Wordpress 3.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuWordpressConfig extends Wordpress_3_Config {

    @Inject
    private UbuntuWordpressConfigLogger log

    @Inject
    private CheckFileHashFactory checkFileHashFactory

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultForce service
        installPackages wordpressPackages
        enableMods wordpressMods
        downloadArchive domain, service
        deployMainConfig domain, service
        deployDatabaseConfig domain, service
        deployKeysConfig domain, service
        deployLanguageConfig domain, service
        deploySecureLoginConfig domain, service
        deployDebugConfig domain, service
        deployMultisiteConfig domain, service
        deployMainConfigEnding domain, service
        createDirectories domain, service
        deployThemes domain, service
        deployPlugins domain, service
        setupPermissions domain, service
    }

    /**
     * Downloads and unpacks the Wordpress archive.
     *
     * @param domain
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WebService} Wordpress service.
     */
    void downloadArchive(Domain domain, WebService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-3-8-$name")
        needDownloadArchive(dest) ? copyURLToFile(wordpressArchive.toURL(), dest) : false
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see WordpressService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, WordpressService service) {
        service.overrideMode != OverrideMode.no || !configurationDistFile(domain, service).isFile()
    }

    boolean needDownloadArchive(File dest) {
        log.checkNeedDownloadArchive script, dest, wordpressArchiveHash
        if (dest.isFile() && wordpressArchiveHash != null) {
            def check = checkFileHashFactory.create this, file: dest, hash: wordpressArchiveHash
            check().matching
        } else {
            true
        }
    }

    /**
     * Unpacks the Wordpress archive.
     *
     * @param domain
     *            the {@link Domain} domain of the Wordpress service.
     *
     * @param service
     *            the {@link WebService} Wordpress service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = wordpressDir domain, service
        if (!dir.isDirectory()) {
            dir.mkdirs()
        }
        def strip = stripArchive
        unpack file: archive, type: archiveType(file: archive), output: dir, override: true, strip: strip
        log.downloadArchive script, wordpressArchive
    }

    /**
     * Creates the cache, plugins, themes and upload directories.
     *
     * @param domain
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void createDirectories(Domain domain, WordpressService service) {
        wordpressContentCacheDir domain, service mkdirs()
        wordpressContentPluginsDir domain, service mkdirs()
        wordpressContentThemesDir domain, service mkdirs()
        wordpressContentUploadsDir domain, service mkdirs()
    }

    /**
     * Sets the owner and permissions of the Wordpress service.
     *
     * @param domain
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void setupPermissions(Domain domain, WordpressService service) {
        def user = domain.domainUser
        def conffile = configurationFile domain, service
        def cachedir = wordpressContentCacheDir domain, service
        def pluginsdir = wordpressContentPluginsDir domain, service
        def themesdir = wordpressContentThemesDir domain, service
        def uploadsdir = wordpressContentUploadsDir domain, service
        script.changeOwner owner: "root", ownerGroup: user.group, files: [
            conffile
        ]
        script.changeMod mod: "0440", files: [
            conffile
        ]
        script.changeOwner owner: user.name, ownerGroup: user.group, files: [
            cachedir,
            pluginsdir,
            themesdir,
            uploadsdir,
        ]
        script.changeMod mod: "0775", files: [
            cachedir,
            pluginsdir,
            themesdir,
            uploadsdir,
        ]
    }
}
