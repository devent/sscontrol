/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.fromarchive

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Downloads and deploys <i>FUDForum</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class FudforumFromArchiveConfig {

    @Inject
    private FudforumFromArchiveConfigLogger log

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
     * Deploys the <i>FUDForum</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void deployService(Domain domain, FudforumService service) {
        unpackFudforumArchive domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>FUDForum</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #getFudforumArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackFudforumArchive(Domain domain, FudforumService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(fudforumArchive.path).name
        def dest = new File(tmpDirectory, "fudforum-$name")
        downloadArchive fudforumArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Saves the <i>FUDForum</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #fudforumVersionFile(Domain, FudforumService)
     */
    void saveVersionFile(Domain domain, FudforumService service) {
        def file = fudforumVersionFile domain, service
        def version = versionFormatFactory.create().format(fudforumVersion)
        FileUtils.writeStringToFile file, version, charset
    }

    /**
     * Returns if it needed to download and unpack the <i>FUDForum</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see FudforumService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, FudforumService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(domain, service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkFudforumVersion(domain, service, true)
            case OverrideMode.upgrade:
                return checkFudforumVersion(domain, service, false)
        }
    }

    /**
     * Returns if the <i>FUDForum</i> service is already installed.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean isServiceInstalled(Domain domain, FudforumService service) {
        fudforumInstallFile(domain, service).exists()
    }

    /**
     * Returns if the <i>FUDForum</i> service downloaded and unpackaged.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return {@code true} if the service is deployed.
     */
    boolean isServiceDeployed(Domain domain, FudforumService service) {
        fudforumArchiveFile(domain, service).isFile()
    }

    /**
     * Downloads the <i>FUDForum</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #getFudforumArchive()
     */
    void downloadArchive(URI archive, File dest, FudforumService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, fudforumArchive)
        }
    }

    /**
     * Unpacks the <i>FUDForum</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, FudforumService service, File archive) {
        def dir = fudforumDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log.log,
                runCommands: runCommands,
                file: archive,
                output: dir,
                override: true,
                strip: stripArchive,
                stripDirectory: stripDirectory,
                commands: unpackCommands,
                this, threads)()
        log.unpackArchiveDone this, fudforumArchive
    }

    /**
     * Checks that the installed <i>FUDForum</i> version is older than the
     * archive version, that is, check
     * if {@code currentVersion >= archiveVersion <= upperLimit} if equals is set to true and
     * if {@code currentVersion > archiveVersion <= upperLimit} if equals is set to false.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkFudforumVersion(Domain domain, FudforumService service, boolean equals) {
        def versionFile = fudforumVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, fudforumVersion, fudforumUpperVersion
            return fudforumVersion.compareTo(version) >= 0 && version.compareTo(fudforumUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, fudforumVersion, fudforumUpperVersion
            return fudforumVersion.compareTo(version) > 0 && version.compareTo(fudforumUpperVersion) <= 0
        }
    }

    /**
     * Checks the <i>FUDForum</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @see #getFudforumArchiveHash()
     */
    boolean checkArchiveHash(File archive, FudforumService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: fudforumArchiveHash)()
        return check.matching
    }

    /**
     * Returns the <i>FUDForum</i> archive resource.
     *
     * <ul>
     * <li>profile property {@code "fudforum_archive"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    URI getFudforumArchive() {
        profileURIProperty "fudforum_archive", fudforumFromArchiveProperties
    }

    /**
     * Returns the <i>FUDForum</i> archive hash.
     *
     * <ul>
     * <li>profile property {@code "fudforum_archive_hash"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    URI getFudforumArchiveHash() {
        profileURIProperty "fudforum_archive_hash", fudforumFromArchiveProperties
    }

    /**
     * Returns to strip the <i>FUDForum</i> archive from the
     * container directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_strip_archive"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    boolean getStripArchive() {
        profileBooleanProperty "fudforum_strip_archive", fudforumFromArchiveProperties
    }

    /**
     * Returns the directory name of the <i>FUDForum</i> archive that
     * should be stripped.
     *
     * <ul>
     * <li>profile property {@code "fudforum_strip_directory"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    String getStripDirectory() {
        def value = profileProperty "fudforum_strip_directory", fudforumFromArchiveProperties
        return StringUtils.isBlank(value) ? null : value
    }

    /**
     * Returns the <i>FUDForum</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "fudforum_version"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    Version getFudforumVersion() {
        profileTypedProperty "fudforum_version", versionFormatFactory.create(), fudforumFromArchiveProperties
    }

    /**
     * Returns the upper <i>FUDForum</i> version, for
     * example {@code "1.7"}
     *
     * <ul>
     * <li>profile property {@code "fudforum_upper_version"}</li>
     * </ul>
     *
     * @see #getFudforumFromArchiveProperties()
     */
    Version getFudforumUpperVersion() {
        profileTypedProperty "fudforum_upper_version", versionFormatFactory.create(), fudforumFromArchiveProperties
    }

    /**
     * Returns the <i>FUDForum</i> version file, for example
     * {@code "version.txt".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see FudforumService#getPrefix()
     * @see #getFudforumFromArchiveProperties()
     */
    File fudforumVersionFile(Domain domain, FudforumService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "fudforum_version_file", dir, fudforumFromArchiveProperties
    }

    /**
     * Returns the <i>fudforum_archive</i> script. If the file is not absolute,
     * then the file is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_archive_file"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    File fudforumArchiveFile(Domain domain, FudforumService service) {
        profileFileProperty "fudforum_archive_file", fudforumDir(domain, service), fudforumFromArchiveProperties
    }

    /**
     * Returns the <i>FUDForum</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFudforumFromArchiveProperties()

    /**
     * Returns the <i>FUDForum</i> service name.
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
