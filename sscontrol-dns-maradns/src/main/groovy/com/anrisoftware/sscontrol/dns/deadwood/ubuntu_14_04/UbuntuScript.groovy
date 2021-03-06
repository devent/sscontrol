/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.dns.deadwood.deadwood_3_2.Deadwood_3_2_Script
import com.anrisoftware.sscontrol.scripts.localuser.LocalGroupAddFactory
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserAddFactory
import com.anrisoftware.sscontrol.scripts.localuser.LocalUserInfoFactory
import com.anrisoftware.sscontrol.scripts.unix.InstallPackagesFactory
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory

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
    RestartServicesFactory restartServicesFactory

    @Inject
    LocalUserAddFactory localUserAddFactory

    @Inject
    LocalGroupAddFactory localGroupAddFactory

    @Inject
    LocalUserInfoFactory localUserInfoFactory

    @Inject
    ScriptExecFactory scriptExecFactory

    TemplateResource deadwoodRunTemplate

    TemplateResource activateServiceTemplate

    TemplateResource statusServiceTemplate

    def run() {
        setupDefaultBinding service
        installPackages()
        deployDeadwoodConfiguration service
        createDeadwoodDirectories()
        createDeadwoodUser()
        createDeadwoodCacheFile()
        deployDeadwoodRunScript()
        activateDeadwoodService()
        restartService()
        checkService()
    }

    @Inject
    final void setUbuntuScriptTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create("Deadwood_Ubuntu_14_04")
        this.deadwoodRunTemplate = templates.getResource("deadwoodrun")
        this.activateServiceTemplate = templates.getResource("activate_service")
        this.statusServiceTemplate = templates.getResource("status_service")
    }

    /**
     * Creates the <i>Deadwood</i> user.
     */
    void createDeadwoodUser() {
        localGroupAddFactory.create(
                log: log,
                runCommands: runCommands,
                command: groupAddCommand,
                groupsFile: groupsFile,
                groupName: deadwoodGroup,
                systemGroup: true,
                this, threads)()
        localUserAddFactory.create(
                log: log,
                runCommands: runCommands,
                command: userAddCommand,
                usersFile: usersFile,
                userName: deadwoodUser,
                groupName: deadwoodGroup,
                systemUser: true,
                this, threads)()
        def info = localUserInfoFactory.create(
                log: log,
                runCommands: runCommands,
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
                log: log,
                runCommands: runCommands,
                command: installCommand,
                packages: packages,
                system: systemName,
                this, threads)()
    }

    /**
     * Restarts the <i>Deadwood</i> service.
     */
    void restartService() {
        restartServicesFactory.create(
                log: log,
                runCommands: runCommands,
                command: restartCommand,
                services: restartServices,
                flags: restartFlags,
                this, threads)()
    }

    /**
     * Checks the status of the <i>Deadwood</i> service.
     */
    void checkService() {
        scriptExecFactory.create(
                log: log,
                runCommands: runCommands,
                command: statusCommand,
                flags: statusFlags,
                this, threads,
                statusServiceTemplate, "statusService")()
    }

    /**
     * Creates the <i>Deadwood</i> service directories.
     */
    void createDeadwoodDirectories() {
    }

    /**
     * Deploys the <i>Deadwood</i> run script.
     */
    void deployDeadwoodRunScript() {
        def configuration = deadwoodRunTemplate.getText("deadwoodrun", "args", this)
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
                log: log,
                command: updateRcCommand,
                service: name,
                this, threads,
                activateServiceTemplate, "activateService")()
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

    /**
     * Returns path of the <i>status</i> command, for
     * example {@code /etc/init.d/deadwood}.
     *
     * <ul>
     * <li>profile property key {@code status_command}</li>
     * <li>profile property key {@code deadwood_status_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getStatusCommand() {
        if (containsKey("deadwood_status_command", defaultProperties)) {
            profileProperty "deadwood_status_command", defaultProperties
        } else {
            profileProperty "status_command", defaultProperties
        }
    }

    /**
     * Returns path of the <i>start-stop-daemon</i> command, for
     * example {@code /sbin/start-stop-daemon}.
     *
     * <ul>
     * <li>profile property key {@code start_stop_daemon_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getStartStopDaemonCommand() {
        profileProperty "start_stop_daemon_command", defaultProperties
    }

    /**
     * Returns the <i>status</i> command flags, for
     * example {@code status}.
     *
     * <ul>
     * <li>profile property key {@code status_flags}</li>
     * <li>profile property key {@code deadwood_status_flags}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getStatusFlags() {
        if (containsKey("deadwood_status_flags", defaultProperties)) {
            profileProperty "deadwood_status_flags", defaultProperties
        } else {
            profileProperty "status_flags", defaultProperties
        }
    }

    ContextProperties getDefaultProperties() {
        ubuntuProperties.get()
    }
}
