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

import static org.apache.commons.lang3.StringUtils.*

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * Returns roundcube/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseWordpressConfig {

    public static final String NAME = "wordpress"

    @Inject
    private BaseWordpressConfigLogger log

    private ApacheScript script

    /**
     * Returns the Wordpress web mail distribution archive.
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive[_<language>]"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    URI getWordpressArchive() {
        String lang = language.toString()
        switch (lang) {
            case "":
                return profileURIProperty("wordpress_archive", defaultProperties)
            default:
                if (containsKey("wordpress_archive_$lang")) {
                    return profileURIProperty("wordpress_archive_$lang", defaultProperties)
                } else {
                    return profileURIProperty("wordpress_archive", defaultProperties)
                }
        }
    }

    /**
     * Returns to strip the Wordpress archive from the container directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_strip_archive[_<language>]"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    boolean getStripArchive() {
        String lang = language.toString()
        switch (lang) {
            case "":
                return profileBooleanProperty("wordpress_strip_archive", defaultProperties)
            default:
                if (containsKey("wordpress_strip_archive_$lang")) {
                    return profileBooleanProperty("wordpress_strip_archive_$lang", defaultProperties)
                } else {
                    return profileBooleanProperty("wordpress_strip_archive", defaultProperties)
                }
        }
    }

    /**
     * Returns the list of needed packages for Wordpress.
     *
     * <ul>
     * <li>profile property {@code "wordpress_packages"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    List getWordpressPackages() {
        profileListProperty "wordpress_packages", defaultProperties
    }

    /**
     * Returns the list of needed Apache mods.
     *
     * <ul>
     * <li>profile property {@code "wordpress_mods"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    List getWordpressMods() {
        profileListProperty "wordpress_mods", defaultProperties
    }

    /**
     * Wordpress configuration directory, for
     * example {@code "wordpress-3.8"}. If the path is relative then
     * the directory will be under the domain directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} for which the directory is returned.
     */
    File wordpressDir(Domain domain) {
        profileFileProperty "wordpress_directory", domainDir(domain), defaultProperties
    }

    /**
     * Wordpress linked configuration directory, for
     * example {@code "wordpress"}. If the path is relative then
     * the directory will be under the domain directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_linked_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     */
    File wordpressLinkedDir(def domain) {
        profileFileProperty "wordpress_linked_directory", domainDir(domain), defaultProperties
    }

    /**
     * Wordpress content cache directory, for
     * example {@code "wp-content/cache/"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_content_cache_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #wordpressDir(Object)
     */
    File wordpressContentCacheDir(def domain) {
        profileFileProperty "wordpress_content_cache_directory", wordpressDir(domain), defaultProperties
    }

    /**
     * Wordpress content plugins directory, for
     * example {@code "wp-content/plugins/"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_content_plugins_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #wordpressDir(Object)
     */
    File wordpressContentPluginsDir(def domain) {
        profileFileProperty "wordpress_content_plugins_directory", wordpressDir(domain), defaultProperties
    }

    /**
     * Wordpress content themes directory, for
     * example {@code "wp-content/themes/"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_content_themes_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #wordpressDir(Object)
     */
    File wordpressContentThemesDir(def domain) {
        profileFileProperty "wordpress_content_themes_directory", wordpressDir(domain), defaultProperties
    }

    /**
     * Wordpress content uploads directory, for
     * example {@code "wp-content/uploads/"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_content_uploads_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the domain for which the directory is returned.
     *
     * @see ApacheScript#getDefaultProperties()
     * @see #wordpressDir(Object)
     */
    File wordpressContentUploadsDir(def domain) {
        profileFileProperty "wordpress_content_uploads_directory", wordpressDir(domain), defaultProperties
    }

    /**
     * Returns the service alias directory path.
     *
     * @param service
     *            the Wordpress {@link WebService} web service.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @see #wordpressDir(def)
     */
    String serviceAliasDir(WebService service, Domain domain, Domain refDomain) {
        def serviceDir = serviceDir domain, refDomain
        service.alias.empty ? "$serviceDir/" : serviceDir
    }

    /**
     * Returns the service directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @see #wordpressDir(def)
     */
    String serviceDir(Domain domain, Domain refDomain) {
        refDomain == null ? wordpressDir(domain).absolutePath :
                wordpressDir(refDomain).absolutePath
    }

    /**
     * Returns the database default character set, for example {@code "utf8"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_charset"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getDatabaseDefaultCharset() {
        profileProperty("wordpress_database_default_charset", defaultProperties)
    }

    /**
     * Returns the database default collate, for example {@code ""}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_collate"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getDatabaseDefaultCollate() {
        profileProperty("wordpress_database_default_collate", defaultProperties)
    }

    /**
     * Returns the database default table prefix, for example {@code "wp_"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_table_prefix"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getDatabaseDefaultTablePrefix() {
        profileProperty("wordpress_database_default_table_prefix", defaultProperties)
    }

    /**
     * Returns to force secure log-in, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_force_secure_login"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    boolean getForceSecureLogin() {
        profileBooleanProperty("wordpress_force_secure_login", defaultProperties)
    }

    /**
     * Returns to force secure administrator, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_force_secure_admin"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    boolean getForceSecureAdmin() {
        profileBooleanProperty("wordpress_force_secure_admin", defaultProperties)
    }

    /**
     * Returns the configuration file character set, for
     * example {@code "ISO-8859-15"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_config_file_charset"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    Charset getConfigFileCharset() {
        profileCharsetProperty("wordpress_config_file_charset", defaultProperties)
    }

    /**
     * Returns the language, for example {@code "de_DE"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_language"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    Locale getLanguage() {
        profileLocaleProperty("wordpress_language", defaultProperties)
    }

    /**
     * Returns the authentication key, if empty the key is generated.
     *
     * <ul>
     * <li>profile property {@code "wordpress_auth_key"}</li>
     * </ul>
     *
     * @see <a href="https://api.wordpress.org/secret-key/1.1/salt/">Secret key salt [wordpress.org]</a>
     * @see ApacheScript#getDefaultProperties()
     */
    String getAuthKey() {
        def key = profileProperty("wordpress_auth_key", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getSecureAuthKey() {
        def key = profileProperty("wordpress_secure_auth_key", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getLoggedInKey() {
        def key = profileProperty("wordpress_logged_in_key", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getNonceKey() {
        def key = profileProperty("wordpress_nonce_key", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getAuthSalt() {
        def key = profileProperty("wordpress_auth_salt", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getSecureAuthSalt() {
        def key = profileProperty("wordpress_secure_auth_salt", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getLoggedInSalt() {
        def key = profileProperty("wordpress_logged_in_salt", defaultProperties)
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
     * @see ApacheScript#getDefaultProperties()
     */
    String getNonceSalt() {
        def key = profileProperty("wordpress_nonce_salt", defaultProperties)
        key.empty ? RandomStringUtils.randomAlphanumeric(64) : key
    }

    /**
     * Returns the theme archive for the specified name.
     *
     * @param name
     *            the name of the theme.
     *
     * @return the {@link URI} of the theme.
     */
    URI themeArchive(String name) {
        name = replace(name, "-", "_")
        name = replace(name, " ", "_")
        profileURIProperty "wordpress_$name", defaultProperties
    }

    /**
     * Returns the plug-in archive for the specified name.
     *
     * @param name
     *            the name of the plug-in.
     *
     * @return the {@link URI} of the plug-in.
     */
    URI pluginArchive(String name) {
        name = replace(name, "-", "_")
        name = replace(name, " ", "_")
        profileURIProperty "wordpress_$name", defaultProperties
    }

    /**
     * Returns the service name {@code "roundcube".}
     */
    String getServiceName() {
        NAME
    }

    /**
     * Sets the parent script with the properties.
     *
     * @param script
     *            the {@link ApacheScript}.
     */
    void setScript(ApacheScript script) {
        this.script = script
    }

    /**
     * Returns the parent script with the properties.
     *
     * @return the {@link ApacheScript}.
     */
    ApacheScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
