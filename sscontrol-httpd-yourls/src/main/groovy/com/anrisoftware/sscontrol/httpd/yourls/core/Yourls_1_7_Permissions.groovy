/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.yourls.YourlsService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>Yourls 1.7</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Yourls_1_7_Permissions {

    private Object script

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Sets the owner and permissions of the <i>Yourls</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupPermissions(Domain domain, YourlsService service) {
        setupYourlsPermissions domain, service
        setupPublicPermissions domain, service
        setupPrivatePermissions domain, service
    }

    void setupYourlsPermissions(Domain domain, YourlsService service) {
        def user = domain.domainUser
        def dir = yourlsDir domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: "root",
                ownerGroup: user.group,
                files: dir,
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rwX,g=rX,o=rX",
                files: dir,
                recursive: true,
                this, threads)()
    }

    void setupPublicPermissions(Domain domain, YourlsService service) {
    }

    void setupPrivatePermissions(Domain domain, YourlsService service) {
        def user = domain.domainUser
        def configfile = yourlsConfigFile domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: configfile,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=r,g=r,o-rwx",
                files: configfile,
                this, threads)()
    }

    /**
     * Returns the <i>Yourls</i> service name.
     */
    String getServiceName() {
        script.getServiceName()
    }

    /**
     * Returns the profile name.
     */
    String getProfile() {
        script.getProfile()
    }

    /**
     * Sets the parent script.
     */
    void setScript(Object script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    Object getScript() {
        script
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
     */
    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }

    @Override
    public String toString() {
        new ToStringBuilder(this)
                .append("service name", getServiceName())
                .append("profile name", getProfile()).toString();
    }
}
