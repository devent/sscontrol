/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.citadel.core.Citadel_8_Config
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory

/**
 * <i>Citadel 8 Ubuntu</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Citadel_8_Ubuntu_Config extends Citadel_8_Config {

    @Inject
    private Citadel_8_Ubuntu_ConfigLogger logg

    @Inject
    ScriptExecFactory scriptExecFactory

    @Inject
    AuthMethodAttributeRenderer authMethodAttributeRenderer

    @Inject
    RestartServicesFactory restartServicesFactory

    TemplateResource citadelSetupTemplate

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Citadel_8_Ubuntu_Config", ["renderers": [authMethodAttributeRenderer]]
        this.citadelSetupTemplate = templates.getResource "setup_config"
    }

    /**
     * Setups the <i>Citadel</i> service.
     *
     * @param service
     *            the {@link CitadelService} service.
     *
     * @see #getCitadelProperties()
     */
    void setupCitadel(CitadelService service) {
        def citadelAddress = service.bindingAddresses.keySet().first()
        def citadelPort = service.bindingAddresses[citadelAddress].first()
        def args = [:]
        args.citadelSetupCommand = citadelSetupCommand
        args.adminUser = service.adminUser
        args.adminPassword = service.adminPassword
        args.citadelUser = citadelUser
        args.citadelAddress = citadelAddress
        args.citadelPort = citadelPort
        args.authMethod = service.authMethod
        args.nsswitchDbDisabled = nsswitchDbDisable
        args.setupTimeout = citadelSetupTimeout.getStandardSeconds()
        def script = citadelSetupTemplate.getText(true, "citadelSetupScript", "args", args)
        def file = createSetupCitadelScriptFile()
        FileUtils.writeStringToFile file, script, charset
        file.setExecutable true
        def task = scriptExecFactory.create(
                log: log,
                expectCommand: expectCommand,
                expectScript: file.absolutePath,
                this, threads, citadelSetupTemplate, "citadelSetupWrapper")()
        if (setupCitadelScriptFile.isEmpty()) {
            file.delete()
        }
        logg.setupCitadelDone this, task
    }

    /**
     * Creates the <i>expect</i> setup script file. The file is either created
     * from the property file path or as a temporary file.
     *
     * @see #getCitadelProperties()
     */
    File createSetupCitadelScriptFile() {
        String path = setupCitadelScriptFile
        if (path.isEmpty()) {
            File.createTempFile "setupcitadel", "expect", tmpDirectory
        } else {
            new File(path)
        }
    }

    /**
     * Restarts the <i>Citadel</i> service.
     *
     * @param service
     *            the {@link CitadelService}.
     *
     * @see #getCitadelProperties()
     */
    void restartCitadel(CitadelService service) {
        def task = restartServicesFactory.create(
                log: log,
                command: citadelRestartCommand,
                services: [],
                flags: citadelRestartFlags,
                this, threads)()
    }

    /**
     * Returns the setup <i>Citadel</i> script file path, for
     * example {@code ""}
     *
     * <ul>
     * <li>profile property {@code "setup_citadel_script_file"}</li>
     * </ul>
     *
     * @see #getCitadelProperties()
     */
    String getSetupCitadelScriptFile() {
        profileProperty "setup_citadel_script_file", citadelProperties
    }
}
