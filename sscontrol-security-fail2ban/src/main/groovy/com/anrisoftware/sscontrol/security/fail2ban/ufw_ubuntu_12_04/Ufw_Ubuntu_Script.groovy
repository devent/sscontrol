/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.ufw_ubuntu_12_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.security.fail2ban.Fail2banService
import com.anrisoftware.sscontrol.security.fail2ban.Jail
import com.anrisoftware.sscontrol.security.fail2ban.ufw.UfwFail2banScript

/**
 * <i>Ufw Fail2ban Ubuntu 12.04</i> firewall script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Ufw_Ubuntu_Script extends UfwFail2banScript {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Override
    void beforeConfiguration(Fail2banService service) {
        installPackages()
        super.beforeConfiguration()
    }

    @Override
    void deployFirewallScript(Fail2banService service, Jail jail) {
        super.deployFirewallScript service, jail
    }

    /**
     * Installs the <i>Ufw</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: ufwPackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Returns the packages to install, for example {@code "ufw".}
     *
     * <ul>
     * <li>profile property {@code "ufw_packages"}</li>
     * </ul>
     *
     * @see #getFirewallProperties()
     */
    List getUfwPackages() {
        profileListProperty "ufw_packages", firewallProperties
    }

    @Override
    ContextProperties getFirewallProperties() {
        ubuntuPropertiesProvider.get()
    }
}
