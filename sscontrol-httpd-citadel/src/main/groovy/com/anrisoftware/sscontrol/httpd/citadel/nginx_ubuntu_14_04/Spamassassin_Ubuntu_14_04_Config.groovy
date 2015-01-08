/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.citadel.spamassassin_ubuntu.Spamassassin_3_Ubuntu_Config
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory

/**
 * <i>Spamassassin 3.x Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Spamassassin_Ubuntu_14_04_Config extends Spamassassin_3_Ubuntu_Config {

    @Inject
    Citadel_ubuntu_14_04_PropertyProvider citadelPropertyProvider

    @Inject
    InstallPackagesFactory installPackagesFactory

    /**
     * Deploys the <i>Spamassassin</i> service.
     *
     * @param service
     *            the <i>CitadelService</i> service.
     */
    void deploySpamassassin(CitadelService service) {
        if (enableSpamassassin) {
            installPackages()
        }
        if (spamassassinDefaultsFile.isFile()) {
            deploySpamassassinConfig service
            deploySpamassassinDefaultConfig service
            restartSpamassassin service
        }
    }

    /**
     * Installs the <i>Spamassassin</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log,
                command: installCommand,
                packages: spamassassinPackages,
                system: systemName,
                this, threads)()
    }

    ContextProperties getCitadelProperties() {
        citadelPropertyProvider.get()
    }

    String getServiceName() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.WEB_NAME
    }

    String getProfile() {
        Citadel_Nginx_Ubuntu_14_04_ConfigFactory.PROFILE_NAME
    }
}
