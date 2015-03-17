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
package com.anrisoftware.sscontrol.httpd.owncloud.fromarchive

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
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * <i>ownCloud</i> from an archive configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class OwncloudFromArchive {

    private Object script

    @Inject
    private OwncloudFromArchiveLogger log

    @Inject
    UnpackFactory unpackFactory

    @Inject
    CheckFileHashFactory checkFileHashFactory

    @Inject
    VersionFormatFactory versionFormatFactory

    /**
     * Deploys the configuration.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void deployService(Domain domain, OwncloudService service) {
        unpackOwncloudArchive domain, service
        saveOwncloudVersion domain, service
    }

    /**
     * Downloads and unpacks the <i>ownCloud</i> source archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getPiwikArchive()
     */
    void unpackOwncloudArchive(Domain domain, OwncloudService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(owncloudArchive.path).name
        def dest = new File(tmpDirectory, "owncloud-$name")
        downloadArchive owncloudArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>ownCloud</i> archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see OwncloudService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, OwncloudService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(domain, service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkOwncloudVersion(domain, service, true)
            case OverrideMode.upgrade:
                return checkOwncloudVersion(domain, service, false)
        }
    }

    /**
     * Returns if the <i>ownCloud</i> service is already installed.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean serviceInstalled(Domain domain, OwncloudService service) {
        owncloudConfigFile(domain, service).exists()
    }

    /**
     * Downloads the <i>ownCloud</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getPiwikArchive()
     */
    void downloadArchive(URI archive, File dest, OwncloudService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, owncloudArchive)
        }
    }

    /**
     * Checks the <i>ownCloud</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getPiwikArchiveHash()
     */
    boolean checkArchiveHash(File archive, OwncloudService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: owncloudArchiveHash)()
        return check.matching
    }

    /**
     * Unpacks the <i>ownCloud</i> archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = owncloudDir domain, service
        dir.mkdirs()
        unpackFactory.create(
                log: log.log,
                runCommands: runCommands,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: unpackCommands,
                this, threads)()

        log.unpackArchiveDone this, owncloudArchive
    }

    /**
     * Checks the installed <i>ownCloud</i> service version.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkOwncloudVersion(Domain domain, OwncloudService service, boolean equals) {
        def versionFile = owncloudVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, owncloudVersion, owncloudUpperVersion
            return owncloudVersion.compareTo(version) >= 0 && version.compareTo(owncloudUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, owncloudVersion, owncloudUpperVersion
            return owncloudVersion.compareTo(version) > 0 && version.compareTo(owncloudUpperVersion) <= 0
        }
    }

    /**
     * Saves the installed <i>ownCloud</i> version.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #piwikVersionFile(Domain, OwncloudService)
     */
    void saveOwncloudVersion(Domain domain, OwncloudService service) {
        def file = owncloudVersionFile domain, service
        def string = versionFormatFactory.create().format owncloudVersion
        FileUtils.writeStringToFile file, string, charset
    }

    /**
     * Returns the <i>ownCloud</i> archive, for example
     * {@code "https://download.owncloud.org/community/owncloud-7.0.4.tar.bz2"}
     *
     * <ul>
     * <li>profile property {@code "owncloud_archive"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    URI getOwncloudArchive() {
        profileURIProperty "owncloud_archive", owncloudArchiveProperties
    }

    /**
     * Returns the <i>ownCloud</i> version, for
     * example {@code "7.0.4"}
     *
     * <ul>
     * <li>profile property {@code "owncloud_version"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    Version getOwncloudVersion() {
        profileTypedProperty "owncloud_version", versionFormatFactory.create(), owncloudArchiveProperties
    }

    /**
     * Returns the upper <i>ownCloud</i> version, for
     * example {@code "7"}
     *
     * <ul>
     * <li>profile property {@code "owncloud_upper_version"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    Version getOwncloudUpperVersion() {
        profileTypedProperty "owncloud_upper_version", versionFormatFactory.create(), owncloudArchiveProperties
    }

    /**
     * Returns the <i>ownCloud</i> archive hash, for
     * example {@code "https://download.owncloud.org/community/owncloud-7.0.4.tar.bz2.md5"}
     *
     * <ul>
     * <li>profile property {@code "owncloud_archive_hash"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    URI getOwncloudArchiveHash() {
        profileURIProperty "owncloud_archive_hash", owncloudArchiveProperties
    }

    /**
     * Returns the <i>ownCloud</i> version file, for example
     * {@code "version.txt".}
     * If the path is not absolute, the path is assumed to be under the
     * service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the version {@code File} file.
     *
     * @see #getOwncloudProperties()
     */
    File owncloudVersionFile(Domain domain, OwncloudService service) {
        profileFileProperty "owncloud_version_file", owncloudDir(domain, service), owncloudArchiveProperties
    }

    /**
     * Returns the <i>ownCloud</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getOwncloudArchiveProperties()

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
     * Returns the service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the service profile name.
     */
    String getProfile() {
        script.getProfile()
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
    String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
