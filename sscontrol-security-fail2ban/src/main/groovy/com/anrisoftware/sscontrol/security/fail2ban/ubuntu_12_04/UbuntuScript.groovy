/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ubuntu_12_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.security.fail2ban.linux.Fail2BanScript

/**
 * <i>fail2ban</i> script for <i>Ubuntu 12.04.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Fail2BanScript {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    @Override
    void beforeConfiguration() {
        installPackages()
    }

    @Override
    Object run() {
        super.run();
        restartServices()
    }

    /**
     * Installs the <i>fail2ban</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    /**
     * Restarts the <i>fail2ban</i> services.
     */
    void restartServices() {
        restartServicesFactory.create(
                log: log,
                command: restartCommand,
                services: restartServices,
                this, threads)()
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuPropertiesProvider.get()
    }
}
