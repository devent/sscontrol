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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.linux

import static org.apache.commons.io.FileUtils.*

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.wordpress.MultiSite
import com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Wordpress 3 configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Wordpress_3_Config extends WordpressConfig {

    Templates wordpressTemplates

    TemplateResource wordpressConfigTemplate

    /**
     * Deploys the main configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployMainConfig(WordpressService service, def domain) {
        def file = configurationFile domain
        def name = file.name
        def tmp = new File(name, tmpDirectory)
        if (!file.isFile()) {
            copyFile configurationDistFile(domain), tmp
        } else {
            copyFile configurationFile(domain), tmp
        }
        List lines = readLines(tmp, configFileCharset)
        writeLines file, configFileCharset.toString(), lines[0..-10]
    }

    /**
     * Deploys the main configuration end.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployMainConfigEnding(WordpressService service, def domain) {
        def search = wordpressConfigTemplate.getText(true, "configEnding_search")
        def replace = wordpressConfigTemplate.getText(true, "configEnding")
        def temp = new TokenTemplate(search, replace)
        temp.enclose = false
        def configs = [temp]
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, configs, file
    }

    /**
     * Deploys the database configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployDatabaseConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, databaseConfigurations(service), file
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
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployKeysConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, keysConfigurations(service), file
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
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployLanguageConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, languageConfigurations(service), file
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
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deploySecureLoginConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, secureLoginConfigurations(service), file
    }

    /**
     * Returns the force secure log-in configurations.
     */
    List secureLoginConfigurations(WordpressService service) {
        [
            configForceSecureLogin(service),
            configForceSecureAdmin(service),
        ]
    }

    def configForceSecureLogin(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configForceSecureLogin_search")
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureLogin", "enabled", service.force.login)
        new TokenTemplate(search, replace)
    }

    def configForceSecureAdmin(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configForceSecureAdmin_search")
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureAdmin", "enabled", service.force.admin)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the debug configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployDebugConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, debugConfigurations(service), file
    }

    /**
     * Returns the debug configurations.
     */
    List debugConfigurations(WordpressService service) {
        [
            configDebug(service),
        ]
    }

    def configDebug(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDebug_search")
        def replace = wordpressConfigTemplate.getText(true, "configDebug", "enabled", service.debug.level == 1)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys multi-site configuration.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     */
    void deployMultisiteConfig(WordpressService service, def domain) {
        def conf = mainConfiguration domain
        def file = configurationFile domain
        deployConfiguration configurationTokens(), conf, debugMultisiteConfig(service), file
    }

    /**
     * Returns the multi-site configurations.
     */
    List debugMultisiteConfig(WordpressService service) {
        [
            configAllowMultisite(service),
        ]
    }

    def configAllowMultisite(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configAllowMultisite_search")
        def replace = wordpressConfigTemplate.getText(true, "configAllowMultisite", "enabled", service.multiSite != MultiSite.none)
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
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Object)
     */
    File configurationFile(def domain) {
        profileFileProperty "wordpress_main_file", wordpressDir(domain), wordpressProperties
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
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Object)
     */
    File configurationDistFile(def domain) {
        profileFileProperty "wordpress_main_dist_file", wordpressDir(domain), wordpressProperties
    }

    /**
     * Returns the current main configuration.
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see #getConfigurationFile()
     * @see #configurationFile(Object)
     */
    String mainConfiguration(def domain) {
        currentConfiguration configurationFile(domain)
    }

    /**
     * Downloads and unpacks the themes.
     *
     * @param domain
     *            the {@link Domain} where the themes are unpacked.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressContentThemesDir(Object)
     */
    void deployThemes(Domain domain, WordpressService service) {
        def dir = wordpressContentThemesDir domain
        service.themes.each { String theme ->
            def archive = themeArchive theme
            def name = new File(archive.path).name
            def dest = new File(tmpDirectory, name)
            def type = archiveType file: dest
            copyURLToFile archive.toURL(), dest
            unpack file: dest, type: type, output: dir, override: true, strip: false
        }
    }

    /**
     * Downloads and unpacks the plug-ins.
     *
     * @param domain
     *            the {@link Domain} where the plug-ins are unpacked.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressContentPluginsDir(Object)
     */
    void deployPlugins(Domain domain, WordpressService service) {
        def dir = wordpressContentPluginsDir domain
        service.plugins.each { String plugin ->
            def archive = pluginArchive plugin
            def name = FilenameUtils.getName(archive.toString())
            def dest = new File(tmpDirectory, name)
            def type = archiveType file: dest
            copyURLToFile archive.toURL(), dest
            unpack file: dest, type: type, output: dir, override: true, strip: false
        }
    }

    @Override
    void setScript(ApacheScript script) {
        super.setScript script
        wordpressTemplates = templatesFactory.create "Wordpress_3"
        wordpressConfigTemplate = wordpressTemplates.getResource "config"
    }
}