/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.Nginx_1_4_Script

/**
 * Uses the Nginx service on the Ubuntu 10.04 Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_10_04Script extends Nginx_1_4_Script {

    @Inject
    Ubuntu10_04PropertiesProvider ubuntuProperties

    @Override
    def run() {
        super.run()
        restartServices()
    }

    @Override
    void beforeConfiguration() {
        enableRepositories()
        installPackages()
    }

    void enableRepositories() {
        def distribution = profileProperty "distribution_name", defaultProperties
        def repositories = profileListProperty "additional_repositories", defaultProperties
        enableDebRepositories distribution, repositories
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
