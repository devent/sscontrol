/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.fromarchive

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * <i>Piwik</i> from an archive configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class PiwikFromArchive {

    private Object script

    @Inject
    private PiwikFromArchiveLogger logg

    @Inject
    UnpackFactory unpackFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

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
     *            the {@link PiwikService} service.
     */
    void deployService(Domain domain, PiwikService service) {
        unpackPiwikArchive domain, service
        savePiwikVersion domain, service
        setupPermissions domain, service
    }

    /**
     * Downloads and unpacks the <i>Piwik</i> source archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #getPiwikArchive()
     */
    void unpackPiwikArchive(Domain domain, PiwikService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(piwikArchive.path).name
        def dest = new File(tmpDirectory, "piwik-$name")
        downloadArchive piwikArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>Piwik</i> archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see PiwikService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, PiwikService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return piwikConfigFile(domain, service).exists()
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkPiwikVersion(domain, service) == false
        }
    }

    /**
     * Downloads the <i>Piwik</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #getPiwikArchive()
     */
    void downloadArchive(URI archive, File dest, PiwikService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw logg.errorArchiveHash(service, piwikArchive)
        }
    }

    /**
     * Checks the <i>Piwik</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #getPiwikArchiveHash()
     */
    boolean checkArchiveHash(File archive, PiwikService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: piwikArchiveHash)()
        return check.matching
    }

    /**
     * Unpacks the <i>Piwik</i> archive.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = piwikDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log,
                runCommands: runCommands,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: unpackCommands,
                this, threads)()

        logg.unpackArchiveDone this, piwikArchive
    }

    /**
     * Checks the installed <i>Piwik</i> service version.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkPiwikVersion(Domain domain, PiwikService service) {
        def versionFile = piwikVersionFile domain, service
        if (!versionFile.isFile()) {
            return false
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        logg.checkPiwikVersion this, version, piwikUpperVersion
        version.compareTo(piwikUpperVersion) <= 0
    }

    /**
     * Saves the installed <i>Piwik</i> version.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #piwikVersionFile(Domain, PiwikService)
     */
    void savePiwikVersion(Domain domain, PiwikService service) {
        def file = piwikVersionFile domain, service
        FileUtils.writeStringToFile file, piwikVersion, charset
    }

    /**
     * Sets the owner of <i>Piwik</i> directory.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupPermissions(Domain domain, PiwikService service) {
        def user = domain.domainUser
        def dir = piwikDir(domain, service)
        def tmpDir = piwikTempDirectory domain, service
        def configDir = piwikConfigDirectory domain, service
        tmpDir.mkdirs()
        configDir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: "root",
                ownerGroup: "root",
                recursive: true,
                files: dir,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                recursive: true,
                files: [tmpDir, configDir],
                this, threads)()
    }

    /**
     * Returns the <i>Piwik</i> temp directory, for
     * example {@code "tmp".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "piwik_temp_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #piwikDir(Domain, PiwikService)
     */
    File piwikTempDirectory(Domain domain, PiwikService service) {
        profileFileProperty "piwik_temp_directory", piwikDir(domain, service), piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> configuration directory, for
     * example {@code "config".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "piwik_config_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #piwikDir(Domain, PiwikService)
     */
    File piwikConfigDirectory(Domain domain, PiwikService service) {
        profileFileProperty "piwik_config_directory", piwikDir(domain, service), piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> archive, for example
     * {@code "http://builds.piwik.org/piwik-2.10.0.tar.gz"}
     *
     * <ul>
     * <li>profile property {@code "piwik_archive"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    URI getPiwikArchive() {
        profileURIProperty "piwik_archive", piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> version, for
     * example {@code "2.10.0"}
     *
     * <ul>
     * <li>profile property {@code "piwik_version"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikVersion() {
        profileProperty "piwik_version", piwikArchiveProperties
    }

    /**
     * Returns the upper <i>Piwik</i> version, for
     * example {@code "2.10"}
     *
     * <ul>
     * <li>profile property {@code "piwik_upper_version"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    Version getPiwikUpperVersion() {
        profileTypedProperty "piwik_upper_version", versionFormatFactory.create(), piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> archive hash, for
     * example {@code "md5:aa92904a7bca24fe20b3fd000e99291b"}
     *
     * <ul>
     * <li>profile property {@code "piwik_archive_hash"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    URI getPiwikArchiveHash() {
        profileURIProperty "piwik_archive_hash", piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> version file, for example
     * {@code "/var/www/domain.com/piwikprefix/version.txt".}
     * If the path is not absolute, the path is assumed to be under the
     * service installation directory.
     *
     * <ul>
     * <li>profile property {@code "piwik_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return the version {@code File} file.
     *
     * @see #getPiwikProperties()
     */
    File piwikVersionFile(Domain domain, PiwikService service) {
        profileFileProperty "piwik_version_file", piwikDir(domain, service), piwikArchiveProperties
    }

    /**
     * Returns the <i>Piwik</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getPiwikArchiveProperties()

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
