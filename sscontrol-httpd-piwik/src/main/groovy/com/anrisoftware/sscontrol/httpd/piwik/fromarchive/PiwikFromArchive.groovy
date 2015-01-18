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
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory;
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * Installs and configures <i>Piwik</i> from an archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class PiwikFromArchive {

    @Inject
    private PiwikFromArchiveLogger logg

    @Inject
    UnpackFactory unpackFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    Object script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        unpackPiwikArchive domain, service
        savePiwikVersion domain, service
        setupPermissions domain, service
    }

    /**
     * Downloads and unpacks the <i>Piwik</i> source archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #getPiwikArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackPiwikArchive(Domain domain, PiwikService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(piwikArchive.path).name
        def dest = new File(tmpDirectory, "piwik-$name")
        copyURLToFile piwikArchive.toURL(), dest
        unpackArchive domain, service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>Piwik</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see PiwikService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, PiwikService service) {
        def override = service.overrideMode != OverrideMode.no || !piwikConfigExist(domain, service)
        def update = service.overrideMode != OverrideMode.update || !checkPiwikVersion(domain, service)
        override && update
    }

    /**
     * Unpacks the <i>Piwik</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WebService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = piwikDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: script.unpackCommands,
                this, threads)()

        logg.unpackArchiveDone this, piwikArchive
    }

    /**
     * Checks the installed <i>Piwik</i> service version.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkPiwikVersion(Domain domain, PiwikService service) {
        def file = piwikVersionFile domain, service
        if (!file.isFile()) {
            return false
        }
        def version = FileUtils.readFileToString file
        logg.checkPiwikVersion this, version, piwikExpectedVersion
        return StringUtils.startsWith(version, piwikExpectedVersion)
    }

    /**
     * Saves the installed <i>Piwik</i> version.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @see #piwikVersionFile(Domain, PiwikService)
     */
    void savePiwikVersion(Domain domain, PiwikService service) {
        def file = piwikVersionFile domain, service
        FileUtils.writeStringToFile file, "${piwikArchiveVersion}\n", charset
    }

    /**
     * Returns the <i>Piwik</i> version file.
     *
     * <ul>
     * <li>profile property {@code "piwik_version_file_name"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return the version {@code File} file.
     */
    File piwikVersionFile(Domain domain, PiwikService service) {
        def str = profileProperty "piwik_version_file_name", piwikProperties
        def dir = piwikDir domain, service
        new File(dir, str)
    }

    /**
     * Sets the owner of <i>Piwik</i> directory.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link PiwikService}.
     */
    void setupPermissions(Domain domain, PiwikService service) {
        def dir = piwikDir domain, service
        def user = domain.domainUser
        def tmpDir = new File(dir, piwikTempDirName)
        def configDir = new File(dir, piwikConfigDirName)
        changeFileOwnerFactory.create(
                log: log, command: script.chownCommand,
                owner: "root", ownerGroup: "root", recursive: true,
                files: dir,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log, command: script.chownCommand,
                owner: user.name, ownerGroup: user.group, recursive: true,
                files: [tmpDir, configDir],
                this, threads)()
    }

    /**
     * Returns the <i>Piwik</i> temp directory name property, for
     * example {@code "tmp".}
     *
     * <ul>
     * <li>profile property {@code "piwik_temp_directory_name"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, PiwikService)
     */
    String getPiwikTempDirName() {
        profileProperty "piwik_temp_directory_name", piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> configuration directory name property, for
     * example {@code "config".}
     *
     * <ul>
     * <li>profile property {@code "piwik_config_directory_name"}</li>
     * </ul>
     *
     * @see #gititDir(Domain, PiwikService)
     */
    String getPiwikConfigDirName() {
        profileProperty "piwik_config_directory_name", piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> archive, for example
     * {@code http://builds.piwik.org/piwik-2.3.0.tar.gz}
     *
     * <ul>
     * <li>profile property {@code "piwik_archive"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    URI getPiwikArchive() {
        profileURIProperty "piwik_archive", piwikProperties
    }

    /**
     * Returns the expected <i>Piwik</i> version, for
     * example {@code "2.3"}
     *
     * <ul>
     * <li>profile property {@code "piwik_expected_version"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikExpectedVersion() {
        profileProperty "piwik_expected_version", piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> version from the archive, for
     * example {@code "2.3.0"}
     *
     * <ul>
     * <li>profile property {@code "piwik_archive_version"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikArchiveVersion() {
        profileProperty "piwik_archive_version", piwikProperties
    }

    /**
     * Returns the default <i>hsenv</i> <i>Piwik</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getPiwikProperties()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * @see ServiceConfig#getName()
     */
    String getName() {
        script.getName()
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
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
