/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.apt

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.repo.service.RepoService
import com.google.inject.assistedinject.Assisted

/**
 * Deploys the <i>Apt</i> proxy.
 *
 * @see DeployAptSourcesListFactory
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DeployAptProxy {

    @Inject
    @Assisted
    RepoService service

    @Inject
    @Assisted
    Object script

    TemplateResource proxyConfiguration

    /**
     * Deploys the proxy.
     */
    void deployProxy() {
        if (service.proxy == null) {
            return
        }
        def file = new File(configurationDir, proxyFileName)
        def config = []
        config << proxyConfiguration.getText(true, "configHeader")
        config << proxyConfiguration.getText(true, "proxyConfig", "proxy", service.proxy)
        writeLines file, charset.name(), config
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "AptConfig"
        this.proxyConfiguration = templates.getResource("proxyconfig")
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
