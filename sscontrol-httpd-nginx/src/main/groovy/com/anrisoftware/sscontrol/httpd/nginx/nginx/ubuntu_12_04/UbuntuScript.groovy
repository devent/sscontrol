/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4.Nginx_1_4_Script
import com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04.UbuntuNginxRepositoryScript

/**
 * Ubuntu 12.04 Nginx.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuScript extends Nginx_1_4_Script {

    @Inject
    private UbuntuNginxRepositoryScript nginxRepositoryScript

    @Inject
    private UbuntuPropertiesProvider ubuntuProperties

    @Override
    def run() {
        nginxRepositoryScript.setScript this
        super.run()
        restartService()
    }

    @Override
    void beforeConfiguration() {
        enableDebRepositories() ? nginxRepositoryScript.signRepositories() : false
        installPackages()
    }

    /**
     * Restarts the service.
     */
    void restartService() {
        def services = findPortsServices uniqueDomains
        services.each { port, service -> stopService service }
        restartServices()
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
