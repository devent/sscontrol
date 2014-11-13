/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_10_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.maradns.maradns_1_2.Maradns_1_2_Script
import com.anrisoftware.sscontrol.scripts.enableaptrepository.EnableAptRepositoryFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * MaraDNS/Ubuntu 10.04 service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Maradns_1_2_Script {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    EnableAptRepositoryFactory enableAptRepositoryFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    def run() {
        enableRepository()
        installPackages()
        deployMaraDnsConfiguration()
        deployZoneDbConfigurations()
        restartService()
    }

    /**
     * Installs the <i>universe</i> repository.
     */
    void enableRepository() {
        enableAptRepositoryFactory.create(
                log: log,
                charset: charset,
                repository: additionalRepository,
                distributionName: distributionName,
                repositoryString: repositoryString,
                packagesSourcesFile: packagesSourcesFile,
                this, threads)()
    }

    /**
     * Installs the <i>MaraDNS</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: installCommand, packages: packages, this, threads)()
    }

    /**
     * Restarts the <i>MaraDNS</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log, command: restartCommand, services: restartServices, this, threads)()
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
