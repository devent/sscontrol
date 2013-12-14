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
package com.anrisoftware.sscontrol.httpd.apache.linux.wordpress

import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Wordpress 3 configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseWordpress_3_Config extends BaseWordpressConfig {

    Templates wordpressTemplates

    TemplateResource wordpressConfigTemplate

    /**
     * Deploys the database configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployDatabaseConfig(WordpressService service) {
        if (!configurationFile.isFile()) {
            copyFile configurationDistFile, configurationFile
        }
        deployConfiguration configurationTokens(), mainConfiguration, databaseConfigurations(service), configurationFile
    }

    /**
     * Returns the database configurations.
     */
    List databaseConfigurations(WordpressService service) {
        [
            configDatabaseName(service),
            configDatabaseUser(service),
            configDatabasePassword(service),
            configDatabaseHost(service),
            configDatabaseCharset(service),
            configDatabaseCollate(service),
        ]
    }

    def configDatabaseName(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseName_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseName", "database", service.database)
        new TokenTemplate(search, replace)
    }

    def configDatabaseUser(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseUser_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseUser", "database", service.database)
        new TokenTemplate(search, replace)
    }

    def configDatabasePassword(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabasePassword_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabasePassword", "database", service.database)
        new TokenTemplate(search, replace)
    }

    def configDatabaseHost(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseHost_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseHost", "database", service.database)
        new TokenTemplate(search, replace)
    }

    def configDatabaseCharset(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseCharset_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseCharset", "charset", databaseDefaultCharset)
        new TokenTemplate(search, replace)
    }

    def configDatabaseCollate(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseCollate_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseCollate", "collate", databaseDefaultCollate)
        new TokenTemplate(search, replace)
    }

    /**
     * Wordpress main configuration file, for
     * example {@code "config/wp-config.php"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_main_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #getWordpressDir()
     */
    File getConfigurationFile() {
        profileFileProperty("wordpress_main_file", wordpressDir, defaultProperties)
    }

    /**
     * Wordpress main distribution configuration file, for
     * example {@code "config/wp-config-sample.php"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_main_dist_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #getWordpressDir()
     */
    File getConfigurationDistFile() {
        profileFileProperty("wordpress_main_dist_file", wordpressDir, defaultProperties)
    }

    /**
     * Returns the current main configuration.
     *
     * @see #getConfigurationFile()
     */
    String getMainConfiguration() {
        currentConfiguration configurationFile
    }

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
        wordpressTemplates = templatesFactory.create "Wordpress_3"
        wordpressConfigTemplate = wordpressTemplates.getResource "config"
    }
}
