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

import static org.apache.commons.io.FileUtils.*

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
        if (!databaseConfigFile.isFile()) {
            copyFile databaseDistFile, databaseConfigFile
        }
        deployConfiguration configurationTokens(), databaseConfiguration, databaseConfigurations(service), databaseConfigFile
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

    /**
     * Roundcube main configuration file, for
     * example {@code "config/main.inc.php"}. If the path is relative then
     * the file will be under the Roundcube installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_main_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    File getConfigurationFile() {
        profileFileProperty("roundcube_main_file", roundcubeDir, defaultProperties)
    }

    /**
     * Roundcube database configuration file, for
     * example {@code "config/db.inc.php"}. If the path is relative then
     * the file will be under the Roundcube installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getDatabaseConfigFile() {
        profileFileProperty("roundcube_database_file", roundcubeDir, defaultProperties)
    }

    /**
     * Roundcube database distribution configuration file, for
     * example {@code "config/db.inc.php.dist"}. If the path is relative then
     * the file will be under the Roundcube installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_dist_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #getConfigurationDir()
     */
    File getDatabaseDistFile() {
        profileFileProperty("roundcube_database_dist_file", roundcubeDir, defaultProperties)
    }

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
        roundcubeTemplates = templatesFactory.create "Roundcube_0_9"
        roundcubeConfigTemplate = roundcubeTemplates.getResource "config"
    }
}
