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

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.citadel.core.Citadel_8_Config
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
        def task = scriptExecFactory.create(
                log: log,
                citadelServerSetupCommand: citadelSetupCommand,
                adminUser: service.adminUser,
                adminPassword: service.adminPassword,
                citadelUser: citadelUser,
                citadelAddress: citadelAddress,
                citadelPort: citadelPort,
                authMethod: service.authMethod,
                nsswitchDbDisabled: nsswitchDbDisable,
                setupTimeout: cabalSetupTimeout.getStandardSeconds(),
                this, threads, citadelSetupTemplate, "citadelSetup")()
        logg.setupCitadelDone this, task
    }
}
