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

import org.apache.commons.io.FileUtils
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

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

    private Object script

    @Inject
    CheckFileHashFactory checkFileHashFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    UnpackFactory unpackFactory

    @Inject
    VersionFormatFactory versionFormatFactory

    @Inject
    CheckVersionLimitFactory checkVersionLimitFactory

    @Inject
    ReadVersionFactory readVersionFactory

    /**
     * Downloads and unpacks the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WebService} service.
     */
    void downloadArchive(Domain domain, WebService service) {
        def currentVersion = wordpressCurrentVersion domain, service
        if (!checkWordpressVersion(domain, service, currentVersion)) {
            return
        }
        if (!canUnpackArchive(domain, service)) {
            return
        }
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-$name")
        if (needDownloadArchive(dest)) {
            downloadArchive dest
        }
        unpackArchive domain, service, dest
        FileUtils.write wordpressVersionFile(domain, service), versionFormatFactory.create().format(currentVersion), charset
    }

    /**
     * @see CheckVersionLimit
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param currentVersion
     *            the current {@link Version} version.
     */
    boolean checkWordpressVersion(Domain domain, WebService service, Version currentVersion) {
        if (currentVersion != null) {
            return checkVersionLimitFactory.create(currentVersion, wordpressArchiveVersion, wordpressVersionLimit).checkVersion()
        } else {
            return true
        }
    }

    /**
     * Returns the current <i>Wordpress</i> version.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return the {@link Version} or {@code null}.
     */
    Version wordpressCurrentVersion(Domain domain, WebService service) {
        def versionFile = wordpressVersionFile(domain, service)
        if (versionFile.exists()) {
            return readVersionFactory.create(versionFile.toURI(), charset).readVersion()
        } else {
            return null
        }
    }

    /**
     * Allowed to unpack the archive if the <i>Roundcube</i> is already
     * installed and the override mode is not {@link OverrideMode#no}.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return {@code true} if it is allowed to unpack and therefore to
     * override an already existing installation.
     *
     * @see WordpressService#getOverrideMode()
     */
    boolean canUnpackArchive(Domain domain, WordpressService service) {
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
        if (wordpressArchiveHash != null) {
            def check = checkFileHashFactory.create(this, file: dest, hash: wordpressArchiveHash)()
            logg.checkHashArchive script.script, wordpressArchive, wordpressArchiveHash, check.matching
        }
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
                log: log,
                file: archive,
                output: dir,
                override: true,
                strip: strip,
                commands: unpackCommands,
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
                log: log,
                command: chownCommand,
                owner: "root",
                ownerGroup: user.group,
                files: conffile,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "0440",
                files: conffile,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                recursive: true,
                owner: user.name,
                ownerGroup: user.group,
                files: [
                    cachedir,
                    uploadsdir,
                ],
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "0775",
                files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                this, threads)()
    }


    /**
     * Returns if the <i>Wordpress</i> version limit, for example {@code "3"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_version_limit"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    Version getWordpressVersionLimit() {
        def p = profileProperty "wordpress_version_limit", wordpressProperties
        versionFormatFactory.create().parse p
    }

    /**
     * Returns if the <i>Wordpress</i> version of the archive, for example
     * {@code "3.9.2"}
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive_version"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    Version getWordpressArchiveVersion() {
        def p = profileProperty "wordpress_archive_version", wordpressProperties
        versionFormatFactory.create().parse p
    }

    /**
     * Returns the <i>Wordpress</i> version file.
     * The placeholder variable are replaced:
     * <ul>
     * <li>"&lt;domainDir>" with the directory of the domain;</li>
     * <li>"&lt;prefix>" with the directory of the service prefix;</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return the version {@link File} file.
     *
     * @see #domainDir(Domain)
     * @see WebService#getPrefix()
     */
    File wordpressVersionFile(Domain domain, WebService service) {
        def name = new ST(wordpressVersionFilePath).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns <i>Wordpress</i> configuration file, for
     * example {@code "<domainDir>/<prefix>/version.txt"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_version_file"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getWordpressVersionFilePath() {
        profileProperty "wordpress_version_file", wordpressProperties
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
