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

import static org.apache.commons.lang3.StringUtils.*

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.wordpress.MultiSite
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService

/**
 * <i>Wordpress</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WordpressConfig {

    public static final String NAME = "wordpress"

    @Inject
    private WordpressConfigLogger log

    private LinuxScript script

    /**
     * Sets the default prefix.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultWordpressPrefix()
     */
    void setupDefaultPrefix(WordpressService service) {
        if (service.prefix == null) {
            service.prefix = defaultWordpressPrefix
        }
    }

    /**
     * Sets the default override mode.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultOverrideMode()
     */
    void setupDefaultOverrideMode(WordpressService service) {
        if (service.overrideMode == null) {
            service.overrideMode = defaultOverrideMode
        }
    }

    /**
     * Sets the default force SSL login and admin.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultForceSecureLogin()
     * @see #getDefaultForceSecureAdmin()
     */
    void setupDefaultForce(WordpressService service) {
        if (service.forceSslLogin == null) {
            service.forceSslLogin = wordpressDefaultForceSecureLogin
        }
        if (service.forceSslAdmin == null) {
            service.forceSslAdmin = wordpressDefaultForceSecureAdmin
        }
    }

    /**
     * Sets the default multi-site mode.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultMultisiteMode()
     */
    void setupDefaultMultisite(WordpressService service) {
        if (service.multiSite == null) {
            service.multiSite = defaultMultisiteMode
        }
    }

    /**
     * Sets the default database.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultDatabaseHost()
     * @see #getDefaultDatabasePort()
     * @see #getDefaultDatabaseCharset()
     * @see #getDefaultDatabaseCollate()
     * @see #getDefaultDatabaseTablePrefix()
     */
    void setupDefaultDatabase(WordpressService service) {
        def database = service.database
        def db = service.database.database
        if (database == null || database.host == null) {
            service.database db, host: defaultDatabaseHost
        }
        if (database == null || database.port == null) {
            service.database db, port: defaultDatabasePort
        }
        if (database == null || database.charset == null) {
            service.database db, schema: defaultDatabaseSchema
        }
        if (database == null || database.charset == null) {
            service.database db, charset: defaultDatabaseCharset
        }
        if (database == null || database.collate == null) {
            service.database db, collate: defaultDatabaseCollate
        }
        if (database == null || database.prefix == null) {
            service.database db, prefix: defaultDatabasePrefix
        }
    }

    /**
     * Sets the default debug.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getDefaultWordpressDebugLevel()
     */
    void setupDefaultDebug(WordpressService service) {
        if (service.debugLogging("level") == null || service.debugLogging("level")["wordpress"] == null) {
            service.debug "wordpress", level: defaultWordpressDebugLevel
        }
    }

    /**
     * Returns the <i>Wordpress</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see WordpressService#getPrefix()
     */
    File wordpressDir(Domain domain, WordpressService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>Wordpress</i> cache directory, for
     * example {@code "wp-content/cache"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_cache_directory"}</li>
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
    File wordpressCacheDirectory(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_cache_directory", dir, wordpressProperties
    }

    /**
     * Returns the <i>Wordpress</i> plug-ins directory, for
     * example {@code "wp-content/plugins"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_plugins_directory"}</li>
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
    File wordpressPluginsDirectory(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_plugins_directory", dir, wordpressProperties
    }

    /**
     * Returns the <i>Wordpress</i> themes directory, for
     * example {@code "wp-content/themes"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_themes_directory"}</li>
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
    File wordpressThemesDirectory(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_themes_directory", dir, wordpressProperties
    }

    /**
     * Returns the <i>Wordpress</i> uploads directory, for
     * example {@code "wp-content/uploads"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_uploads_directory"}</li>
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
    File wordpressUploadsDirectory(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_uploads_directory", dir, wordpressProperties
    }

    /**
     * Returns the service alias directory path.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the references {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressDir(def)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, WordpressService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "$serviceDir/" : serviceDir
    }

    /**
     * Returns the service directory path.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param refDomain
     *            the references {@link Domain} domain or {@code null}.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #wordpressDir(Domain, WordpressService)
     */
    String serviceDir(Domain domain, Domain refDomain, WordpressService service) {
        refDomain == null ? wordpressDir(domain, service).absolutePath :
                wordpressDir(refDomain, service).absolutePath
    }

    /**
     * Returns to block no-referrer requests, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_block_no_referrer_requests"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getBlockNoReferrerRequests() {
        profileBooleanProperty "wordpress_block_no_referrer_requests", wordpressProperties
    }

    /**
     * Returns the list of needed packages for Wordpress.
     *
     * <ul>
     * <li>profile property {@code "wordpress_packages"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    List getWordpressPackages() {
        profileListProperty "wordpress_packages", wordpressProperties
    }

    /**
     * Returns the list of needed Apache mods.
     *
     * <ul>
     * <li>profile property {@code "wordpress_mods"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    List getWordpressMods() {
        profileListProperty "wordpress_mods", wordpressProperties
    }

    /**
     * Returns default Wordpress prefix, for
     * example {@code "wordpress_3_8"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_prefix"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultWordpressPrefix() {
        profileProperty "wordpress_default_prefix", wordpressProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_override_mode"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    OverrideMode getDefaultOverrideMode() {
        def mode = profileProperty "wordpress_default_override_mode", wordpressProperties
        OverrideMode.valueOf mode
    }

    /**
     * Returns the default multi-site mode, for
     * example {@code "none"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_multisite_mode"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    MultiSite getDefaultMultisiteMode() {
        def mode = profileProperty "wordpress_default_multisite_mode", wordpressProperties
        MultiSite.valueOf mode
    }

    /**
     * Returns the default <i>Wordpress</i> debug level, for example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_debug_level"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    int getDefaultWordpressDebugLevel() {
        profileNumberProperty "wordpress_default_debug_level", wordpressProperties
    }

    /**
     * Returns the default database character set, for example {@code "utf8"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_charset"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultDatabaseCharset() {
        profileProperty "wordpress_default_database_charset", wordpressProperties
    }

    /**
     * Returns the default database collate, for example {@code ""}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_collate"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultDatabaseCollate() {
        profileProperty "wordpress_default_database_collate", wordpressProperties
    }

    /**
     * Returns the default database table prefix, for example {@code "wp_"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_prefix"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultDatabasePrefix() {
        profileProperty "wordpress_default_database_prefix", wordpressProperties
    }

    /**
     * Returns the default database host, for example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_host"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultDatabaseHost() {
        profileProperty "wordpress_default_database_host", wordpressProperties
    }

    /**
     * Returns the default database port, for example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_port"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    int getDefaultDatabasePort() {
        profileNumberProperty "wordpress_default_database_port", wordpressProperties
    }

    /**
     * Returns the default database schema, for example {@code "mysql"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_database_schema"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDefaultDatabaseSchema() {
        profileProperty "wordpress_default_database_schema", wordpressProperties
    }

    /**
     * Returns the default enable force secure log-in, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_force_secure_login"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getWordpressDefaultForceSecureLogin() {
        profileBooleanProperty "wordpress_default_force_secure_login", wordpressProperties
    }

    /**
     * Returns the default enable force secure administrator, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_default_force_secure_admin"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getWordpressDefaultForceSecureAdmin() {
        profileBooleanProperty "wordpress_default_force_secure_admin", wordpressProperties
    }

    /**
     * Returns the configuration file character set, for
     * example {@code "ISO-8859-15"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_config_file_charset"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    Charset getConfigFileCharset() {
        profileCharsetProperty "wordpress_config_file_charset", wordpressProperties
    }

    /**
     * Returns the language, for example {@code "de_DE"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_language"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    Locale getWordpressLanguage() {
        profileLocaleProperty "wordpress_language", wordpressProperties
    }

    /**
     * Returns the authentication key, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_auth_key"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getAuthKey() {
        def key = profileProperty "wordpress_auth_key", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the secure authentication key, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_secure_auth_key"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getSecureAuthKey() {
        def key = profileProperty "wordpress_secure_auth_key", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the logged-in key, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_logged_in_key"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getLoggedInKey() {
        def key = profileProperty "wordpress_logged_in_key", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the Nonce key, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_nonce_key"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getNonceKey() {
        def key = profileProperty "wordpress_nonce_key", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the authentication salt, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_auth_salt"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getAuthSalt() {
        def key = profileProperty "wordpress_auth_salt", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the secure authentication salt, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_secure_auth_salt"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getSecureAuthSalt() {
        def key = profileProperty "wordpress_secure_auth_salt", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the logged-in salt, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_logged_in_salt"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getLoggedInSalt() {
        def key = profileProperty "wordpress_logged_in_salt", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the Nonce salt, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_nonce_salt"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see #getWordpressProperties()
     */
    String getNonceSalt() {
        def key = profileProperty "wordpress_nonce_salt", wordpressProperties
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the advanced cache configuration file path, for example
     * {@code "wp-content/advanced-cache.php"}. If the path is relative then
     * the file will be under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_advanced_cache_config_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} where the plug-ins are unpacked.
     *
     * @param service
     *            the {@link WordpressService} service.
     *
     * @see #getWordpressProperties()
     */
    File advancedCacheConfigFile(Domain domain, WordpressService service) {
        def dir = wordpressDir domain, service
        def value = profileProperty "wordpress_advanced_cache_config_file", wordpressProperties
        if (!StringUtils.isEmpty(value)) {
            def file = new File(value)
            return file.absolute ? file : new File(dir, value)
        } else {
            null
        }
    }

    /**
     * Returns the theme archive for the specified name.
     *
     * @param name
     *            the name of the theme.
     *
     * @return the {@link URI} of the theme.
     *
     * @see #getWordpressProperties()
     */
    URI themeArchive(String name) {
        name = replace(name, "-", "_")
        name = replace(name, " ", "_")
        profileURIProperty "wordpress_$name", wordpressProperties
    }

    /**
     * Returns the plug-in archive for the specified name.
     *
     * @param name
     *            the name of the plug-in.
     *
     * @return the {@link URI} of the plug-in.
     *
     * @see #getWordpressProperties()
     */
    URI pluginArchive(String name) {
        name = replace(name, "-", "_")
        name = replace(name, " ", "_")
        profileURIProperty "wordpress_$name", wordpressProperties
    }

    /**
     * Returns the default <i>Wordpress</i> properties.
     */
    abstract ContextProperties getWordpressProperties()

    /**
     * Returns the <i>Wordpress</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        script
    }

    /**
     * Delegates missing properties to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to the parent script.
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
