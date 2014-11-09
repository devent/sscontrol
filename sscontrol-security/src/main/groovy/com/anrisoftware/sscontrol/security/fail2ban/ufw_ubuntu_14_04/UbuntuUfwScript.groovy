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
package com.anrisoftware.sscontrol.security.fail2ban.ufw_ubuntu_14_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2BanScript
import com.anrisoftware.sscontrol.security.services.Service

/**
 * <i>Ufw</i> firewall <i>fail2ban</i> script for <i>Ubuntu 14.04.</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuUfwScript extends UfwFail2BanScript {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Override
    void beforeConfiguration() {
        installPackages()
        super.beforeConfiguration()
    }

    @Override
    void deployFirewallScript(Service service) {
        super.deployFirewallScript service
    }

    /**
     * Installs the <i>UFW</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: packages,
                this, threads)()
    }

    /**
     * Returns the packages to install, for example {@code "ufw".}
     *
     * <ul>
     * <li>profile property {@code "packages"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    List getPackages() {
        profileListProperty "packages", firewallProperties
    }

    @Override
    ContextProperties getFirewallProperties() {
        ubuntuPropertiesProvider.get()
    }
}
