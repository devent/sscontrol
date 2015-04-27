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
package com.anrisoftware.sscontrol.httpd.frontaccounting.fromarchive

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
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Installs <i>FrontAccounting</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class FrontaccountingFromArchiveConfig {

    @Inject
    private FrontaccountingFromArchiveConfigLogger log

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
     * Deploys the <i>FrontAccounting</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     */
    void deployService(Domain domain, FrontaccountingService service) {
        unpackServiceArchive domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>FrontAccounting</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @see #getFrontaccountingArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackServiceArchive(Domain domain, FrontaccountingService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(frontaccountingArchive.path).name
        def dest = new File(tmpDirectory, "frontaccounting-$name")
        downloadArchive frontaccountingArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Saves the <i>FrontAccounting</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @see #yourlsVersionFile(Domain, FrontaccountingService)
     */
    void saveVersionFile(Domain domain, FrontaccountingService service) {
        def file = serviceVersionFile domain, service
        def version = versionFormatFactory.create().format(frontaccountingVersion)
        FileUtils.writeStringToFile file, version, charset
    }

    /**
     * Returns if it needed to download and unpack the <i>FrontAccounting</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see FrontaccountingService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, FrontaccountingService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(domain, service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkServiceVersion(domain, service, true)
            case OverrideMode.upgrade:
                return checkServiceVersion(domain, service, false)
        }
    }

    /**
     * Returns if the <i>FrontAccounting</i> service is already installed.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean serviceInstalled(Domain domain, FrontaccountingService service) {
        configurationFile(domain, service).exists()
    }

    /**
     * Downloads the <i>FrontAccounting</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @see #getFrontaccountingArchive()
     */
    void downloadArchive(URI archive, File dest, FrontaccountingService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, frontaccountingArchive)
        }
    }

    /**
     * Unpacks the <i>FrontAccounting</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, FrontaccountingService service, File archive) {
        def dir = frontaccountingDir domain, service
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
        log.unpackArchiveDone this, frontaccountingArchive
    }

    /**
     * Checks that the installed <i>FrontAccounting</i> version is older than the
     * archive version, that is, check
     * if {@code currentVersion >= archiveVersion <= upperLimit} if equals is set to true and
     * if {@code currentVersion > archiveVersion <= upperLimit} if equals is set to false.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkServiceVersion(Domain domain, FrontaccountingService service, boolean equals) {
        def versionFile = serviceVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, frontaccountingVersion, frontaccountingUpperVersion
            return frontaccountingVersion.compareTo(version) >= 0 && version.compareTo(frontaccountingUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, frontaccountingVersion, frontaccountingUpperVersion
            return frontaccountingVersion.compareTo(version) > 0 && version.compareTo(frontaccountingUpperVersion) <= 0
        }
    }

    /**
     * Checks the <i>FrontAccounting</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @see #getFrontaccountingArchiveHash()
     */
    boolean checkArchiveHash(File archive, FrontaccountingService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: frontaccountingArchiveHash)()
        return check.matching
    }

    /**
     * Returns the <i>FrontAccounting</i> archive resource.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_archive"}</li>
     * </ul>
     *
     * @see #getFrontaccountingFromArchiveProperties()
     */
    URI getFrontaccountingArchive() {
        profileURIProperty "frontaccounting_archive", frontaccountingFromArchiveProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> archive hash.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_archive_hash"}</li>
     * </ul>
     *
     * @see #getFrontaccountingFromArchiveProperties()
     */
    URI getFrontaccountingArchiveHash() {
        profileURIProperty "frontaccounting_archive_hash", frontaccountingFromArchiveProperties
    }

    /**
     * Returns to strip the <i>FrontAccounting</i> archive from the
     * container directory.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_strip_archive"}</li>
     * </ul>
     *
     * @see #getFrontaccountingFromArchiveProperties()
     */
    boolean getFrontaccountingStripArchive() {
        profileBooleanProperty "frontaccounting_strip_archive", frontaccountingFromArchiveProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_version"}</li>
     * </ul>
     *
     * @see #getFrontaccountingFromArchiveProperties()
     */
    Version getFrontaccountingVersion() {
        profileTypedProperty "frontaccounting_version", versionFormatFactory.create(), frontaccountingFromArchiveProperties
    }

    /**
     * Returns the upper <i>FrontAccounting</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_upper_version"}</li>
     * </ul>
     *
     * @see #getFrontaccountingFromArchiveProperties()
     */
    Version getFrontaccountingUpperVersion() {
        profileTypedProperty "frontaccounting_upper_version", versionFormatFactory.create(), frontaccountingFromArchiveProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> version file, for example
     * {@code "version.txt".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see FrontaccountingService#getPrefix()
     * @see #getFrontaccountingFromArchiveProperties()
     */
    File serviceVersionFile(Domain domain, FrontaccountingService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "frontaccounting_version_file", dir, frontaccountingFromArchiveProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFrontaccountingFromArchiveProperties()

    /**
     * Returns the <i>FrontAccounting</i> service name.
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
