/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.apache_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.php.config.Php_5_Config

/**
 * <i>Roundcube PHP 5</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuRoundcubePhp_5_Config extends Php_5_Config {

    /**
     * Returns the path for the parent directory containing the sites.
     * For example {@code /var/www}.
     *
     * <ul>
     * <li>profile property {@code "sites_directory"}</li>
     * </ul>
     *
     * @see LinuxScript#getDefaultProperties()
     */
    File getSitesDirectory() {
        profileProperty("sites_directory", defaultProperties) as File
    }

    /**
     * Returns the file pattern for domain custom {@code php.ini} files,
     * for example {@code "%s_php.ini".}
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_domain_php_ini_file"}</li>
     * </ul>
     *
     * @see LinuxScript#getDefaultProperties()
     */
    String getPhpiniFileName() {
        profileProperty "php_fcgi_domain_php_ini_file", defaultProperties
    }

    /**
     * Returns the sub-directory to save the php/fcgi scripts.
     * For example {@code "php-fcgi-scripts"}.
     *
     * <ul>
     * <li>profile property {@code "php_fcgi_scripts_subdirectory"}</li>
     * </ul>
     *
     * @see LinuxScript#getDefaultProperties()
     */
    String getScriptsSubdirectory() {
        profileProperty "php_fcgi_scripts_subdirectory", defaultProperties
    }

    /**
     * Returns enabled the magic_quotes state for GPC (Get/Post/Cookie)
     * operations, for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_magic_quotes_gpc"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/de/info.configuration.php#ini.magic-quotes-gpc">magic_quotes_gpc</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getMagicQuotesGpcEnabled() {
        profileBooleanProperty "roundcube_php_magic_quotes_gpc", roundcubeProperties
    }

    /**
     * Returns enabled the quotes escaped with a backslash,
     * for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_magic_quotes_runtime"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/info.configuration.php#ini.magic-quotes-runtime">magic_quotes_runtime</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getMagicQuotesRuntimeEnabled() {
        profileBooleanProperty "roundcube_php_magic_quotes_runtime", roundcubeProperties
    }

    /**
     * Returns enabled the single-quote escape,
     * for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_magic_quotes_sybase"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/sybase.configuration.php#ini.magic-quotes-sybase">magic_quotes_sybase</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getMagicQuotesSybaseEnabled() {
        profileBooleanProperty "roundcube_php_magic_quotes_sybase", roundcubeProperties
    }

    /**
     * Returns allowed HTTP file uploads,
     * for example {@code "true".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_file_uploads"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.file-uploads">file_uploads</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getFileUploadsEnabled() {
        profileBooleanProperty "roundcube_php_file_uploads", roundcubeProperties
    }

    /**
     * Returns enabled that the session module starts a session automatically,
     * for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_session_auto_start"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/session.configuration.php#ini.session.auto-start">session.auto_start</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getSessionAutoStartEnabled() {
        profileBooleanProperty "roundcube_php_session_auto_start", roundcubeProperties
    }

    /**
     * Returns enabled the compatibility mode with Zend Engine 1,
     * for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_zend_ze1_compatibility_mode"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/ini.core.php#ini.zend.ze1-compatibility-mode">zend.ze1_compatibility_mode</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getZendZe1CompatibilityModeEnabled() {
        profileBooleanProperty "roundcube_php_zend_ze1_compatibility_mode", roundcubeProperties
    }

    /**
     * Returns enabled {@code suhosin.session.encrypt},
     * for example {@code "false".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_suhosin_session_encrypt"}</li>
     * </ul>
     *
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Boolean getSuhosinSessionEncryptEnabled() {
        profileBooleanProperty "roundcube_php_suhosin_session_encrypt", roundcubeProperties
    }

    /**
     * Returns the overloads a set of single byte functions by the
     * mbstring counterparts,
     * for example {@code "0".}
     *
     * <ul>
     * <li>profile property {@code "roundcube_php_mbstring_func_overload"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/mbstring.configuration.php#ini.mbstring.func-overload">mbstring.func_overload</a>
     * @see UbuntuApacheRoundcubeConfig#getRoundcubeProperties()
     */
    Integer getMbstringFuncOverload() {
        profileNumberProperty "roundcube_php_mbstring_func_overload", roundcubeProperties
    }
}
