/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.apache.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory

/**
 * Deploys the SSL/domain configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SslDomainConfig {

    @Inject
    SslDomainConfigLogger logg

    @Inject
    ChangeFileModFactory changeFileModFactory

    LinuxScript script

    /**
     * Copies the certificates to the server.
     */
    void deployCertificates(SslDomain domain) {
        copyURLToFile domain.certificationResource, certFile(domain)
        logg.deployedCert domain
        copyURLToFile domain.certificationKeyResource, certKeyFile(domain)
        logg.deployedCertKey domain
        changePermissions(domain)
    }

    void changePermissions(SslDomain domain) {
        def dir = script.sslDir(domain)
        changeFileModFactory.create(
                log: log,
                command: script.chmodCommand,
                mod: "go-r", files: "${dir.absolutePath}/*",
                this, threads)()
    }

    File certFile(SslDomain domain) {
        new File(sslDir(domain), domain.certificationFile)
    }

    File certKeyFile(SslDomain domain) {
        new File(sslDir(domain), domain.certificationKeyFile)
    }

    /**
     * Enable the SSL mod.
     */
    void enableSsl() {
        enableMod "ssl"
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
