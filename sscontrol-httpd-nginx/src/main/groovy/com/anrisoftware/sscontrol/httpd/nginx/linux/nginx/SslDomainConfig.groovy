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
package com.anrisoftware.sscontrol.httpd.nginx.linux.nginx

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain

/**
 * Deploys the SSL/domain configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SslDomainConfig {

    @Inject
    SslDomainConfigLogger log

    NginxScript script

    /**
     * Copies the certificates to the server.
     */
    void deployCertificates(SslDomain domain) {
        copyURLToFile domain.certificationResource, certFile(domain)
        log.deployedCert domain
        copyURLToFile domain.certificationKeyResource, certKeyFile(domain)
        log.deployedCertKey domain
        changePermissions(domain)
    }

    void changePermissions(SslDomain domain) {
        def dir = script.sslDir(domain)
        changeMod mod: "go-r", files: "$dir.absolutePath/*"
    }

    File certFile(SslDomain domain) {
        new File(sslDir(domain), domain.certificationFile)
    }

    File certKeyFile(SslDomain domain) {
        new File(sslDir(domain), domain.certificationKeyFile)
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
