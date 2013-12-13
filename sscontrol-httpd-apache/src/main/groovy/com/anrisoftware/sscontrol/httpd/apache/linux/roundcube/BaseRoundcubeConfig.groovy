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

import javax.inject.Inject

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService

/**
 * Returns roundcube/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseRoundcubeConfig {

    public static final String NAME = "roundcube"

    @Inject
    private BaseRoundcubeConfigLogger log

    @Inject
    private Map<String, RoundcubeDatabaseConfig> databaseConfigs

    private ApacheScript script

    /**
     * Setups the database.
     *
     * @param service
     *            the {@link RoundcubeService}.
     *
     * @see #getDatabaseBackend()
     */
    void setupDatabase(RoundcubeService service) {
        def config = databaseConfigs[databaseBackend]
        log.checkDatabaseConfig script, config, databaseBackend
        config.setScript script
        config.setupDatabase service
    }

    /**
     * Returns the Roundcube web mail distribution archive.
     *
     * <ul>
     * <li>profile property {@code "roundcube_archive"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    URI getRoundcubeArchive() {
        profileURIProperty("roundcube_archive", defaultProperties)
    }

    /**
     * Returns the list of needed packages for Roundcube web mail.
     *
     * <ul>
     * <li>profile property {@code "roundcube_packages"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    List getRoundcubePackages() {
        profileListProperty "roundcube_packages", defaultProperties
    }

    /**
     * Roundcube installation directory, for
     * example {@code "roundcube"}. If the path is relative then
     * the directory will be under the local software directory.
     *
     * <ul>
     * <li>profile property {@code "roundcube_directory"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     * @see ApacheScript#getLocalSoftwareDir()
     */
    File getRoundcubeDir() {
        profileFileProperty("roundcube_directory", localSoftwareDir, defaultProperties)
    }

    /**
     * Returns the Roundcube database back-end, for example {@code "mysql"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_backend"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getDatabaseBackend() {
        profileProperty("roundcube_database_backend", defaultProperties)
    }

    /**
     * Returns the SMTP default host, for example {@code "tls://%h"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_host"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getSmtpDefaultHost() {
        profileProperty("roundcube_smtp_default_host", defaultProperties)
    }

    /**
     * Returns the SMTP default user, for example {@code "%u"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_user"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getSmtpDefaultUser() {
        profileProperty("roundcube_smtp_default_user", defaultProperties)
    }

    /**
     * Returns the SMTP default user, for example {@code "%p"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_password"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getSmtpDefaultPassword() {
        profileProperty("roundcube_smtp_default_password", defaultProperties)
    }

    /**
     * Returns the logging driver, for example {@code "syslog", "file"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_log_driver"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    String getLogDriver() {
        profileProperty("roundcube_log_driver", defaultProperties)
    }

    /**
     * Returns the logging facility, for example {@code "LOG_MAIL"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_log_facility"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/function.openlog.php">openlog [php.net]</a>
     * @see ApacheScript#getDefaultProperties()
     */
    String getLogFacility() {
        profileProperty("roundcube_log_facility", defaultProperties)
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
        databaseConfigs.each { it.value.script = script }
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
