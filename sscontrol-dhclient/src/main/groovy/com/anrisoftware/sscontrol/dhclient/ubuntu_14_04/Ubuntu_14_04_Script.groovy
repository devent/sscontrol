/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.ubuntu_14_04

import static java.util.regex.Pattern.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.dhclient.ubuntu.UbuntuScript
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * Dhclient/Ubuntu 14.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ubuntu_14_04_Script extends UbuntuScript {

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    void distributionSpecificConfiguration() {
        installPackages()
    }

    /**
     * Installs the <i>dhclient</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: installCommand, packages: packages, this, threads)()
    }

    /**
     * Restarts the <i>dhclient</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log,
                command: restartCommand,
                services: restartServices,
                this, threads)()
    }

    /**
     * Returns the restart command for the service.
     *
     * <ul>
     * <li>property key {@code restart_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getRestartCommand() {
        def command = profileProperty("restart_command", defaultProperties)
        new ST(command).add("interfaces", restartInterfaces).render()
    }

    /**
     * Returns the interfaces to restart.
     *
     * <ul>
     * <li>profile property key {@code restart_interfaces}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getRestartInterfaces() {
        profileListProperty "restart_interfaces", defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
