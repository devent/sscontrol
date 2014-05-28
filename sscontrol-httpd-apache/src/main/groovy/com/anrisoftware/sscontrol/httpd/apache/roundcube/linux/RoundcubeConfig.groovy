/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.roundcube.linux

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.apache.roundcube.api.RoundcubeDatabaseConfig
import com.anrisoftware.sscontrol.httpd.domain.DomainImpl;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Roundcube.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class RoundcubeConfig {

    public static final String NAME = "roundcube"

    @Inject
    private RoundcubeConfigLogger log

    @Inject
    private Map<String, RoundcubeDatabaseConfig> databaseConfigs

    private LinuxScript script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, java.util.List)
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#deployService(Domain, WebService, java.util.List)
     */
    abstract void deployService(Domain domain, WebService service, List config)

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
        log.checkDatabaseConfig config, script, databaseBackend
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
     * @see #getRoundcubeProperties()
     */
    URI getRoundcubeArchive() {
        profileURIProperty("roundcube_archive", roundcubeProperties)
    }

    /**
     * Returns the list of needed packages for Roundcube web mail.
     *
     * <ul>
     * <li>profile property {@code "roundcube_packages"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    List getRoundcubePackages() {
        profileListProperty "roundcube_packages", roundcubeProperties
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
     * @see #getRoundcubeProperties()
     * @see ApacheScript#getLocalSoftwareDir()
     */
    File getRoundcubeDir() {
        profileFileProperty("roundcube_directory", localSoftwareDir, roundcubeProperties)
    }

    /**
     * Returns the Roundcube database back-end, for example {@code "mysql"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_backend"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDatabaseBackend() {
        profileProperty("roundcube_database_backend", roundcubeProperties)
    }

    /**
     * Returns the SMTP default host, for example {@code "tls://%h"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_host"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getSmtpDefaultHost() {
        profileProperty("roundcube_smtp_default_host", roundcubeProperties)
    }

    /**
     * Returns the SMTP default user, for example {@code "%u"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_user"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getSmtpDefaultUser() {
        profileProperty("roundcube_smtp_default_user", roundcubeProperties)
    }

    /**
     * Returns the SMTP default user, for example {@code "%p"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_default_password"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getSmtpDefaultPassword() {
        profileProperty("roundcube_smtp_default_password", roundcubeProperties)
    }

    /**
     * Returns the logging driver, for example {@code "syslog", "file"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_log_driver"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getLogDriver() {
        profileProperty("roundcube_log_driver", roundcubeProperties)
    }

    /**
     * Returns the logging facility, for example {@code "LOG_MAIL"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_log_facility"}</li>
     * </ul>
     *
     * @see <a href="http://php.net/manual/en/function.openlog.php">openlog [php.net]</a>
     * @see #getRoundcubeProperties()
     */
    String getLogFacility() {
        profileProperty("roundcube_log_facility", roundcubeProperties)
    }

    /**
     * Returns the IMAP authentication type, for example {@code "LOG_MAIL"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_auth_type"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getImapAuthType() {
        def type = profileProperty("roundcube_imap_auth_type", roundcubeProperties)
        type.isEmpty() ? null : type
    }

    /**
     * Returns the IMAP folder delimiter, for example {@code "/"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_delimiter"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getImapDelimiter() {
        def delimiter = profileProperty("roundcube_imap_delimiter", roundcubeProperties)
        delimiter.isEmpty() ? null : delimiter
    }

    /**
     * Returns the IMAP NAMESPACE extension property.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_personal"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getImapNsPersonal() {
        def option = profileProperty("roundcube_imap_ns_personal", roundcubeProperties)
        option.isEmpty() ? null : option
    }

    /**
     * Returns the IMAP NAMESPACE extension property.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_other"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getImapNsOther() {
        def option = profileProperty("roundcube_imap_ns_other", roundcubeProperties)
        option.isEmpty() ? null : option
    }

    /**
     * Returns the IMAP NAMESPACE extension property.
     *
     * <ul>
     * <li>profile property {@code "roundcube_imap_ns_shared"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getImapNsShared() {
        def option = profileProperty("roundcube_imap_ns_shared", roundcubeProperties)
        option.isEmpty() ? null : option
    }

    /**
     * Returns to enable the installer after configuration.
     *
     * <ul>
     * <li>profile property {@code "roundcube_enable_installer"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getEnableInstaller() {
        profileBooleanProperty("roundcube_enable_installer", roundcubeProperties)
    }

    /**
     * Returns to force HTTPS.
     *
     * <ul>
     * <li>profile property {@code "roundcube_force_https"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getForceHttps() {
        profileBooleanProperty("roundcube_force_https", roundcubeProperties)
    }

    /**
     * Returns the level of auto-complete on log-in.
     * <ul>
     * <li>0: disabled,
     * <li>1: user name and host only,
     * <li>2: user name, host, password.</li>
     * </ul>
     *
     * <ul>
     * <li>profile property {@code "roundcube_login_autocomplete"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    int getLoginAutocomplete() {
        profileNumberProperty("roundcube_login_autocomplete", roundcubeProperties)
    }

    /**
     * Returns to check client IP in session authorization.
     *
     * <ul>
     * <li>profile property {@code "roundcube_ip_check"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getIpCheck() {
        profileBooleanProperty("roundcube_ip_check", roundcubeProperties)
    }

    /**
     * Returns that the log-in is case sensitive.
     *
     * <ul>
     * <li>profile property {@code "roundcube_login_case_sensitive"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getLoginCaseSensitive() {
        profileBooleanProperty("roundcube_login_case_sensitive", roundcubeProperties)
    }

    /**
     * Returns to automatically create a new Roundcube user
     * when log-in the first time.
     *
     * <ul>
     * <li>profile property {@code "roundcube_auto_create_user"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getAutoCreateUser() {
        profileBooleanProperty("roundcube_auto_create_user", roundcubeProperties)
    }

    /**
     * Returns the user agent string.
     *
     * <ul>
     * <li>profile property {@code "roundcube_useragent"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getUserAgent() {
        profileProperty("roundcube_useragent", roundcubeProperties)
    }

    /**
     * Returns the identities access level:
     * <ul>
     * <li>0: many identities with possibility to edit all parameters,
     * <li>1: many identities with possibility to edit all parameters but not email address,
     * <li>2: one identity with possibility to edit all parameters,</li>
     * <li>3: one identity with possibility to edit all parameters but not email address,</li>
     * <li>4: one identity with possibility to edit only signature.</li>
     * </ul>
     *
     * <ul>
     * <li>profile property {@code "roundcube_identities_level"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    int getIdentitiesLevel() {
        profileNumberProperty("roundcube_identities_level", roundcubeProperties)
    }

    /**
     * Returns use of the built-in spell checker.
     *
     * <ul>
     * <li>profile property {@code "roundcube_enable_spellcheck"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getEnableSpellcheck() {
        profileBooleanProperty("roundcube_enable_spellcheck", roundcubeProperties)
    }

    /**
     * Returns the DES key.
     *
     * <ul>
     * <li>profile property {@code "roundcube_des_key"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDesKey() {
        def key = profileProperty("roundcube_des_key", roundcubeProperties)
        key.isEmpty() ? RandomStringUtils.randomAlphanumeric(24) : key
    }

    /**
     * Returns the service name {@code "roundcube".}
     */
    String getServiceName() {
        NAME
    }

    /**
     * Returns the default Roundcube properties.
     */
    abstract ContextProperties getRoundcubeProperties()

    /**
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
        databaseConfigs.each { it.value.script = script }
    }

    /**
     * Returns the parent script with the properties.
     *
     * @return the {@link LinuxScript}.
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
