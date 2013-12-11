/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Roundcube 0.9 configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseRoundcube_0_9_Config extends BaseRoundcubeConfig {

    Templates roundcubeTemplates

    TemplateResource roundcubeConfigTemplate

    /**
     * Deploys the database configuration.
     *
     * @param service
     *            the {@link RoundcubeService}.
     */
    void deployDatabaseConfig(RoundcubeService service) {
        deployConfiguration configurationTokens(), databaseConfiguration, databaseConfigurations(service), configurationFile
    }

    /**
     * Returns the database configurations.
     */
    List databaseConfigurations(RoundcubeService service) {
        [
            configDbdsnw(service),
        ]
    }

    def configDbdsnw(RoundcubeService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDbdsnw_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDbdsnw", "database", service.database)
        new TokenTemplate(search, replace)
    }

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
        roundcubeTemplates = templatesFactory.create "Roundcube_0_9"
        roundcubeConfigTemplate = roundcubeTemplates.getResource "config"
    }
}
