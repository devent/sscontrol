/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>FUDForum 3</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Fudforum_3_Permissions {

    private Object script

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Sets the owner and permissions of the <i>FUDForum</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void setupPermissions(Domain domain, FudforumService service) {
        setupFudforumPermissions domain, service
        setupPublicPermissions domain, service
        setupPrivateReadonlyPermissions domain, service
        setupPrivateWritablePermissions domain, service
    }

    void setupFudforumPermissions(Domain domain, FudforumService service) {
        def user = domain.domainUser
        def dir = fudforumDir domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
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

    void setupPublicPermissions(Domain domain, FudforumService service) {
    }

    void setupPrivateReadonlyPermissions(Domain domain, FudforumService service) {
        def user = domain.domainUser
        def installfile = fudforumInstallFile domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: installfile,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=r,g=r,o-rwx",
                files: installfile,
                this, threads)()
    }

    void setupPrivateWritablePermissions(Domain domain, FudforumService service) {
        def user = domain.domainUser
        def globalsfile = fudforumGlobalsFile domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: globalsfile,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rw,g=r,o-rwx",
                files: globalsfile,
                this, threads)()
    }

    /**
     * Returns the <i>FUDForum</i> service name.
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
