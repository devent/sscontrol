/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>Wordpress 4</i> file permissions configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Wordpress_4_Permissions {

    private Object script

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Creates the <i>Wordpress</i> service directories.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     */
    void createDirectories(Domain domain, WordpressService service) {
        def cachedir = wordpressCacheDirectory domain, service
        def pluginsdir = wordpressPluginsDirectory domain, service
        def themesdir = wordpressThemesDirectory domain, service
        def uploadsdir = wordpressUploadsDirectory domain, service
        cachedir.mkdirs()
        pluginsdir.mkdirs()
        themesdir.mkdirs()
        uploadsdir.mkdirs()
    }

    /**
     * Sets the owner and permissions of the <i>Wordpress</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     */
    void setupPermissions(Domain domain, WordpressService service) {
        setupWordpressPermissions domain, service
        setupPublicPermissions domain, service
        setupPrivatePermissions domain, service
        setupCachePermissions domain, service
    }

    void setupWordpressPermissions(Domain domain, WordpressService service) {
        def user = domain.domainUser
        def dir = wordpressDir domain, service
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

    void setupPublicPermissions(Domain domain, WordpressService service) {
        def user = domain.domainUser
        def cachedir = wordpressCacheDirectory domain, service
        def pluginsdir = wordpressPluginsDirectory domain, service
        def themesdir = wordpressThemesDirectory domain, service
        def uploadsdir = wordpressUploadsDirectory domain, service
        cachedir.mkdirs()
        pluginsdir.mkdirs()
        themesdir.mkdirs()
        uploadsdir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                recursive: true,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rwX,g=rX,o=rX",
                files: [
                    cachedir,
                    pluginsdir,
                    themesdir,
                    uploadsdir,
                ],
                recursive: true,
                this, threads)()
    }

    void setupPrivatePermissions(Domain domain, WordpressService service) {
        def user = domain.domainUser
        def conffile = configurationFile domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: conffile,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=r,g=r,o-rwx",
                files: conffile,
                this, threads)()
    }

    void setupCachePermissions(Domain domain, WordpressService service) {
        def user = domain.domainUser
        File advancedCacheConfigFile = advancedCacheConfigFile domain, service
        if (advancedCacheConfigFile == null || !advancedCacheConfigFile.exists()) {
            return
        }
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                files: advancedCacheConfigFile,
                owner: domain.domainUser.name,
                ownerGroup: domain.domainUser.group,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                files: advancedCacheConfigFile,
                mod: "u=rw,g=r,o-rw",
                this, threads)()
    }

    /**
     * Returns the <i>Wordpress</i> service name.
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
