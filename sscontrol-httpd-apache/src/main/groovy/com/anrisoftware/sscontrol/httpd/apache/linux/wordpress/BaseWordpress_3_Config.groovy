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
            configDatabaseTablePrefix(service),
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

    def configDatabaseTablePrefix(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configTablePrefix_search")
        def replace = wordpressConfigTemplate.getText(true, "configTablePrefix", "prefix", databaseDefaultTablePrefix)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the keys and salts.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployKeysConfig(WordpressService service) {
        if (!configurationFile.isFile()) {
            copyFile configurationDistFile, configurationFile
        }
        deployConfiguration configurationTokens(), mainConfiguration, keysConfigurations(service), configurationFile
    }

    /**
     * Returns the keys and salts configurations.
     */
    List keysConfigurations(WordpressService service) {
        [
            configAuthKey(),
            configSecureAuthKey(),
            configLoggedInKey(),
            configNonceKey(),
            configAuthSalt(),
            configSecureAuthSalt(),
            configLoggedInSalt(),
            configNonceSalt(),
        ]
    }

    def configAuthKey() {
        def search = wordpressConfigTemplate.getText(true, "configAuthKey_search")
        def replace = wordpressConfigTemplate.getText(true, "configAuthKey", "key", authKey)
        new TokenTemplate(search, replace)
    }

    def configSecureAuthKey() {
        def search = wordpressConfigTemplate.getText(true, "configSecureAuthKey_search")
        def replace = wordpressConfigTemplate.getText(true, "configSecureAuthKey", "key", secureAuthKey)
        new TokenTemplate(search, replace)
    }

    def configLoggedInKey() {
        def search = wordpressConfigTemplate.getText(true, "configLoggedInKey_search")
        def replace = wordpressConfigTemplate.getText(true, "configLoggedInKey", "key", loggedInKey)
        new TokenTemplate(search, replace)
    }

    def configNonceKey() {
        def search = wordpressConfigTemplate.getText(true, "configNonceKey_search")
        def replace = wordpressConfigTemplate.getText(true, "configNonceKey", "key", nonceKey)
        new TokenTemplate(search, replace)
    }

    def configAuthSalt() {
        def search = wordpressConfigTemplate.getText(true, "configAuthSalt_search")
        def replace = wordpressConfigTemplate.getText(true, "configAuthSalt", "salt", authSalt)
        new TokenTemplate(search, replace)
    }

    def configSecureAuthSalt() {
        def search = wordpressConfigTemplate.getText(true, "configSecureAuthSalt_search")
        def replace = wordpressConfigTemplate.getText(true, "configSecureAuthSalt", "salt", secureAuthSalt)
        new TokenTemplate(search, replace)
    }

    def configLoggedInSalt() {
        def search = wordpressConfigTemplate.getText(true, "configLoggedInSalt_search")
        def replace = wordpressConfigTemplate.getText(true, "configLoggedInSalt", "salt", loggedInSalt)
        new TokenTemplate(search, replace)
    }

    def configNonceSalt() {
        def search = wordpressConfigTemplate.getText(true, "configNonceSalt_search")
        def replace = wordpressConfigTemplate.getText(true, "configNonceSalt", "salt", nonceSalt)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the language configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployLanguageConfig(WordpressService service) {
        if (!configurationFile.isFile()) {
            copyFile configurationDistFile, configurationFile
        }
        deployConfiguration configurationTokens(), mainConfiguration, languageConfigurations(service), configurationFile
    }

    /**
     * Returns the language configurations.
     */
    List languageConfigurations(WordpressService service) {
        [
            configLanguage(),
        ]
    }

    def configLanguage() {
        def search = wordpressConfigTemplate.getText(true, "configLanguage_search")
        def replace = wordpressConfigTemplate.getText(true, "configLanguage", "language", language)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the force secure log-in configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deploySecureLoginConfig(WordpressService service) {
        if (!configurationFile.isFile()) {
            copyFile configurationDistFile, configurationFile
        }
        deployConfiguration configurationTokens(), mainConfiguration, secureLoginConfigurations(service), configurationFile
    }

    /**
     * Returns the force secure log-in configurations.
     */
    List secureLoginConfigurations(WordpressService service) {
        [
            configForceSecureLogin(),
            configForceSecureAdmin(),
        ]
    }

    def configForceSecureLogin() {
        def search = wordpressConfigTemplate.getText(true, "configForceSecureLogin_search")
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureLogin", "enabled", forceSecureLogin)
        new TokenTemplate(search, replace)
    }

    def configForceSecureAdmin() {
        def search = wordpressConfigTemplate.getText(true, "configForceSecureAdmin_search")
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureAdmin", "enabled", forceSecureAdmin)
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
