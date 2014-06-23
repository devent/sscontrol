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
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.checkfilehash.CheckFileHashFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * Installs and configures <i>Redmine</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class RedmineFromArchive {

    @Inject
    private RedmineFromArchiveLogger logg

    @Inject
    TemplatesFactory templatesFactory

    @Inject
    UnpackFactory unpackFactory

    @Inject
    CheckFileHashFactory checkFileHashFactory

    Object script

    Templates hsenvTemplates

    TemplateResource hsenvCommandTemplate

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
    }

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List config) {
        unpackRedmineArchive domain, service
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
     * @see #getRedmineArchive()
     * @see #getRedmineArchiveHash()
     */
    boolean checkArchiveHash(File archive, RedmineService service) {
        def hash = redmineArchiveHash
        if (hash.scheme == "md5") {
            def hashFile = new File(tmpDirectory, "redmine-hash.md5")
            FileUtils.writeStringToFile hashFile, "${hash.schemeSpecificPart}\n", charset
            def check = checkFileHashFactory.create(this, file: archive, hash: hashFile.toURI())()
            hashFile.delete()
            return check.matching
        }
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
     * @see WordpressService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, RedmineService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return false
            case OverrideMode.yes:
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
     *            the {@link WebService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WebService service, File archive) {
        def dir = redmineDir domain, service
        dir.isDirectory() ? false : dir.mkdirs()
        unpackFactory.create(
                log: log,
                file: archive,
                output: dir,
                override: true,
                strip: true,
                commands: script.unpackCommands,
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
        def version = FileUtils.readFileToString versionFile
        logg.checkRedmineVersion this, version, redmineVersion
        return StringUtils.startsWith(version, redmineVersion)
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
     * example {@code "2.5"}
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
     * Returns the <i>Redmine</i> version file, for example
     * {@code /var/www/domain.com/redmineprefix/version.txt}
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
        def name = profileProperty "redmine_version_file", redmineFromArchiveProperties
        new File(dir, name)
    }

    /**
     * Returns the <i>Redmine</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getRedmineFromArchiveProperties()

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
