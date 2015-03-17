/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService

/**
 * <i>Gitolite Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class UbuntuConfig {

    @Inject
    private UbuntuConfigLogger log

    private Object script

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    /**
     * Creates the local user and group.
     *
     * @param service
     *            the {@link GitoliteService} service.
     */
    void createGitoliteUser(GitoliteService service) {
        def home = service.prefix
        def shell = "/bin/false"
        localGroupAddFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: groupAddCommand,
                groupsFile: groupsFile,
                groupName: service.user.group,
                groupId: service.user.gid,
                systemGroup: true,
                this, threads)()
        localUserAddFactory.create(
                log: log.log,
                runCommands: runCommands,
                command: userAddCommand,
                usersFile: usersFile,
                userName: service.user.user,
                groupName: service.user.group,
                userId: service.user.uid,
                homeDir: home,
                shell: shell,
                systemUser: true,
                this, threads)()
        log.createdGitoliteUser this, service
    }

    /**
     * Installs the <i>Gitolite/i> packages.
     *
     * @see #getGitolitePackages()
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log.log,
                command: installCommand,
                packages: gitolitePackages,
                system: systemName,
                this, threads)()
    }

    /**
     * Returns the <i>Gitolite</i> packages, for example
     * {@code "git"}
     *
     * <ul>
     * <li>profile property {@code "gitolite_packages"}</li>
     * </ul>
     *
     * @see #getGitoliteProperties()
     */
    List getGitolitePackages() {
        profileListProperty "gitolite_packages", gitoliteProperties
    }

    /**
     * Returns the default <i>Gitolite</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getGitoliteProperties()

    /**
     * Returns the <i>Gitolite</i> service name.
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
