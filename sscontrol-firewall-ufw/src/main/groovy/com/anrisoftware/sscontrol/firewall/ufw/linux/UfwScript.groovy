/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.linux

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * <i>UFW</i> service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class UfwScript extends LinuxScript {

    @Inject
    UfwScriptLogger logg

    @Inject
    ScriptExecFactory scriptExecFactory

    /**
     * Resource containing the <i>ufw</i> rules templates,.
     */
    TemplateResource rulesTemplate

    @Override
    def run() {
        distributionSpecificConfiguration()
        deployRules()
    }

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Ufw"
        rulesTemplate = templates.getResource "rules"
    }

    /**
     * Run the distribution specific configuration.
     */
    abstract distributionSpecificConfiguration()

    /**
     * Deploys the <i>firewall</i> rules.
     */
    void deployRules() {
        def worker = scriptExecFactory.create(
                log: log,
                service: service,
                command: ufwCommand,
                this, threads,
                rulesTemplate, "rules")()
        logg.deployedRules this, worker
    }

    /**
     * Returns the <i>ufw</i> tool command.
     *
     * <ul>
     * <li>profile property {@code "ufw_command"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getUfwCommand() {
        profileProperty "ufw_command", defaultProperties
    }
}
