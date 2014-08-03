/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.fromarchive

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * Installs <i>Wordpress</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class WordpressFromArchiveConfig {

    @Inject
    private WordpressFromArchiveConfigLogger logg

    @Inject
    private CheckFileHashFactory checkFileHashFactory

    private Object script

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    UnpackFactory unpackFactory

    /**
     * Downloads and unpacks the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the <i>Wordpress</i> service.
     *
     * @param service
     *            the {@link WebService} <i>Wordpress</i> service.
     */
    void downloadArchive(Domain domain, WebService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-3-$name")
        needDownloadArchive(dest) ? downloadArchive(dest) : false
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to unpack the <i>Wordpress</i> archive.
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

    /**
     * Returns if it needed to download the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return {@code true} if it is needed.
     */
    boolean needDownloadArchive(File dest) {
        logg.checkNeedDownloadArchive script.script, dest, wordpressArchiveHash
        if (dest.isFile() && wordpressArchiveHash != null) {
            def check = checkFileHashFactory.create(this, file: dest, hash: wordpressArchiveHash)()
            return !check.matching
        } else {
            return true
        }
    }

    /**
     * Downloads the <i>Wordpress</i> archive.
     *
     * @param dest
     *            the {@link File} destination.
     */
    void downloadArchive(File dest) {
        logg.startDownload script.script, wordpressArchive, dest
        copyURLToFile(wordpressArchive.toURL(), dest)
        logg.finishDownload script.script, wordpressArchive, dest
    }

    /**
     * Unpacks the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the <i>Wordpress</i> service.
     *
     * @param service
     *            the {@link WebService} <i>Wordpress</i> service.
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
        unpackFactory.create(
                log: log, file: archive, output: dir, override: true, strip: strip,
                commands: script.unpackCommands,
                this, threads)()
        logg.downloadArchive script.script, wordpressArchive
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
        changeFileOwnerFactory.create(
                log: log, command: script.chownCommand,
                owner: "root", ownerGroup: user.group, files: conffile,
                this, threads)()
        changeFileModFactory.create(
                log: log, command: script.chmodCommand,
                mod: "0440", files: conffile,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log, command: script.chownCommand,
                owner: user.name, ownerGroup: user.group, files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                this, threads)()
        changeFileOwnerFactory.create(
                log: log, command: script.chownCommand, recursive: true,
                owner: user.name, ownerGroup: user.group, files: [
                    cachedir,
                    uploadsdir,
                ],
                this, threads)()
        changeFileModFactory.create(
                log: log, command: script.chmodCommand,
                mod: "0775", files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                this, threads)()
    }


    void setScript(Object script) {
        this.script = script
    }

    Object getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
