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
package com.anrisoftware.sscontrol.source.gitolite.core

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory
import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService

/**
 * <i>Gitolite 3 Rc</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GitolitercConfig {

    private Object script

    private TemplateResource gitolitercConfigTemplate

    @Inject
    private Gitolite_3_ConfigLogger log

    @Inject
    private ScriptExecFactory scriptExecFactory

    @Inject
    final void setTemplatesFactory(TemplatesFactory templatesFactory) {
        def templates = templatesFactory.create "Gitolite_3_Config"
        this.gitolitercConfigTemplate = templates.getResource "gitolite_rc_config"
    }

    /**
     * Deploys the <i>Gitolite Rc</i> configuration.
     *
     * @param service
     *            the {@link GitoliteService}.
     */
    void deployGitolitercConfig(GitoliteService service) {
        def file = gitolitercFile service
        def current = currentConfiguration file
        def configs = [
            configUmask(service),
        ]
        deployConfiguration configurationTokens(), current, configs, file
    }

    def configUmask(GitoliteService service) {
        def search = gitolitercConfigTemplate.getText(true, "configUmaskSearch")
        def replace = gitolitercConfigTemplate.getText(true, "configUmask", "umask", umask)
        new TokenTemplate(search, replace)
    }

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
     * Delegates missing property to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing method to the parent script.
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
