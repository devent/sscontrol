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
import com.anrisoftware.sscontrol.httpd.statements.roundcube.Host
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
     * Deploys the main configuration.
     *
     * @param service
     *            the {@link RoundcubeService}.
     */
    void deployMainConfig(RoundcubeService service) {
        if (!configurationFile.isFile()) {
            copyFile configurationDistFile, configurationFile
        }
        deployConfiguration configurationTokens(), mainConfiguration, loggingConfigurations(service), configurationFile
        deployConfiguration configurationTokens(), mainConfiguration, mainConfigurations(service), configurationFile
    }

    /**
     * Returns the main logging configurations.
     */
    List loggingConfigurations(RoundcubeService service) {
        [
            configDebuglevel(service),
            configSmtplog(service),
            configLoglogins(service),
            configLogsession(service),
            configSqldebug(service),
            configImapdebug(service),
            configLdapdebug(service),
            configSmtpdebug(service),
        ]
    }

    def configDebuglevel(RoundcubeService service) {
        def search = roundcubeConfigTemplate.getText(true, "configDebuglevel_search")
        def replace = roundcubeConfigTemplate.getText(true, "configDebuglevel", "debug", service.debugLogging)
        new TokenTemplate(search, replace)
    }

    def configSmtplog(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("smtp")
        def search = roundcubeConfigTemplate.getText(true, "configSmtplog_search")
        def replace = roundcubeConfigTemplate.getText(true, "configSmtplog", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configLoglogins(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("logins")
        def search = roundcubeConfigTemplate.getText(true, "configLoglogins_search")
        def replace = roundcubeConfigTemplate.getText(true, "configLoglogins", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configLogsession(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("session")
        def search = roundcubeConfigTemplate.getText(true, "configLogsession_search")
        def replace = roundcubeConfigTemplate.getText(true, "configLogsession", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configSqldebug(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("sql")
        def search = roundcubeConfigTemplate.getText(true, "configSqldebug_search")
        def replace = roundcubeConfigTemplate.getText(true, "configSqldebug", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configImapdebug(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("imap")
        def search = roundcubeConfigTemplate.getText(true, "configImapdebug_search")
        def replace = roundcubeConfigTemplate.getText(true, "configImapdebug", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configLdapdebug(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("ldap")
        def search = roundcubeConfigTemplate.getText(true, "configLdapdebug_search")
        def replace = roundcubeConfigTemplate.getText(true, "configLdapdebug", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    def configSmtpdebug(RoundcubeService service) {
        def enabled = service.debugLogging.modules?.contains("smtp")
        def search = roundcubeConfigTemplate.getText(true, "configSmtpdebug_search")
        def replace = roundcubeConfigTemplate.getText(true, "configSmtpdebug", "enabled", enabled)
        new TokenTemplate(search, replace)
    }

    /**
     * Returns the main configurations.
     */
    List mainConfigurations(RoundcubeService service) {
        [
            configDefaultHostsInit(service),
            configDefaultHosts(service),
        ]
    }

    def configDefaultHostsInit(RoundcubeService service) {
        if (service.hosts.size() == 1 && service.hosts[0].alias != null) {
            def search = roundcubeConfigTemplate.getText(true, "configDefaultHostMultipleInit_search")
            def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostMultipleInit")
            return new TokenTemplate(search, replace)
        }
        if (service.hosts.size() > 1) {
            def search = roundcubeConfigTemplate.getText(true, "configDefaultHostMultipleInit_search")
            def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostMultipleInit")
            return new TokenTemplate(search, replace)
        }
        []
    }

    def configDefaultHosts(RoundcubeService service) {
        if (service.hosts.size() == 1) {
            def search = roundcubeConfigTemplate.getText(true, "configDefaultHostSingle_search")
            def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostSingle")
            return new TokenTemplate(search, replace)
        }
        if (service.hosts.size() == 1 && service.hosts[0].alias != null) {
            def search = roundcubeConfigTemplate.getText(true, "configDefaultHostMap_search")
            def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostMap")
            return new TokenTemplate(search, replace)
        }
        service.hosts.inject([]) { List list, Host host ->
            println host
            if (host.alias == null) {
                def search = roundcubeConfigTemplate.getText(true, "configDefaultHostMultiple_search", "host", host)
                def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostMultiple", "host", host)
                list << new TokenTemplate(search, replace)
            } else {
                def search = roundcubeConfigTemplate.getText(true, "configDefaultHostMap_search", "host", host)
                def replace = roundcubeConfigTemplate.getText(true, "configDefaultHostMap", "host", host)
                list << new TokenTemplate(search, replace)
            }
        }
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
     * Roundcube main distribution configuration file, for
     * example {@code "config/main.inc.php.dist"}. If the path is relative then
     * the file will be under the Roundcube installation directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_main_dist_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    File getConfigurationDistFile() {
        profileFileProperty("roundcube_main_dist_file", roundcubeDir, defaultProperties)
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

    /**
     * Returns the current database configuration.
     *
     * @see #getDatabaseeConfigFile()
     */
    String getDatabaseConfiguration() {
        currentConfiguration databaseConfigFile
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
        roundcubeTemplates = templatesFactory.create "Roundcube_0_9"
        roundcubeConfigTemplate = roundcubeTemplates.getResource "config"
    }
}
