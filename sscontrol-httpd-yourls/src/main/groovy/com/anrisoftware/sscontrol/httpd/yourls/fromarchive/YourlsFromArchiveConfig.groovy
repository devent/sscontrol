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
package com.anrisoftware.sscontrol.httpd.yourls.fromarchive

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.yourls.YourlsService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Installs <i>Yourls</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class YourlsFromArchiveConfig {

    @Inject
    private YourlsFromArchiveConfigLogger log

    @Inject
    private CheckFileHashFactory checkFileHashFactory

    private Object script

    @Inject
    UnpackFactory unpackFactory

    @Inject
    VersionFormatFactory versionFormatFactory

    @Inject
    CheckVersionLimitFactory checkVersionLimitFactory

    @Inject
    ReadVersionFactory readVersionFactory

    /**
     * Deploys the <i>Yourls</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void deployService(Domain domain, YourlsService service) {
        unpackYourlsArchive domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>Yourls</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @see #getYourlsArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackYourlsArchive(Domain domain, YourlsService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(yourlsArchive.path).name
        def dest = new File(tmpDirectory, "yourls-$name")
        downloadArchive yourlsArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Saves the <i>Yourls</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @see #yourlsVersionFile(Domain, YourlsService)
     */
    void saveVersionFile(Domain domain, YourlsService service) {
        def file = yourlsVersionFile domain, service
        def version = versionFormatFactory.create().format(yourlsVersion)
        FileUtils.writeStringToFile file, version, charset
    }

    /**
     * Returns if it needed to download and unpack the <i>Yourls</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see YourlsService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, YourlsService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(domain, service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkYourlsVersion(domain, service, true)
            case OverrideMode.upgrade:
                return checkYourlsVersion(domain, service, false)
        }
    }

    /**
     * Returns if the <i>Yourls</i> service is already installed.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean serviceInstalled(Domain domain, YourlsService service) {
        configurationFile(domain, service).exists()
    }

    /**
     * Downloads the <i>Yourls</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @see #getYourlsArchive()
     */
    void downloadArchive(URI archive, File dest, YourlsService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, yourlsArchive)
        }
    }

    /**
     * Unpacks the <i>Yourls</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, YourlsService service, File archive) {
        def dir = yourlsDir domain, service
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
        log.unpackArchiveDone this, yourlsArchive
    }

    /**
     * Checks that the installed <i>Yourls</i> version is older than the
     * archive version, that is, check
     * if {@code currentVersion >= archiveVersion <= upperLimit} if equals is set to true and
     * if {@code currentVersion > archiveVersion <= upperLimit} if equals is set to false.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkYourlsVersion(Domain domain, YourlsService service, boolean equals) {
        def versionFile = yourlsVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, yourlsVersion, yourlsUpperVersion
            return yourlsVersion.compareTo(version) >= 0 && version.compareTo(yourlsUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, yourlsVersion, yourlsUpperVersion
            return yourlsVersion.compareTo(version) > 0 && version.compareTo(yourlsUpperVersion) <= 0
        }
    }

    /**
     * Checks the <i>Yourls</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @see #getYourlsArchiveHash()
     */
    boolean checkArchiveHash(File archive, YourlsService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: yourlsArchiveHash)()
        return check.matching
    }

    /**
     * Returns the <i>Yourls</i> archive resource.
     *
     * <ul>
     * <li>profile property {@code "yourls_archive"}</li>
     * </ul>
     *
     * @see #getYourlsFromArchiveProperties()
     */
    URI getYourlsArchive() {
        profileURIProperty "yourls_archive", yourlsFromArchiveProperties
    }

    /**
     * Returns the <i>Yourls</i> archive hash.
     *
     * <ul>
     * <li>profile property {@code "yourls_archive_hash"}</li>
     * </ul>
     *
     * @see #getYourlsFromArchiveProperties()
     */
    URI getYourlsArchiveHash() {
        profileURIProperty "yourls_archive_hash", yourlsFromArchiveProperties
    }

    /**
     * Returns to strip the <i>Yourls</i> archive from the
     * container directory.
     *
     * <ul>
     * <li>profile property {@code "yourls_strip_archive"}</li>
     * </ul>
     *
     * @see #getYourlsFromArchiveProperties()
     */
    boolean getStripArchive() {
        profileBooleanProperty "yourls_strip_archive", yourlsFromArchiveProperties
    }

    /**
     * Returns the <i>Yourls</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "yourls_version"}</li>
     * </ul>
     *
     * @see #getYourlsFromArchiveProperties()
     */
    Version getYourlsVersion() {
        profileTypedProperty "yourls_version", versionFormatFactory.create(), yourlsFromArchiveProperties
    }

    /**
     * Returns the upper <i>Yourls</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "yourls_upper_version"}</li>
     * </ul>
     *
     * @see #getYourlsFromArchiveProperties()
     */
    Version getYourlsUpperVersion() {
        profileTypedProperty "yourls_upper_version", versionFormatFactory.create(), yourlsFromArchiveProperties
    }

    /**
     * Returns the <i>Yourls</i> version file, for example
     * {@code "version.txt".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "yourls_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see YourlsService#getPrefix()
     * @see #getYourlsFromArchiveProperties()
     */
    File yourlsVersionFile(Domain domain, YourlsService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "yourls_version_file", dir, yourlsFromArchiveProperties
    }

    /**
     * Returns the <i>Yourls</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getYourlsFromArchiveProperties()

    /**
     * Returns the <i>Yourls</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

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
