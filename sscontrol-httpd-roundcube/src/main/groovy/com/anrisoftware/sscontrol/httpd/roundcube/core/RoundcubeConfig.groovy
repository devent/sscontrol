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
package com.anrisoftware.sscontrol.httpd.roundcube.core

import static org.apache.commons.lang3.StringUtils.*

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

/**
 * <i>Roundcube</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class RoundcubeConfig {

    public static final String NAME = "roundcube"

    @Inject
    private RoundcubeConfigLogger log

    private LinuxScript script

    /**
     * Sets default arguments.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaults(RoundcubeService service) {
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultDebugLevels service
        setupDefaultDatabase service
        setupDefaultSmtp service
        setupDefaultImap service
        setupDefaultPlugins service
        setupDefaultMisc service
    }

    /**
     * Sets default prefix.
     *
     * @see #getRoundcubeDefaultPrefix()
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultPrefix(RoundcubeService service) {
        if (service.prefix == null) {
            service.prefix = roundcubeDefaultPrefix
        }
    }

    /**
     * Sets default override mode.
     *
     * @see #getRoundcubeDefaultOverrideMode()
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultOverrideMode(RoundcubeService service) {
        if (service.overrideMode == null) {
            service.override mode: roundcubeDefaultOverrideMode
        }
    }

    /**
     * Sets default debug levels.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_debug_level_roundcube"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_smtplog"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_logins"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_session"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_sql"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_imap"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_ldap"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_smtp"}</li>
     * <li>profile property {@code "roundcube_default_debug_level_php"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultDebugLevels(RoundcubeService service) {
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "roundcube", level: (int) profileNumberProperty("roundcube_default_debug_level_roundcube", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "smtplog", level: (int) profileNumberProperty("roundcube_default_debug_level_smtplog", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "logins", level: (int) profileNumberProperty("roundcube_default_debug_level_logins", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "session", level: (int) profileNumberProperty("roundcube_default_debug_level_session", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "sql", level: (int) profileNumberProperty("roundcube_default_debug_level_sql", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "imap", level: (int) profileNumberProperty("roundcube_default_debug_level_imap", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "ldap", level: (int) profileNumberProperty("roundcube_default_debug_level_ldap", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "smtp", level: (int) profileNumberProperty("roundcube_default_debug_level_smtp", roundcubeProperties)
        }
        if (service.debugLogging("level") == null || service.debugLogging("level").roundcube == null) {
            service.debug "php", level: (int) profileNumberProperty("roundcube_default_debug_level_php", roundcubeProperties)
        }
    }

    /**
     * Sets default database settings.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_database_host"}</li>
     * <li>profile property {@code "roundcube_default_database_port"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultDatabase(RoundcubeService service) {
        def databaseName = service.database.database
        if (service.database.host == null) {
            service.database databaseName, host: profileProperty("roundcube_default_database_host", roundcubeProperties)
        }
        if (service.database.port == null) {
            service.database databaseName, port: profileProperty("roundcube_default_database_port", roundcubeProperties)
        }
    }

    /**
     * Sets default SMTP settings.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_smtp_host"}</li>
     * <li>profile property {@code "roundcube_default_smtp_user"}</li>
     * <li>profile property {@code "roundcube_default_smtp_password"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultSmtp(RoundcubeService service) {
        if (service.mailServer == null || service.mailServer.mail == null) {
            service.mail profileProperty("roundcube_default_smtp_host", roundcubeProperties)
        }
        def mail = service.mailServer.mail
        if (service.mailServer.user == null) {
            service.mail mail, user: profileProperty("roundcube_default_smtp_user", roundcubeProperties)
        }
        if (service.mailServer.password == null) {
            service.mail mail, password: profileProperty("roundcube_default_smtp_password", roundcubeProperties)
        }
    }

    /**
     * Sets default IMAP settings.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_imap_port"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultImap(RoundcubeService service) {
        // server "default", port: 143
        if (service.imapPort == null) {
            service.server "default", port: profileNumberProperty("roundcube_default_imap_port", roundcubeProperties)
        }
    }

    /**
     * Sets default plug-ins.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_plugins"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultPlugins(RoundcubeService service) {
        // plugins: plugins
        if (service.plugins == null) {
            service.plugins profileListProperty("roundcube_default_plugins", roundcubeProperties)
        }
    }

    /**
     * Sets default misc.
     *
     * <ul>
     * <li>profile property {@code "roundcube_product_name"}</li>
     * </ul>
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultMisc(RoundcubeService service) {
        // product name: name
        if (service.productName == null) {
            service.product name: profileProperty("roundcube_product_name", roundcubeProperties)
        }
    }

    /**
     * Returns the list of needed packages for the service.
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
     * Returns default <i>Roundcube</i> service prefix, for
     * example {@code "roundcube_1"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_prefix"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeDefaultPrefix() {
        profileProperty "roundcube_default_prefix", roundcubeProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_default_override_mode"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    OverrideMode getRoundcubeDefaultOverrideMode() {
        def mode = profileProperty "roundcube_default_override_mode", roundcubeProperties
        OverrideMode.valueOf mode
    }

    /**
     * Returns the <i>Roundcube</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see RoundcubeService#getPrefix()
     */
    File roundcubeDir(Domain domain, RoundcubeService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>Roundcube</i> service alias directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #wordpressDir(def)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, RoundcubeService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "$serviceDir/" : serviceDir
    }

    /**
     * Returns the <i>Roundcube</i> service directory path.
     *
     * @param domain
     *            the {@link Domain} for which the path is returned.
     *
     * @param refDomain
     *            the references {@link Domain} or {@code null}.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     *
     * @see #wordpressDir(Domain, RoundcubeService)
     */
    String serviceDir(Domain domain, Domain refDomain, RoundcubeService service) {
        refDomain == null ? roundcubeDir(domain, service).absolutePath :
                roundcubeDir(refDomain, service).absolutePath
    }

    /**
     * Returns the list of needed packages for database driver.
     *
     * <ul>
     * <li>profile property {@code "roundcube_${driver}_packages"}</li>
     * </ul>
     *
     * @param driver
     *            the database {@link String} driver.
     *
     * @see #getRoundcubeProperties()
     */
    List roundcubeDatabasePackages(String driver) {
        profileListProperty "roundcube_${driver}_packages", roundcubeProperties
    }

    /**
     * Returns the database command for the specified database driver.
     *
     * <ul>
     * <li>profile property {@code "${driver}_command"}</li>
     * </ul>
     *
     * @param driver
     *            the database {@link String} driver.
     *
     * @see #getDefaultProperties()
     */
    String roundcubeDatabaseCommand(String driver) {
        log.checkDatabaseDriver this, driver
        profileProperty "${driver}_command", defaultProperties
    }

    /**
     * Returns database table prefix, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_database_table_prefix"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRoundcubeDatabaseTablePrefix() {
        profileProperty "roundcube_database_table_prefix", roundcubeProperties
    }

    /**
     * Returns the DES key. If the property is empty then the key is
     * auto-generated.
     *
     * <ul>
     * <li>profile property {@code "roundcube_des_key"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getRedmineDesKey() {
        def value = profileProperty "roundcube_des_key", roundcubeProperties
        if (StringUtils.isBlank(value)) {
            return RandomStringUtils.random(24, true, true)
        } else {
            return value
        }
    }

    /**
     * Returns the SMTP server port, for example {@code "25"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_server_port"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    int getSmtpServerPort() {
        profileNumberProperty "roundcube_smtp_server_port", roundcubeProperties
    }

    /**
     * Returns the SMTP authentication type, for example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "roundcube_smtp_server_auth_type"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getSmtpServerAuthType() {
        profileProperty "roundcube_smtp_server_auth_type", roundcubeProperties
    }

    /**
     * Returns the debug driver, for example {@code "syslog"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_debug_driver"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDebugDriver() {
        profileProperty "roundcube_debug_driver", roundcubeProperties
    }

    /**
     * Returns the debug date format, for example {@code "d-M-Y H:i:s O"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_debug_date_format"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDebugDateFormat() {
        profileProperty "roundcube_debug_date_format", roundcubeProperties
    }

    /**
     * Returns the debug syslog ID, for example {@code "roundcube"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_debug_syslog_id"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDebugSyslogId() {
        profileProperty "roundcube_debug_syslog_id", roundcubeProperties
    }

    /**
     * Returns the debug syslog facility, for example {@code "LOG_USER"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_debug_syslog_facility"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    String getDebugSyslogFacility() {
        profileProperty "roundcube_debug_syslog_facility", roundcubeProperties
    }

    /**
     * Returns enabled the debug per user logging, for example {@code "false"}.
     *
     * <ul>
     * <li>profile property {@code "roundcube_debug_per_user_logging"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    boolean getDebugPerUserLogging() {
        profileProperty "roundcube_debug_per_user_logging", roundcubeProperties
    }

    /**
     * Returns the <i>Roundcube</i> properties.
     */
    abstract ContextProperties getRoundcubeProperties()

    /**
     * Returns the service name {@code "roundcube".}
     */
    String getServiceName() {
        NAME
    }

    /**
     * Returns the profile name for the configuration.
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
     * Delegates the missing property to the parent script.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates the missing method to the parent script.
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
