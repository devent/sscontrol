/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.domain.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

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

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    LinuxScript script

    /**
     * Copies the certificates to the server.
     */
    void deployCertificates(SslDomain domain) {
        copyURLToFile domain.certResource.toURL(), certFile(domain)
        logg.deployedCert domain
        copyURLToFile domain.keyResource.toURL(), certKeyFile(domain)
        logg.deployedCertKey domain
        if (domain.caResource) {
            copyURLToFile domain.caResource.toURL(), certCaFile(domain)
        }
        logg.deployedCa domain
        changePermissions(domain)
    }

    void changePermissions(SslDomain domain) {
        def dir = script.sslDir(domain)
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: script.chownCommand,
                owner: "root", ownerGroup: "root", files: "${dir.absolutePath}/*",
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: script.chmodCommand,
                mod: "400", files: "${dir.absolutePath}/*",
                this, threads)()
    }

    File certFile(SslDomain domain) {
        def name = new File(domain.certResource).name
        new File(sslDir(domain), name)
    }

    File certKeyFile(SslDomain domain) {
        def name = new File(domain.keyResource).name
        new File(sslDir(domain), name)
    }

    File certCaFile(SslDomain domain) {
        def name = new File(domain.caResource).name
        new File(sslDir(domain), name)
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
