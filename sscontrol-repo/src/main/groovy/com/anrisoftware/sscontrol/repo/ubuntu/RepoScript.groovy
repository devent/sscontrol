/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.UpdatePackagesFactory

/**
 * Deploys the repository on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class RepoScript extends LinuxScript {

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    UpdatePackagesFactory updatePackagesFactory

    /**
     * Installs the repository service packages.
     *
     * @see #getPackages()
     * @see #getInstallCommand()
     * @see #getSystemName()
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                packages: packages,
                command: installCommand,
                system: systemName,
                this, threads)()
    }

    /**
     * Updates the local cache of the packages.
     *
     * @see #getAptitudeCommand()
     * @see #getSystemName()
     */
    void updatePackages() {
        updatePackagesFactory.create(
                log: log,
                command: aptitudeCommand,
                system: systemName,
                this, threads)()
    }

    /**
     * Returns the sources file.
     */
    File getSourcesFile() {
        new File(configurationDir, sourcesFileName)
    }

    /**
     * Returns the <i>aptitude</i> command, for example {@code "/usr/bin/aptitude"}.
     *
     * <ul>
     * <li>property {@code "aptitude_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getAptitudeCommand() {
        profileProperty "aptitude_command", defaultProperties
    }

    /**
     * Returns the sources file name, for example {@code "sources.list"}.
     *
     * <ul>
     * <li>property {@code "sources_file_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSourcesFileName() {
        profileProperty "sources_file_name", defaultProperties
    }

    /**
     * Returns the repository type, for example {@code "deb"}.
     *
     * <ul>
     * <li>property {@code "repository_type"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRepositoryType() {
        profileProperty "repository_type", defaultProperties
    }

    /**
     * Returns the source repository type, for example {@code "deb-src"}.
     *
     * <ul>
     * <li>property {@code "source_repository_type"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSourceRepositoryType() {
        profileProperty "source_repository_type", defaultProperties
    }

    /**
     * Returns the name of the distribution, for example {@code "precise"}.
     *
     * <ul>
     * <li>property {@code "distribution_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDistributionName() {
        profileProperty "distribution_name", defaultProperties
    }

    /**
     * Returns the name of the distribution for security
     * packages, for example {@code "precise-security"}.
     *
     * <ul>
     * <li>property {@code "security_distribution_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getSecurityDistributionName() {
        profileProperty "security_distribution_name", defaultProperties
    }

    /**
     * Returns the name of the distribution for update
     * packages, for example {@code "precise-updates"}.
     *
     * <ul>
     * <li>property {@code "updates_distribution_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUpdatesDistributionName() {
        profileProperty "updates_distribution_name", defaultProperties
    }

    /**
     * Returns the name of the distribution for backports
     * packages, for example {@code "precise-backports"}.
     *
     * <ul>
     * <li>property {@code "backports_distribution_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getBackportsDistributionName() {
        profileProperty "backports_distribution_name", defaultProperties
    }

    /**
     * Returns the default components, for
     * example {@code "main, restricted, universe, multiverse"}.
     *
     * <ul>
     * <li>property {@code "default_components"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultComponents() {
        profileListProperty "default_components", defaultProperties
    }

    /**
     * Returns the default security components, for
     * example {@code "main, restricted, universe, multiverse"}.
     *
     * <ul>
     * <li>property {@code "default_security_components"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultSecurityComponents() {
        profileListProperty "default_security_components", defaultProperties
    }

    /**
     * Returns the default update components, for
     * example {@code "main, restricted, universe, multiverse"}.
     *
     * <ul>
     * <li>property {@code "default_updates_components"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultUpdatesComponents() {
        profileListProperty "default_updates_components", defaultProperties
    }

    /**
     * Returns the default backports components, for
     * example {@code "main, restricted, universe, multiverse"}.
     *
     * <ul>
     * <li>property {@code "default_backports_components"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBackportsComponents() {
        profileListProperty "default_backports_components", defaultProperties
    }

    /**
     * Returns the default repository, for
     * example {@code "http://archive.ubuntu.com/ubuntu/"}.
     *
     * <ul>
     * <li>property {@code "default_repository"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDefaultRepository() {
        profileProperty "default_repository", defaultProperties
    }
}
