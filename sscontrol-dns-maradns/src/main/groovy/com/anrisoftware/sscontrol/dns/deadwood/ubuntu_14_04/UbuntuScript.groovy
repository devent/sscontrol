/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.deadwood.ubuntu_14_04

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.dns.deadwood.deadwood_3_2.Deadwood_3_2_Script
import com.anrisoftware.sscontrol.scripts.enableaptrepository.EnableAptRepositoryFactory
import com.anrisoftware.sscontrol.scripts.localgroupadd.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuseradd.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.localuserinfo.LocalUserInfoFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * <i>Deadwood 3.2.x Ubuntu 14.04</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class UbuntuScript extends Deadwood_3_2_Script {

    @Inject
    UbuntuScriptLogger logg

    @Inject
    UbuntuPropertiesProvider ubuntuProperties

    @Inject
    InstallPackagesFactory installPackagesFactory

    @Inject
    EnableAptRepositoryFactory enableAptRepositoryFactory

    @Inject
    RestartServicesFactory restartServicesFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserInfoFactory localUserInfoFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    Templates deadwoodUbuntuTemplates

    TemplateResource deadwoodRun

    TemplateResource activateService

    def run() {
        enableRepository()
        installPackages()
        deployDeadwoodConfiguration()
        createDeadwoodUser()
        createDeadwoodCacheFile()
        deployDeadwoodRunScript()
        activateDeadwoodService()
        restartService()
    }

    @Inject
    void setUbuntuScriptTemplatesFactory(TemplatesFactory factory) {
        this.deadwoodUbuntuTemplates = factory.create("Deadwood_Ubuntu_14_04")
        this.deadwoodRun = deadwoodUbuntuTemplates.getResource("deadwoodrun")
        this.activateService = deadwoodUbuntuTemplates.getResource("activate_service")
    }

    /**
     * Installs the <i>universe</i> repository.
     */
    void enableRepository() {
        enableAptRepositoryFactory.create(
                log: log,
                charset: charset,
                repository: additionalRepository,
                distributionName: distributionName,
                repositoryString: repositoryString,
                packagesSourcesFile: packagesSourcesFile,
                this, threads)()
    }

    /**
     * Creates the <i>Deadwood</i> user.
     */
    void createDeadwoodUser() {
        localUserAddFactory.create(
                log: log,
                command: userAddCommand,
                usersFile: usersFile,
                userName: deadwoodUser,
                groupName: deadwoodGroup,
                systemUser: true,
                this, threads)()
        localGroupAddFactory.create(
                log: log,
                command: groupAddCommand,
                groupsFile: groupsFile,
                groupName: deadwoodGroup,
                systemGroup: true,
                this, threads)()
        def info = localUserInfoFactory.create(
                log: log,
                command: userIdCommand,
                userName: deadwoodUser,
                this, threads)()
        def uid = info.uid
        def gid = info.gid
        deployDeadwoodUserConfiguration uid, gid
    }

    /**
     * Installs the <i>MaraDNS</i> packages.
     */
    void installPackages() {
        installPackagesFactory.create(
                log: log, command: installCommand, packages: packages, this, threads)()
    }

    /**
     * Restarts the <i>MaraDNS</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log, command: restartCommand, services: restartServices, this, threads)()
    }

    /**
     * Deploys the <i>Deadwood</i> run script.
     */
    void deployDeadwoodRunScript() {
        def configuration = deadwoodRun.getText("deadwoodrun", "args", this)
        FileUtils.write deadwoodScriptFile, configuration, charset
        deadwoodScriptFile.setExecutable(true)
        logg.deployRunScriptDone this, deadwoodScriptFile, configuration
    }

    /**
     * Activates the <i>Deadwood</i> service.
     */
    void activateDeadwoodService() {
        def name = "deadwood"
        scriptExecFactory.create(
                log: log, command: updateRcCommand, service: name,
                this, threads, activateService, "activateService")()
    }

    /**
     * Returns path of the <i>update-rc.d</i> command, for
     * example {@code /usr/sbin/update-rc.d}.
     *
     * <ul>
     * <li>profile property key {@code update_rc_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUpdateRcCommand() {
        profileProperty "update_rc_command", defaultProperties
    }

    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
