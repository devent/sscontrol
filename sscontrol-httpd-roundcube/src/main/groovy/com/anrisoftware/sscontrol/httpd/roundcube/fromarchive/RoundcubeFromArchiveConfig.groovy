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

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Installs <i>Roundcube</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class RoundcubeFromArchiveConfig {

    @Inject
    private RoundcubeFromArchiveConfigLogger log

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
     * Deploys the <i>Roundcube</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void deployService(Domain domain, RoundcubeService service) {
        unpackRoundcubeArchive domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>Roundcube</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackRoundcubeArchive(Domain domain, RoundcubeService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(roundcubeArchive.path).name
        def dest = new File(tmpDirectory, "roundcube-$name")
        downloadArchive roundcubeArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Saves the <i>Roundcube</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #roundcubeVersionFile(Domain, RoundcubeService)
     */
    void saveVersionFile(Domain domain, RoundcubeService service) {
        def file = roundcubeVersionFile domain, service
        def version = versionFormatFactory.create().format(roundcubeVersion)
        FileUtils.writeStringToFile file, version, charset
    }

    /**
     * Returns if it needed to download and unpack the <i>Roundcube</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see RoundcubeService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, RoundcubeService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(domain, service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkRoundcubeVersion(domain, service, true)
            case OverrideMode.upgrade:
                return checkRoundcubeVersion(domain, service, false)
        }
    }

    /**
     * Returns if the <i>Roundcube</i> service is already installed.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean serviceInstalled(Domain domain, RoundcubeService service) {
        configurationFile(domain, service).exists()
    }

    /**
     * Downloads the <i>Roundcube</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeArchive()
     */
    void downloadArchive(URI archive, File dest, RoundcubeService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, roundcubeArchive)
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
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log.log,
                runCommands: runCommands,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: unpackCommands,
                this, threads)()
        log.unpackArchiveDone this, roundcubeArchive
    }

    /**
     * Checks that the installed <i>Roundcube</i> version is older than the
     * archive version, that is, check
     * if {@code currentVersion >= archiveVersion <= upperLimit} if equals is set to true and
     * if {@code currentVersion > archiveVersion <= upperLimit} if equals is set to false.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkRoundcubeVersion(Domain domain, RoundcubeService service, boolean equals) {
        def versionFile = roundcubeVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, roundcubeVersion, roundcubeUpperVersion
            return roundcubeVersion.compareTo(version) >= 0 && version.compareTo(roundcubeUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, roundcubeVersion, roundcubeUpperVersion
            return roundcubeVersion.compareTo(version) > 0 && version.compareTo(roundcubeUpperVersion) <= 0
        }
    }

    /**
     * Checks the <i>Roundcube</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #getRoundcubeArchiveHash()
     */
    boolean checkArchiveHash(File archive, RoundcubeService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: roundcubeArchiveHash)()
        return check.matching
    }

    /**
     * Returns the <i>Roundcube</i> archive resource.
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive"}</li>
     * </ul>
     *
     * @see #getRoundcubeFromArchiveProperties()
     */
    URI getRoundcubeArchive() {
        profileURIProperty "roundcube_archive", roundcubeFromArchiveProperties
    }

    /**
     * Returns the <i>Roundcube</i> archive hash.
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive_hash"}</li>
     * </ul>
     *
     * @see #getRoundcubeFromArchiveProperties()
     */
    URI getRoundcubeArchiveHash() {
        profileURIProperty "roundcube_archive_hash", roundcubeFromArchiveProperties
    }

    /**
     * Returns to strip the <i>Roundcube</i> archive from the
     * container directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_strip_archive"}</li>
     * </ul>
     *
     * @see #getRoundcubeFromArchiveProperties()
     */
    boolean getStripArchive() {
        profileBooleanProperty "roundcube_strip_archive", roundcubeFromArchiveProperties
    }

    /**
     * Returns the <i>Roundcube</i> version, for
     * example {@code "1.1.0"}
     *
     * <ul>
     * <li>profile property {@code "roundcube_version"}</li>
     * </ul>
     *
     * @see #getRoundcubeFromArchiveProperties()
     */
    Version getRoundcubeVersion() {
        profileTypedProperty "roundcube_version", versionFormatFactory.create(), roundcubeFromArchiveProperties
    }

    /**
     * Returns the upper <i>Roundcube</i> version, for
     * example {@code "1"}
     *
     * <ul>
     * <li>profile property {@code "roundcube_upper_version"}</li>
     * </ul>
     *
     * @see #getRoundcubeFromArchiveProperties()
     */
    Version getRoundcubeUpperVersion() {
        profileTypedProperty "roundcube_upper_version", versionFormatFactory.create(), roundcubeFromArchiveProperties
    }

    /**
     * Returns the <i>Roundcube</i> version file, for example
     * {@code "version.txt".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     * @see #getRoundcubeFromArchiveProperties()
     */
    File roundcubeVersionFile(Domain domain, RoundcubeService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "roundcube_version_file", dir, roundcubeFromArchiveProperties
    }

    /**
     * Returns the <i>Roundcube</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRoundcubeFromArchiveProperties()

    /**
     * Returns the <i>Roundcube</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
