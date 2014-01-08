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
package com.anrisoftware.sscontrol.httpd.nginx.nginx.debian

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * Nginx from official repository.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class NginxRepositoryScript {

    @Inject
    private NginxRepositoryScriptLogger log

    private LinuxScript script

    /**
     * Signs the repositories.
     */
    void signRepositories() {
        def keyFile = new File(tmpDirectory, "nginx_signing.key")
        FileUtils.copyURLToFile nginxSigningKey.toURL(), keyFile
        def worker = scriptCommandFactory.create(nginxCommandsTemplate, "aptKey", "command", aptKeyCommand, "key", keyFile)()
        log.repositorySigned script, worker, nginxSigningKey
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


    /**
     * Sets the parent script with the properties.
     *
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Returns the parent script with the properties.
     *
     * @return the {@link LinuxScript}.
     */
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
