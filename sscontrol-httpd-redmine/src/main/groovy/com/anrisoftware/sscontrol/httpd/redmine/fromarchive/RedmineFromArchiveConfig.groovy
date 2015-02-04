/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.fromarchive

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
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04.RedmineConfigFactory;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * <i>Redmine</i> from archive configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class RedmineFromArchiveConfig {

    @Inject
    private RedmineFromArchiveLogger logg

    private Object script

    @Inject
    UnpackFactory unpackFactory

    @Inject
    CheckFileHashFactory checkFileHashFactory

    @Inject
    VersionFormatFactory versionFormatFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Deploys the <i>Redmine</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link RedmineService} service.
     */
    void deployService(Domain domain, RedmineService service) {
        unpackRedmineArchive domain, service
        setupPermissions domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>Redmine</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackRedmineArchive(Domain domain, RedmineService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(redmineArchive.path).name
        def dest = new File(tmpDirectory, "redmine-$name")
        downloadArchive redmineArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Sets the permissions of the <i>Redmine</i> directories.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link RedmineService}.
     */
    void setupPermissions(Domain domain, RedmineService service) {
        def dir = redmineDir domain, service
        changeFileOwnerFactory.create(
                log: log,
                files: dir,
                recursive: true,
                command: script.chownCommand,
                owner: "root", ownerGroup: "root",
                this, threads)()
    }

    /**
     * Saves the <i>Redmine</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #redmineVersionFile(Domain, RedmineService)
     */
    void saveVersionFile(Domain domain, RedmineService service) {
        def file = redmineVersionFile domain, service
        FileUtils.writeStringToFile file, redmineVersion, charset
    }

    /**
     * Downloads the <i>Redmine</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineArchive()
     */
    void downloadArchive(URI archive, File dest, RedmineService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw logg.errorArchiveHash(service, redmineArchive)
        }
    }

    /**
     * Checks the <i>Redmine</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @see #getRedmineArchiveHash()
     */
    boolean checkArchiveHash(File archive, RedmineService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: redmineArchiveHash)()
        return check.matching
    }

    /**
     * Returns if it needed to download and unpack the <i>Redmine</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see RedmineService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, RedmineService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return false
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkRedmineVersion(domain, service) == false
        }
    }

    /**
     * Unpacks the <i>Redmine</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, RedmineService service, File archive) {
        def dir = redmineDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: unpackCommands,
                this, threads)()
        logg.unpackArchiveDone this, redmineArchive
    }

    /**
     * Checks the installed <i>Redmine</i> version.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkRedmineVersion(Domain domain, RedmineService service) {
        def versionFile = redmineVersionFile domain, service
        if (!versionFile.isFile()) {
            return false
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile)
        logg.checkRedmineVersion this, version, redmineUpperVersion
        version.compareTo(redmineUpperVersion) <= 0
    }

    /**
     * Returns the <i>Redmine</i> archive.
     *
     * <ul>
     * <li>profile property {@code "redmine_archive"}</li>
     * </ul>
     *
     * @see #getRedmineFromArchiveProperties()
     */
    URI getRedmineArchive() {
        profileURIProperty "redmine_archive", redmineFromArchiveProperties
    }

    /**
     * Returns the <i>Redmine</i> archive hash, for
     * example {@code "md5:d88ebfdb565489862fc62b4a2a575517"}
     *
     * <ul>
     * <li>profile property {@code "redmine_archive_hash"}</li>
     * </ul>
     *
     * @see #getRedmineFromArchiveProperties()
     */
    URI getRedmineArchiveHash() {
        profileURIProperty "redmine_archive_hash", redmineFromArchiveProperties
    }

    /**
     * Returns the <i>Redmine</i> version, for
     * example {@code "2.5.1"}
     *
     * <ul>
     * <li>profile property {@code "redmine_version"}</li>
     * </ul>
     *
     * @see #getRedmineFromArchiveProperties()
     */
    String getRedmineVersion() {
        profileProperty "redmine_version", redmineFromArchiveProperties
    }

    /**
     * Returns the upper <i>Redmine</i> version, for
     * example {@code "2.5"}
     *
     * <ul>
     * <li>profile property {@code "redmine_upper_version"}</li>
     * </ul>
     *
     * @see #getRedmineFromArchiveProperties()
     */
    Version getRedmineUpperVersion() {
        profileTypedProperty "redmine_upper_version", versionFormatFactory.create(), redmineFromArchiveProperties
    }

    /**
     * Returns the <i>Redmine</i> version file, for example
     * {@code "/var/www/domain.com/redmineprefix/version.txt".}
     * If the path is not absolute, the path is assumed to be under the
     * service installation directory.
     *
     * <ul>
     * <li>profile property {@code "redmine_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link RedmineService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see RedmineService#getPrefix()
     * @see #getRedmineFromArchiveProperties()
     */
    File redmineVersionFile(Domain domain, RedmineService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "redmine_version_file", dir, redmineFromArchiveProperties
    }

    /**
     * Returns the <i>Redmine</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineFromArchiveProperties()

    /**
     * Returns the <i>Redmine</i> service name.
     */
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }

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
