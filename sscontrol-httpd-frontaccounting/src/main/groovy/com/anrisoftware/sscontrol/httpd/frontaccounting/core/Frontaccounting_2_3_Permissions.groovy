/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>FrontAccounting 2.3</i> configuration file.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class Frontaccounting_2_3_Permissions {

    private Object script

    @Inject
    private ChangeFileModFactory changeFileModFactory

    @Inject
    private ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Sets the owner and permissions of the <i>FrontAccounting</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupPermissions(Domain domain, FrontaccountingService service) {
        setupServicePermissions domain, service
        setupRootPermissions domain, service
        setupPublicPermissions domain, service
        setupPrivatePermissions domain, service
    }

    void setupServicePermissions(Domain domain, FrontaccountingService service) {
        def user = domain.domainUser
        def dir = frontaccountingDir domain, service
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

    /**
     * Make service root directory public so the installation can create
     * the {@code config.php} and {@code config_db.php} files. No need to
     * make the directory public if those files were already created.
     */
    void setupRootPermissions(Domain domain, FrontaccountingService service) {
        File configdbfile = frontaccountingDatabaseConfigFile domain, service
        if (configdbfile.exists()) {
            return
        }
        def user = domain.domainUser
        def dir = frontaccountingDir domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: dir,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rwx,g=rwx,o=rx",
                files: dir,
                this, threads)()
    }

    /**
     * Setups the permissions of writable directories.
     */
    void setupPublicPermissions(Domain domain, FrontaccountingService service) {
        def user = domain.domainUser
        def dir = frontaccountingDir domain, service
        def langdir = new File(dir, frontaccountingLangDirectory)
        def modulesdir = new File(dir, frontaccountingModulesDirectory)
        def companydir = new File(dir, frontaccountingCompanyDirectory)
        def themesdir = new File(dir, frontaccountingThemesDirectory)
        def sqldir = new File(dir, frontaccountingSqlDirectory)
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                recursive: true,
                files: [
                    langdir,
                    modulesdir,
                    companydir
                ],
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=rwX,g=rwX,o=rX",
                recursive: true,
                files: [
                    langdir,
                    modulesdir,
                    companydir
                ],
                this, threads)()
    }

    /**
     * Setup security critical files and directories.
     */
    void setupPrivatePermissions(Domain domain, FrontaccountingService service) {
        File configdbfile = frontaccountingDatabaseConfigFile domain, service
        if (!configdbfile.exists()) {
            return
        }
        def user = domain.domainUser
        File configfile = frontaccountingConfigFile domain, service
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                files: [configfile, configdbfile],
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "u=r,g=r,o-rwx",
                files: [configfile, configdbfile],
                this, threads)()
    }

    /**
     * Returns the <i>FrontAccounting</i> service name.
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
