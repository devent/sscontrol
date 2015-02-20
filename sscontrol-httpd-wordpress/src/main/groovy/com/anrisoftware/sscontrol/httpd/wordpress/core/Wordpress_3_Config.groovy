/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.core

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FilenameUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.wordpress.MultiSite
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory
import com.anrisoftware.sscontrol.scripts.unpack.UnpackFactory

/**
 * <i>Wordpress 3</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Wordpress_3_Config extends WordpressConfig {

    @Inject
    private Wordpress_3_ConfigLogger log

    @Inject
    UnpackFactory unpackFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    Wordpress_4_Permissions wordpressPermissions

    TemplateResource wordpressConfigTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Wordpress_4_Config"
        this.wordpressConfigTemplate = templates.getResource "config"
    }

    /**
     * Deploys the main configuration.
     *
     * @param domain
     *            the {@link Domain} if the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void deployMainConfig(Domain domain, WordpressService service) {
        def file = configurationFile domain, service
        def name = file.name
        def tmp = new File(name, tmpDirectory)
        if (!file.isFile()) {
            copyFile configurationDistFile(domain, service), tmp
        } else {
            copyFile file, tmp
        }
        List lines = readLines(tmp, configFileCharset)
        lines = lines[0..-10]
        writeLines file, configFileCharset.toString(), lines
        log.mainConfigDeployed this, domain, file, lines
    }

    /**
     * Deploys the main configuration end.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployMainConfigEnding(Domain domain, WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configEnding_search")
        def replace = wordpressConfigTemplate.getText(true, "configEnding")
        def temp = new TokenTemplate(search, replace)
        temp.enclose = false
        def configs = [temp]
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
        deployConfiguration configurationTokens(), conf, configs, file
        log.mainConfigEndingDeployed this, domain, file
    }

    /**
     * Deploys the database configuration.
     *
     * @param domain
     *            the {@link Domain} if the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void deployDatabaseConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
        deployConfiguration configurationTokens(), conf, databaseConfigurations(service), file
        log.databaseConfigDeployed this, domain, file
    }

    /**
     * Creates the <i>Wordpress</i> service directories.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     */
    void createDirectories(Domain domain, WordpressService service) {
        wordpressPermissions.createDirectories domain, service
    }

    /**
     * Sets the owner and permissions of the <i>Wordpress</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     */
    void setupPermissions(Domain domain, WordpressService service) {
        wordpressPermissions.setupPermissions domain, service
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
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseName", "database", service.database.database)
        new TokenTemplate(search, replace)
    }

    def configDatabaseUser(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseUser_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseUser", "user", service.database.user)
        new TokenTemplate(search, replace)
    }

    def configDatabasePassword(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabasePassword_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabasePassword", "password", service.database.password)
        new TokenTemplate(search, replace)
    }

    def configDatabaseHost(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseHost_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseHost", "host", service.database.host)
        new TokenTemplate(search, replace)
    }

    def configDatabaseCharset(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseCharset_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseCharset", "charset", service.database.charset)
        new TokenTemplate(search, replace)
    }

    def configDatabaseCollate(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configDatabaseCollate_search")
        def replace = wordpressConfigTemplate.getText(true, "configDatabaseCollate", "collate", service.database.collate)
        new TokenTemplate(search, replace)
    }

    def configDatabaseTablePrefix(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configTablePrefix_search")
        def replace = wordpressConfigTemplate.getText(true, "configTablePrefix", "prefix", service.database.prefix)
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
    void deployKeysConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
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
    void deployLanguageConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
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
        def replace = wordpressConfigTemplate.getText(true, "configLanguage", "language", wordpressLanguage)
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
    void deploySecureLoginConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
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
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureLogin", "enabled", service.forceSslLogin)
        new TokenTemplate(search, replace)
    }

    def configForceSecureAdmin(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configForceSecureAdmin_search")
        def replace = wordpressConfigTemplate.getText(true, "configForceSecureAdmin", "enabled", service.forceSslAdmin)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the debug configuration.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployDebugConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
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
        if (service.debugLogging("level") == null || service.debugLogging("level")["wordpress"] == null) {
            return []
        }
        def debugEnabled = service.debugLogging("level")["wordpress"] > 0
        def search = wordpressConfigTemplate.getText(true, "configDebug_search")
        def replace = wordpressConfigTemplate.getText(true, "configDebug", "enabled", debugEnabled)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys multi-site configuration.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployMultisiteConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
        deployConfiguration configurationTokens(), conf, multisiteConfig(service), file
    }

    /**
     * Returns the multi-site configurations.
     */
    List multisiteConfig(WordpressService service) {
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
     * Deploys the cache configuration.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     *
     * @param service
     *            the {@link WordpressService}.
     */
    void deployCacheConfig(Domain domain, WordpressService service) {
        def conf = mainConfiguration domain, service
        def file = configurationFile domain, service
        deployConfiguration configurationTokens(), conf, cacheConfig(service), file
    }

    /**
     * Returns the cache configurations.
     */
    List cacheConfig(WordpressService service) {
        [
            configCacheEnabled(service),
        ]
    }

    def configCacheEnabled(WordpressService service) {
        def search = wordpressConfigTemplate.getText(true, "configCacheEnabled_search")
        def replace = wordpressConfigTemplate.getText(true, "configCacheEnabled", "enabled", service.cacheEnabled)
        new TokenTemplate(search, replace)
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
     * @see #wordpressThemesDirectory(Domain, WordpressService)
     */
    void deployThemes(Domain domain, WordpressService service) {
        def dir = wordpressThemesDirectory domain, service
        def log = this.log
        service.themes.each { String theme ->
            def archive = themeArchive theme
            def name = new File(archive.path).name
            def dest = new File(tmpDirectory, name)
            copyURLToFile archive.toURL(), dest
            unpackFactory.create(
                    log: log.log,
                    runCommands: runCommands,
                    file: dest,
                    output: dir,
                    override: true,
                    strip: false,
                    commands: unpackCommands,
                    this, threads)()
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
     * @see #wordpressPluginsDirectory(Domain, WordpressService)
     */
    void deployPlugins(Domain domain, WordpressService service) {
        def dir = wordpressPluginsDirectory domain, service
        service.plugins.each { String plugin ->
            installPlugin plugin, dir
        }
    }

    /**
     * Downloads and installs the cache plug-in.
     *
     * @param domain
     *            the {@link Domain} where the plug-ins are unpacked.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressPluginsDirectory(Domain, WordpressService)
     */
    void deployCache(Domain domain, WordpressService service) {
        if (!service.cacheEnabled) {
            return
        }
        def dir = wordpressPluginsDirectory domain, service
        installPlugin service.cachePlugin, dir
        def advancedCacheConfigFile = advancedCacheConfigFile domain, service
        if (advancedCacheConfigFile != null) {
            advancedCacheConfigFile.createNewFile()
        }
    }

    /**
     * Installs the specified plug-in.
     *
     * @param plugin
     *            the plug-in name.
     *
     * @param directory
     *            the plug-ins directory.
     */
    void installPlugin(def plugin, def directory) {
        def archive = pluginArchive plugin
        def name = FilenameUtils.getName(archive.toString())
        def dest = new File(tmpDirectory, name)
        copyURLToFile archive.toURL(), dest
        unpackFactory.create(
                log: log.log,
                file: dest,
                output: directory,
                override: true,
                strip: false,
                commands: unpackCommands,
                this, threads)()
    }

    /**
     * Returns the <i>Wordpress</i> main configuration file, for
     * example {@code "config/wp-config.php"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_main_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Domain, WordpressService)
     */
    File configurationFile(Domain domain, WordpressService service) {
        profileFileProperty "wordpress_main_file", wordpressDir(domain, service), wordpressProperties
    }

    /**
     * Returns the <i>Wordpress</i> main distribution configuration file, for
     * example {@code "config/wp-config-sample.php"}. If the path is relative
     * then the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_main_dist_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see WordpressService#getPrefix()
     * @see #getWordpressProperties()
     * @see #domainDir(Object)
     */
    File configurationDistFile(Domain domain, WordpressService service) {
        def dir = wordpressDir domain, service
        profileFileProperty "wordpress_main_dist_file", dir, wordpressProperties
    }

    /**
     * Returns the current main configuration.
     *
     * @param domain
     *            the domain for which the configuration is returned.
     *
     * @param service
     *            the {@link WordpressService}.
     *
     * @see #getConfigurationFile()
     * @see #configurationFile(Object)
     */
    String mainConfiguration(Domain domain, WordpressService service) {
        currentConfiguration configurationFile(domain, service)
    }

    void setScript(LinuxScript script) {
        super.setScript script
        wordpressPermissions.setScript this
    }
}
