/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.userkey.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.Require
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.scripts.changefileowner.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * Local users SSH/keys script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UserKeyScript implements RemoteScript {

    @Inject
    private UserKeyScriptLogger logg

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    /**
     * Resource create SSH/keys.
     */
    TemplateResource createKeyTemplate

    /**
     * Resource to configuration for SSH.
     */
    TemplateResource configTemplate

    LinuxScript script

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "UserKeyScript"
        this.createKeyTemplate = templates.getResource "createkey"
        this.configTemplate = templates.getResource "config"
    }

    @Override
    void deployRemoteScript(RemoteService service) {
        deploySshdConfiguration service
        createSshkeys()
    }

    /**
     * Create local user SSH/keys.
     */
    void createSshkeys() {
        RemoteService service = this.service
        service.users.each { User user ->
            def sshkeyfile = sshkeyFile user
            if (sshkeyfile.isFile()) {
                updateSshkey user, sshkeyfile
            } else {
                createSshkey user, sshkeyfile
            }
        }
    }

    /**
     * Updates the SSH/key for the user.
     *
     * @param user
     *            the {@link User}.
     *
     * @param keyfile
     *            the user key {@link File}.
     */
    void updateSshkey(User user, File keyfile) {
        if (!user.requires.contains(Require.passphrase)) {
            return
        }
        keyfile.delete()
        createSshkey user, keyfile
    }

    /**
     * Creates the SSH/key for the user.
     *
     * @param user
     *            the {@link User}.
     *
     * @param keyfile
     *            the user key {@link File}.
     */
    void createSshkey(User user, File keyfile) {
        if (!user.passphrase) {
            return
        }
        createSshDir user, keyfile.parentFile
        def tasks = scriptExecFactory.create(
                log: log,
                command: keyGenCommand,
                passphrase: user.passphrase,
                keyFile: keyfile,
                this, threads,
                createKeyTemplate, "createKey")()
        logg.sshkeyCreated script, user, tasks
    }

    /**
     * Creates the user SSH/key directory.
     */
    void createSshDir(User user, File dir) {
        if (dir.isDirectory()) {
            return
        }
        dir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                command: script.chownCommand,
                owner: user.uid,
                ownerGroup:
                user.groupId,
                files: dir,
                this, threads)()
    }

    /**
     * Deploys the SSHD configuration.
     */
    void deploySshdConfiguration(RemoteService service) {
        def file = sshdConfigFile
        def config = currentConfiguration file
        deployConfiguration configurationTokens(), config, sshdConfigurations(service), file
    }

    /**
     * Returns the SSHD configurations.
     */
    List sshdConfigurations(RemoteService service) {
        [
            configPorts(service),
            configAddresses(service),
            configProtocol(protocols),
            configLoginGraceTime(loginGraceTime.standardSeconds),
            configPermitRootLogin(permitRootLogin),
            configStrictModes(strictModes),
            configAuthorizedKeysFile(authorizedKeysFile),
            configPasswordAuthenticationFile(passwordAuthentication),
            configXForwardingConfig(XForwarding),
            syslogFacilityConfig(service.debugLogging("facility")["openssh"]),
            logLevelConfig(service.debugLogging("level")["openssh"]),
        ]
    }

    List configPorts(RemoteService service) {
        service.bindingAddresses.inject([]) { acc, address, ports ->
            ports.each { acc << configPort(it) }
            acc
        }
    }

    def configPort(int port) {
        def search = configTemplate.getText(true, "portConfig_search")
        def replace = configTemplate.getText(true, "portConfig", "port", port)
        new TokenTemplate(search, replace)
    }

    List configAddresses(RemoteService service) {
        service.bindingAddresses.inject([]) { acc, address, ports ->
            acc << configAddress(address)
        }
    }

    def configAddress(String address) {
        def search = configTemplate.getText(true, "addressConfig_search")
        def replace = configTemplate.getText(true, "addressConfig", "address", address)
        new TokenTemplate(search, replace)
    }

    def configProtocol(List protocols) {
        def search = configTemplate.getText(true, "protocolConfig_search")
        def replace = configTemplate.getText(true, "protocolConfig", "protocols", protocols)
        new TokenTemplate(search, replace)
    }

    def configLoginGraceTime(long time) {
        def search = configTemplate.getText(true, "loginGraceTimeConfig_search")
        def replace = configTemplate.getText(true, "loginGraceTimeConfig", "time", time)
        new TokenTemplate(search, replace)
    }

    def configPermitRootLogin(boolean enabled) {
        def search = configTemplate.getText(true, "permitRootLoginConfig_search")
        def replace = configTemplate.getText(true, "permitRootLoginConfig", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configStrictModes(boolean enabled) {
        def search = configTemplate.getText(true, "strictModesConfig_search")
        def replace = configTemplate.getText(true, "strictModesConfig", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configAuthorizedKeysFile(String file) {
        def search = configTemplate.getText(true, "authorizedKeysFileConfig_search")
        def replace = configTemplate.getText(true, "authorizedKeysFileConfig", "file", file)
        new TokenTemplate(search, replace)
    }

    def configPasswordAuthenticationFile(boolean enabled) {
        def search = configTemplate.getText(true, "passwordAuthenticationConfig_search")
        def replace = configTemplate.getText(true, "passwordAuthenticationConfig", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configXForwardingConfig(boolean enabled) {
        def search = configTemplate.getText(true, "xForwardingConfig_search")
        def replace = configTemplate.getText(true, "xForwardingConfig", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def syslogFacilityConfig(String facility) {
        def search = configTemplate.getText(true, "syslogFacilityConfig_search")
        def replace = configTemplate.getText(true, "syslogFacilityConfig", "facility", facility)
        new TokenTemplate(search, replace)
    }

    def logLevelConfig(int level) {
        def levelstr
        switch (level) {
            case 0:
                levelstr = "QUIET"
                break
            case 1:
                levelstr = "FATAL"
                break
            case 2:
                levelstr = "ERROR"
                break
            case 3:
                levelstr = "INFO"
                break
            case 4:
                levelstr = "VERBOSE"
                break
            default:
                levelstr = "DEBUG"
                break
        }
        def search = configTemplate.getText(true, "logLevelConfig_search")
        def replace = configTemplate.getText(true, "logLevelConfig", "level", levelstr)
        new TokenTemplate(search, replace)
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
    }

    @Override
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
