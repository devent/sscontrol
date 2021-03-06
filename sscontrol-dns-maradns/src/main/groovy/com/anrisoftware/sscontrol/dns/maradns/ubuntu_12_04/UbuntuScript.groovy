/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_12_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dns.maradns.maradns_1_2.Maradns_1_2_Script
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Ubuntu 12.04 MaraDNS</i> service script.
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
    RestartServicesFactory restartServicesFactory

    def run() {
        setupDefaultBinding service
        installPackages()
        deployMaraDnsConfiguration service
        deployZoneDbConfigurations service
        restartService()
    }

    /**
     * Installs the <i>MaraDNS</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    /**
     * Restarts the <i>MaraDNS</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log,
                runCommands: runCommands,
                command: restartCommand,
                services: restartServices,
                flags: restartFlags,
                this, threads)()
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
