/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.fromarchive

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Installs <i>Roundcube</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class RoundcubeFromArchiveConfig {

    @Inject
    private RoundcubeFromArchiveConfigLogger logg

    @Inject
    private CheckFileHashFactory checkFileHashFactory

    private Object script

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
     * Downloads and unpacks the <i>Roundcube</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void downloadArchive(Domain domain, RoundcubeService service) {
        def currentVersion = currentRoudcubeVersion domain, service
        if (!checkRoundcubeVersion(domain, service, currentVersion)) {
            return
        }
        if (!canUnpackArchive(domain, service)) {
            return
        }
        def name = new File(roundcubeArchive.path).name
        def dest = new File(tmpDirectory, "roundcube_1_0_$name")
        if (needDownloadArchive(dest)) {
            downloadArchive(dest)
        }
        unpackArchive domain, service, dest
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
     * @see RoundcubeService#getOverrideMode()
     */
    boolean canUnpackArchive(Domain domain, RoundcubeService service) {
        service.overrideMode != OverrideMode.no || !configurationFile(domain, service).isFile()
    }

    /**
     * Returns if it needed to download the <i>Roundcube</i> archive.
     *
     * @param dest
     *            the archive {@link File} destination.
     *
     * @return {@code true} if it is needed.
     */
    boolean needDownloadArchive(File dest) {
        logg.checkNeedDownloadArchive script.script, dest, roundcubeArchiveHash
        if (dest.isFile() && roundcubeArchiveHash != null) {
            def check = checkFileHashFactory.create(this, file: dest, hash: roundcubeArchiveHash)()
            return !check.matching
        } else {
            return true
        }
    }

    /**
     * Downloads the <i>Roundcube</i> archive.
     *
     * @param dest
     *            the {@link File} destination.
     */
    void downloadArchive(File dest) {
        logg.startDownload script.script, roundcubeArchive, dest
        copyURLToFile(roundcubeArchive.toURL(), dest)
        if (roundcubeArchiveHash != null) {
            def check = checkFileHashFactory.create(this, file: dest, hash: roundcubeArchiveHash)()
            logg.checkHashArchive script.script, roundcubeArchive, roundcubeArchiveHash, check.matching
        }
        logg.finishDownload script.script, roundcubeArchive, dest
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
    boolean checkRoundcubeVersion(Domain domain, WebService service, Version currentVersion) {
        if (currentVersion != null) {
            return checkVersionLimitFactory.create(currentVersion, roundcubeArchiveVersion, roundcubeVersionLimit).checkVersion()
        } else {
            return true
        }
    }

    /**
     * Unpacks the <i>Roundcube</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, RoundcubeService service, File archive) {
        def dir = roundcubeDir domain, service
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
        logg.downloadArchive script.script, roundcubeArchive
    }

    /**
     * Sets the owner and permissions of the <i>Roundcube</i> service.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupPermissions(Domain domain, RoundcubeService service) {
        def dir = roundcubeDir domain, service
        def user = domain.domainUser
        def conffile = configurationFile domain, service
        File logDir = roundcubeLogDirectory domain, service
        File tempDir = roundcubeTempDirectory domain, service
        logDir.isDirectory() == false ? logDir.mkdirs() : false
        tempDir.isDirectory() == false ? tempDir.mkdirs() : false
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                owner: "root",
                ownerGroup: user.group,
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "u=rwX",
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "g=rX",
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "o=rX",
                files: dir,
                recursive: true,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: [
                    logDir,
                    tempDir,
                ],
                this, threads)()
        changeFileModFactory.create(
                log: log,
                command: chmodCommand,
                mod: "0775",
                files: [
                    logDir,
                    tempDir,
                ],
                this, threads)()
    }

    /**
     * Returns the <i>Roundcube</i> version limit, for example
     * {@code "1.0"}
     *
     * <ul>
     * <li>profile property {@code "roundcube_version_limit"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    Version getRoundcubeVersionLimit() {
        def p = profileProperty "roundcube_version_limit", roundcubeProperties
        versionFormatFactory.create().parse p
    }

    /**
     * Returns if the <i>Roundcube</i> version of the archive, for example
     * {@code "1.0.3"}
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive_version"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    Version getRoundcubeArchiveVersion() {
        def p = profileProperty "roundcube_archive_version", roundcubeProperties
        versionFormatFactory.create().parse p
    }

    /**
     * Returns the current <i>Roundcube</i> version.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @return the {@link Version} or {@code null}.
     */
    Version currentRoudcubeVersion(Domain domain, WebService service) {
        def versionFile = roundcubeVersionFile(domain, service)
        if (versionFile.exists()) {
            return readVersionFactory.create(versionFile.toURI(), charset).readVersion()
        } else {
            return null
        }
    }

    /**
     * Returns the <i>Roundcube</i> version file.
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
    File roundcubeVersionFile(Domain domain, WebService service) {
        def name = new ST(roundcubeVersionFilePath).
                add("domainDir", domainDir(domain)).
                add("prefix", service.prefix).
                render()
        new File(name)
    }

    /**
     * Returns <i>Roundcube</i> configuration file, for
     * example {@code "<domainDir>/<prefix>/version.txt"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_version_file"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeVersionFilePath() {
        profileProperty "roundcube_version_file", roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> archive resource, for
     * example {@code "http://downloads.sourceforge.net/project/roundcubemail/roundcubemail/1.0.3/roundcubemail-1.0.3.tar.gz"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    URI getRoundcubeArchive() {
        profileURIProperty "roundcube_archive", roundcubeProperties
    }

    /**
     * Returns <i>Roundcube</i> archive hash, for
     * example {@code "md5:e35652adea5cd4069fcaa1410ae58864"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive_hash"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    URI getRoundcubeArchiveHash() {
        profileURIProperty "roundcube_archive_hash", roundcubeProperties
    }

    /**
     * Returns if it is needed to remove the root directory from the archive, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_strip_archive"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getStripArchive() {
        profileBooleanProperty "roundcube_strip_archive", roundcubeProperties
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
