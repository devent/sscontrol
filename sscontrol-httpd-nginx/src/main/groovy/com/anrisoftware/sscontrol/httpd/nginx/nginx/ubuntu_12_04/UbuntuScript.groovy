/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_12_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.Nginx_1_4_Script
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

/**
 * <i>Nginx Ubuntu 12.04</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Nginx_1_4_Script {

    @Inject
    private UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    @Override
    def run() {
        super.run()
        restartService()
    }

    @Override
    void beforeConfiguration() {
        installPackages()
    }

    /**
     * Installs the <i>nginx</i> packages.
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
     * Restarts the service.
     */
    void restartService() {
        def services = findPortsServices uniqueDomains
        services.findAll { port, service -> service != nginxService }.each { port, service -> stopService service }
        restartServices()
    }

    /**
     * Restarts the <i>nginx</i> services.
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
        ubuntuProperties.get()
    }

    @Override
    String getProfileName() {
        Ubuntu_12_04_ScriptFactory.PROFILE
    }
}
