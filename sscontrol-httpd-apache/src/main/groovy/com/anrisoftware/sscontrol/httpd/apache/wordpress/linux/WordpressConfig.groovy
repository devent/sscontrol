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

import static org.apache.commons.lang3.StringUtils.*

import java.nio.charset.Charset

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.httpd.statements.wordpress.ForceFactory
import com.anrisoftware.sscontrol.httpd.statements.wordpress.OverrideMode
import com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressService

/**
 * Wordpress.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WordpressConfig {

    public static final String NAME = "wordpress"

    @Inject
    private WordpressConfigLogger log

    @Inject
    private ForceFactory forceFactory

    private LinuxScript script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Sets default prefix.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void setupDefaultPrefix(WordpressService service) {
        if (service.prefix == null) {
            service.prefix = wordpressDefaultPrefix
        }
    }

    /**
     * Sets default override mode.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void setupDefaultOverrideMode(WordpressService service) {
        if (service.overrideMode == null) {
            service.overrideMode = wordpressDefaultOverrideMode
        }
    }

    /**
     * Sets default force SSL login and admin.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     */
    void setupDefaultForce(WordpressService service) {
        if (service.force == null) {
            def force = forceFactory.create(["login": forceSecureLogin, "admin": forceSecureAdmin])
            service.force = force
        }
    }

    /**
     * Returns the Wordpress archive.
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    URI getWordpressArchive() {
        String lang = language.toString()
        String property
        URI uri
        switch (lang) {
            case "":
                property = "wordpress_archive"
                break
            default:
                property = "wordpress_archive_$lang"
                if (!containsKey(property)) {
                    property = "wordpress_archive"
                }
        }
        uri = profileURIProperty property, wordpressProperties
        log.returnsWordpressArchive script, lang, uri
        return uri
    }

    /**
     * Returns the Wordpress archive hash.
     *
     * <ul>
     * <li>profile property {@code "wordpress_archive_hash[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    URI getWordpressArchiveHash() {
        String lang = language.toString()
        String property
        URI uri
        switch (lang) {
            case "":
                property = "wordpress_archive_hash"
                break
            default:
                property = "wordpress_archive_hash_$lang"
        }
        if (containsKey(property)) {
            uri = profileURIProperty property, wordpressProperties
            log.returnsWordpressArchiveHash script, lang, uri
            return uri
        }
    }

    /**
     * Returns to strip the Wordpress archive from the container directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_strip_archive[_<language>]"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getStripArchive() {
        String lang = language.toString()
        String property
        boolean strip
        switch (lang) {
            case "":
                property = "wordpress_strip_archive"
                break
            default:
                property = "wordpress_strip_archive_$lang"
                if (!containsKey(property)) {
                    property = "wordpress_strip_archive"
                }
        }
        strip = profileBooleanProperty property, wordpressProperties
        log.returnsStripArchive script, lang, strip
        return strip
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
    String getWordpressDefaultPrefix() {
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
    OverrideMode getWordpressDefaultOverrideMode() {
        def mode = profileProperty "wordpress_default_override_mode", wordpressProperties
        OverrideMode.valueOf mode
    }

    /**
     * Returns the Wordpress installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
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
     * Wordpress content cache directory, for
     * example {@code "wp-content/cache/"}. If the path is relative then
     * the file will be under the Wordpress installation directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_content_cache_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Domain, WordpressService)
     */
    File wordpressContentCacheDir(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_content_cache_directory", dir, wordpressProperties
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
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Domain, WordpressService)
     */
    File wordpressContentPluginsDir(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_content_plugins_directory", dir, wordpressProperties
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
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Domain, WordpressService)
     */
    File wordpressContentThemesDir(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_content_themes_directory", dir, wordpressProperties
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
     *            the {@link Domain} of the Wordpress service.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
     *
     * @see #getWordpressProperties()
     * @see #wordpressDir(Domain, WordpressService)
     */
    File wordpressContentUploadsDir(Domain domain, WordpressService service) {
        def dir = wordpressDir(domain, service)
        profileFileProperty "wordpress_content_uploads_directory", dir, wordpressProperties
    }

    /**
     * Returns the service alias directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
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
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link WordpressService} Wordpress service.
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
     * Returns the database default character set, for example {@code "utf8"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_charset"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDatabaseDefaultCharset() {
        profileProperty "wordpress_database_default_charset", wordpressProperties
    }

    /**
     * Returns the database default collate, for example {@code ""}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_collate"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDatabaseDefaultCollate() {
        profileProperty "wordpress_database_default_collate", wordpressProperties
    }

    /**
     * Returns the database default table prefix, for example {@code "wp_"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_database_default_table_prefix"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    String getDatabaseDefaultTablePrefix() {
        profileProperty "wordpress_database_default_table_prefix", wordpressProperties
    }

    /**
     * Returns to force secure log-in, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_force_secure_login"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getForceSecureLogin() {
        profileBooleanProperty "wordpress_force_secure_login", wordpressProperties
    }

    /**
     * Returns to force secure administrator, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_force_secure_admin"}</li>
     * </ul>
     *
     * @see #getWordpressProperties()
     */
    boolean getForceSecureAdmin() {
        profileBooleanProperty "wordpress_force_secure_admin", wordpressProperties
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
    Locale getLanguage() {
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
     * Returns the default Wordpress properties.
     */
    abstract ContextProperties getWordpressProperties()

    /**
     * Returns the service name {@code "wordpress".}
     */
    String getServiceName() {
        NAME
    }

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
    }

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
