/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.forarchive

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.checkfilehash.CheckFileHashFactory
import com.anrisoftware.globalpom.version.Version
import com.anrisoftware.globalpom.version.VersionFormatFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService

/**
 * <i>Gitolite</i> from an archive configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class GitoliteFromArchive {

    private Object script

    @Inject
    private GitoliteFromArchiveLogger log

    @Inject
    private UnpackFactory unpackFactory

    @Inject
    private CheckFileHashFactory checkFileHashFactory

    @Inject
    private VersionFormatFactory versionFormatFactory

    /**
     * Deploys the configuration.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void deployService(GitoliteService service) {
        unpackGitoliteArchive service
        saveGitoliteVersion service
    }

    /**
     * Downloads and unpacks the <i>Gitolite</i> source archive.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @see #getGitoliteArchive()
     */
    void unpackGitoliteArchive(GitoliteService service) {
        if (!needUnpackArchive(service)) {
            return
        }
        def name = new File(gitoliteArchive.path).name
        def dest = new File(tmpDirectory, "gitolite-$name")
        downloadArchive gitoliteArchive, dest, service
        unpackArchive service, dest
    }

    /**
     * Returns if it needed to download and unpack the <i>Gitolite</i> archive.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @return {@code true} if it is needed.
     *
     * @see GitoliteService#getOverrideMode()
     */
    boolean needUnpackArchive(GitoliteService service) {
        switch (service.overrideMode) {
            case OverrideMode.no:
                return !serviceInstalled(service)
            case OverrideMode.override:
                return true
            case OverrideMode.update:
                return checkGitoliteVersion(service, true)
            case OverrideMode.upgrade:
                return checkGitoliteVersion(service, false)
        }
    }

    /**
     * Returns if the <i>Gitolite</i> service is already installed.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @return {@code true} if the service is already installed.
     */
    boolean serviceInstalled(GitoliteService service) {
        gitoliteCommand(service).exists()
    }

    /**
     * Downloads the <i>Gitolite</i> archive.
     *
     * @param archive
     *            the archive {@link URI} of the service.
     *
     * @param dest
     *            the destination {@link File} file.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @see #getGitoliteArchive()
     */
    void downloadArchive(URI archive, File dest, GitoliteService service) {
        if (dest.isFile() && !checkArchiveHash(dest, service)) {
            copyURLToFile archive.toURL(), dest
        } else if (!dest.isFile()) {
            copyURLToFile archive.toURL(), dest
        }
        if (!checkArchiveHash(dest, service)) {
            throw log.errorArchiveHash(service, gitoliteArchive)
        }
    }

    /**
     * Checks the <i>Gitolite</i> archive hash.
     *
     * @param archive
     *            the archive {@link File} file.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @see #getGitoliteArchiveHash()
     */
    boolean checkArchiveHash(File archive, GitoliteService service) {
        def check = checkFileHashFactory.create(this, file: archive, hash: gitoliteArchiveHash)()
        return check.matching
    }

    /**
     * Unpacks the <i>Gitolite</i> archive.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @param archive
     *            the archive {@link File} file.
     */
    void unpackArchive(GitoliteService service, File archive) {
        def dir = new File(service.prefix)
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

        log.unpackArchiveDone this, gitoliteArchive
    }

    /**
     * Checks the installed <i>Gitolite</i> service version.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @param equals
     *            set to {@code true} if the version should be greater
     *            or equals.
     *
     * @return {@code true} if the version matches the set version.
     */
    boolean checkGitoliteVersion(GitoliteService service, boolean equals) {
        def versionFile = gitoliteVersionFile service
        if (!versionFile.isFile()) {
            return true
        }
        def version = versionFormatFactory.create().parse FileUtils.readFileToString(versionFile).trim()
        if (equals) {
            log.checkVersionGreaterEquals this, version, gitoliteVersion, gitoliteUpperVersion
            return gitoliteVersion.compareTo(version) >= 0 && version.compareTo(gitoliteUpperVersion) <= 0
        } else {
            log.checkVersionGreater this, version, gitoliteVersion, gitoliteUpperVersion
            return gitoliteVersion.compareTo(version) > 0 && version.compareTo(gitoliteUpperVersion) <= 0
        }
    }

    /**
     * Saves the installed <i>Gitolite</i> version.
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @see #gitoliteVersionFile(GitoliteService)
     */
    void saveGitoliteVersion(GitoliteService service) {
        def file = gitoliteVersionFile service
        def string = versionFormatFactory.create().format gitoliteVersion
        FileUtils.writeStringToFile file, string, charset
    }

    /**
     * Returns the <i>Gitolite</i> archive, for example
     * {@code "https://github.com/sitaramc/gitolite/archive/v3.6.2.tar.gz"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_archive"}</li>
     * </ul>
     *
     * @see #getGitoliteArchiveProperties()
     */
    URI getGitoliteArchive() {
        profileURIProperty "gitolite_archive", gitoliteArchiveProperties
    }

    /**
     * Returns the <i>Gitolite</i> version, for
     * example {@code "3.6.2"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_version"}</li>
     * </ul>
     *
     * @see #getGitoliteArchiveProperties()
     */
    Version getGitoliteVersion() {
        profileTypedProperty "gitolite_version", versionFormatFactory.create(), gitoliteArchiveProperties
    }

    /**
     * Returns the upper <i>Gitolite</i> version, for
     * example {@code "3"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_upper_version"}</li>
     * </ul>
     *
     * @see #getGitoliteArchiveProperties()
     */
    Version getGitoliteUpperVersion() {
        profileTypedProperty "gitolite_upper_version", versionFormatFactory.create(), gitoliteArchiveProperties
    }

    /**
     * Returns the <i>Gitolite</i> archive hash, for
     * example {@code "md5:72b70ed378a775b5cd496ee02b81c467"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_archive_hash"}</li>
     * </ul>
     *
     * @see #getGitoliteArchiveProperties()
     */
    URI getGitoliteArchiveHash() {
        profileURIProperty "gitolite_archive_hash", gitoliteArchiveProperties
    }

    /**
     * Returns the <i>Gitolite</i> version file, for example
     * {@code "version.txt".}
     * If the path is not absolute, the path is assumed to be under the
     * service installation directory.
     *
     * <ul>
     * <li>profile property {@code "gitolite_version_file"}</li>
     * </ul>
     *
     * @param service
     *            the {@link GitoliteService} service.
     *
     * @return the version {@code File} file.
     *
     * @see #getGitoliteArchiveProperties()
     */
    File gitoliteVersionFile(GitoliteService service) {
        profileFileProperty "gitolite_version_file", new File(service.prefix), gitoliteArchiveProperties
    }

    /**
     * Returns the <i>Gitolite</i> archive properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getGitoliteArchiveProperties()

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
