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

import java.nio.charset.Charset

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript

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
     * <li>profile property {@code "wordpress_archive"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    URI getWordpressArchive() {
        profileURIProperty("wordpress_archive", defaultProperties)
    }

    /**
     * Returns the list of needed packages for Wordpress web mail.
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
     * Wordpress installation directory, for
     * example {@code "wordpress"}. If the path is relative then
     * the directory will be under the local software directory.
     *
     * <ul>
     * <li>profile property {@code "wordpress_directory"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see ApacheScript#getLocalSoftwareDir()
     */
    File getWordpressDir() {
        profileFileProperty("wordpress_directory", localSoftwareDir, defaultProperties)
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
     * Returns to generate keys and salts, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "wordpress_generate_keys"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    boolean getGenerateKeys() {
        profileBooleanProperty("wordpress_generate_keys", defaultProperties)
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
