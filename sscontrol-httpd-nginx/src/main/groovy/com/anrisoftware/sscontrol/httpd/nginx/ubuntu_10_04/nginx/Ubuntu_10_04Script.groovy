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
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx

import javax.inject.Inject

import org.apache.commons.io.FileUtils

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
    private Ubuntu_10_04ScriptLogger log

    @Inject
    Ubuntu10_04PropertiesProvider ubuntuProperties

    @Override
    def run() {
        super.run()
        restartServices()
    }

    @Override
    void beforeConfiguration() {
        if (enableDebRepositories()) {
            signRepositories()
        }
        installPackages()
    }

    /**
     * Signs the repositories.
     */
    void signRepositories() {
        def keyFile = new File(tmpDirectory, "nginx_signing.key")
        FileUtils.copyURLToFile nginxSigningKey.toURL(), keyFile
        def worker = scriptCommandFactory.create(nginxCommandsTemplate, "aptKey", "command", aptKeyCommand, "key", keyFile)()
        log.repositorySigned this, worker, nginxSigningKey
    }

    /**
     * Returns the URI of the repository signing key.
     *
     * <ul>
     * <li>profile property {@code nginx_signing_key}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    URI getNginxSigningKey() {
        profileURIProperty "nginx_signing_key", defaultProperties
    }

    @Override
    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
