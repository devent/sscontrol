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

/**
 * Returns roundcube/properties.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BaseRoundcubeConfig {

    public static final String NAME = "roundcube"

    ApacheScript script

    @Inject
    Map<String, RoundcubeDatabaseConfig> databaseConfigs

    void setScript(ApacheScript script) {
        this.script = script
    }

    /**
     * Returns the service name {@code "roundcube".}
     */
    String getServiceName() {
        NAME
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
    URL getRoundcubeArchive() {
        profileURIProperty "roundcube_archive", defaultProperties
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
     * Roundcube main configuration file, for example {@code "main.inc.php"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_main_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    File getConfigurationFile() {
        profileProperty("roundcube_main_file", defaultProperties) as File
    }

    /**
     * Roundcube database configuration file, for
     * example {@code "db.inc.php"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_file"}</li>
     * </ul>
     *
     * @see ApacheScript#getDefaultProperties()
     */
    File getDatabaseScriptFile() {
        profileProperty("roundcube_database_file", defaultProperties) as File
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
