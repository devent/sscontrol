/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */


/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.fromarchive

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.CheckVersionLimitFactory
import com.anrisoftware.sscontrol.scripts.versionlimits.ReadVersionFactory

/**
 * Installs <i>Wordpress</i> from archive.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WordpressFromArchiveConfig {

    @Inject
    private WordpressFromArchiveConfigLogger log

    private Object script

    @Inject
    CheckFileHashFactory checkFileHashFactory

    @Inject
    UnpackFactory unpackFactory

    @Inject
    VersionFormatFactory versionFormatFactory

    @Inject
    CheckVersionLimitFactory checkVersionLimitFactory

    @Inject
    ReadVersionFactory readVersionFactory

    /**
     * Deploys the <i>Wordpress</i> service.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     */
    void deployService(Domain domain, WordpressService service) {
        unpackWordpressArchive domain, service
        saveVersionFile domain, service
    }

    /**
     * Downloads and unpacks the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getWordpressArchive()
     * @see LinuxScript#getTmpDirectory()
     */
    void unpackWordpressArchive(Domain domain, WordpressService service) {
        if (!needUnpackArchive(domain, service)) {
            return
        }
        def name = new File(wordpressArchive.path).name
        def dest = new File(tmpDirectory, "wordpress-$name")
        downloadArchive wordpressArchive, dest, service
        unpackArchive domain, service, dest
    }

    /**
     * Saves the <i>Wordpress</i> version file.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressVersionFile(Domain, WordpressService)
     */
    void saveVersionFile(Domain domain, WordpressService service) {
        def file = wordpressVersionFile domain, service
        FileUtils.writeStringToFile file, wordpressVersion, charset
    }

    /**
     * Returns if it needed to download and unpack the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see WordpressService#getOverrideMode()
     */
    boolean needUnpackArchive(Domain domain, WordpressService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return false
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkWordpressVersion(domain, service)
        }
    }

    /**
     * Downloads the <i>Wordpress</i> archive.
     *
     * @param archive
     *            the archive {@link URI} domain of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getWordpressArchive()
     */
    void downloadArchive(URI archive, File dest, WordpressService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, wordpressArchive)
        }
    }

    /**
     * Unpacks the <i>Wordpress</i> archive.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(Domain domain, WordpressService service, File archive) {
        def dir = wordpressDir domain, service
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
        log.unpackArchiveDone this, wordpressArchive
    }

    /**
     * Checks the installed <i>Wordpress</i> version.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkWordpressVersion(Domain domain, WordpressService service) {
        def versionFile = wordpressVersionFile domain, service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        log.checkVersion this, version, wordpressUpperVersion
        version.compareTo(wordpressUpperVersion) <= 0
    }

    /**
     * Checks the <i>Wordpress</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getWordpressArchiveHash()
     */
    boolean checkArchiveHash(File archive, WordpressService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: wordpressArchiveHash)()
        return check.matching
    }

    /**
     * Returns the <i>Wordpress</i> archive based on the language.
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressFromArchiveProperties()
     */
    URI getWordpressArchive() {
        String lang = wordpressLanguage.toString()
        String property
        URI uri
        switch (lang) {
            case "":
                property = "wordpress_archive"
                break
            default:
                property = "wordpress_archive_$lang"
                if (!containsKey(property, wordpressFromArchiveProperties)) {
                    property = "wordpress_archive"
                }
        }
        uri = profileURIProperty property, wordpressFromArchiveProperties
        log.returnsWordpressArchive this, lang, uri
        return uri
    }

    /**
     * Returns the <i>Wordpress</i> archive hash based on the language.
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive_hash[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressFromArchiveProperties()
     */
    URI getWordpressArchiveHash() {
        String lang = wordpressLanguage.toString()
        String property
        URI uri
        switch (lang) {
            case "":
                property = "wordpress_archive_hash"
                break
            default:
                property = "wordpress_archive_hash_$lang"
        }
        if (containsKey(property, wordpressFromArchiveProperties)) {
            uri = profileURIProperty property, wordpressFromArchiveProperties
            log.returnsWordpressArchiveHash this, lang, uri
            return uri
        }
    }

    /**
     * Returns to strip the <i>Wordpress</i> archive from the
     * container directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_strip_archive[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressFromArchiveProperties()
     */
    boolean getStripArchive() {
        String lang = wordpressLanguage.toString()
        String property
        boolean strip
        switch (lang) {
            case "":
                property = "wordpress_strip_archive"
                break
            default:
                property = "wordpress_strip_archive_$lang"
                if (!containsKey(property, wordpressFromArchiveProperties)) {
                    property = "wordpress_strip_archive"
                }
        }
        strip = profileBooleanProperty property, wordpressFromArchiveProperties
        log.returnsStripArchive this, lang, strip
        return strip
    }

    /**
     * Returns the <i>Wordpress</i> version, for
     * example {@code "4.1"}
     *
     * <ul>
     * <li>profile property {@code "wordpress_version"}</li>
     * </ul>
     *
     * @see #getWordpressFromArchiveProperties()
     */
    String getWordpressVersion() {
        profileProperty "wordpress_version", wordpressFromArchiveProperties
    }

    /**
     * Returns the upper <i>Wordpress</i> version, for
     * example {@code "4"}
     *
     * <ul>
     * <li>profile property {@code "wordpress_upper_version"}</li>
     * </ul>
     *
     * @see #getWordpressFromArchiveProperties()
     */
    Version getWordpressUpperVersion() {
        profileTypedProperty "wordpress_upper_version", versionFormatFactory.create(), wordpressFromArchiveProperties
    }

    /**
     * Returns the <i>Wordpress</i> version file, for example
     * {@code "version.txt".} If the path is not absolute, the path is
     * assumed to be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_version_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see WordpressService#getPrefix()
     * @see #getWordpressFromArchiveProperties()
     */
    File wordpressVersionFile(Domain domain, WordpressService service) {
        def dir = new File(domainDir(domain), service.prefix)
        profileFileProperty "wordpress_version_file", dir, wordpressFromArchiveProperties
    }

    /**
     * Returns the <i>Wordpress</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getWordpressFromArchiveProperties()

    /**
     * Returns the <i>Wordpress</i> service name.
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
