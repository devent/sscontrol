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
package com.anrisoftware.sscontrol.remote.openssh.screen.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.remote.user.UserFactory
import com.anrisoftware.sscontrol.scripts.changefilemod.ChangeFileModFactory

/**
 * <i>Screen</i> script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class ScreenScript implements RemoteScript {

    @Inject
    UserFactory userFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    LinuxScript script

    /**
     * Resource create auto-screen script.
     */
    TemplateResource autoScreenTemplate

    /**
     * Resource create screen-session script.
     */
    TemplateResource screenSessionTemplate

    /**
     * Resource create screen configuration.
     */
    TemplateResource screenConfigTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "ScreenScript"
        this.autoScreenTemplate = templates.getResource "autoscreen"
        this.screenSessionTemplate = templates.getResource "screensession"
        this.screenConfigTemplate = templates.getResource "screenconfig"
    }

    @Override
    final void deployRemoteScript(RemoteService service) {
        if (!deployScreen) {
            return
        }
        deployScreenScript service
    }

    void deployScreenScript(RemoteService service) {
        deployAutoScreenScript()
        deployScreenConfigurations service
    }

    void deployAutoScreenScript() {
        File file = autoScreenFile
        file.parentFile.isDirectory() == false ? file.parentFile.mkdirs() : false
        def str = autoScreenTemplate.getText()
        FileUtils.write file, str, charset
        changeFileModFactory.create(
                log: log,
                command: script.chmodCommand,
                mod: "+x",
                files: file,
                this, threads)()
    }

    void appendScreenSession(String name, File sessionFile) {
        if (screenSessionExist(sessionFile)) {
            return
        }
        def str = screenSessionTemplate.getText(true, name, "file", autoScreenFile)
        FileUtils.write sessionFile, str, charset, true
    }

    boolean screenSessionExist(File sessionFile) {
        if (!sessionFile.isFile()) {
            return false
        }
        def session = FileUtils.lineIterator sessionFile, charset.name()
        def found = session.find { it == "# - robobee -" }
        if (found) {
            return true
        }
        return false
    }

    /**
     * Deploys the screen configuration.
     */
    void deployScreenConfigurations(RemoteService service) {
        service.users.each { User user ->
            def file = screenConfigFile(user)
            def config = currentConfiguration file
            deployConfiguration configurationTokens(), config, screenConfigurations(service), file
        }
    }

    /**
     * Returns the screen configurations.
     */
    List screenConfigurations(RemoteService service) {
        def template = screenConfigTemplate
        [
            new TokenTemplate(template.getText(true, "shelltitleConfig_search"), template.getText(true, "shelltitleConfig")),
            new TokenTemplate(template.getText(true, "vbellConfig_search"), template.getText(true, "vbellConfig")),
            new TokenTemplate(template.getText(true, "autodetachConfig_search"), template.getText(true, "autodetachConfig")),
            new TokenTemplate(template.getText(true, "startupMessageConfig_search"), template.getText(true, "startupMessageConfig")),
            new TokenTemplate(template.getText(true, "defScrollbackConfig_search"), template.getText(true, "defScrollbackConfig")),
            new TokenTemplate(template.getText(true, "hardstatusAlwaysLastlineConfig_search"), template.getText(true, "hardstatusAlwaysLastlineConfig")),
            new TokenTemplate(template.getText(true, "hardstatusStringConfig_search"), template.getText(true, "hardstatusStringConfig")),
        ]
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
