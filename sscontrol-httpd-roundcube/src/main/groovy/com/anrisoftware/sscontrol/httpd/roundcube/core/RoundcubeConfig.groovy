/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.roundcube.core

import static org.apache.commons.lang3.StringUtils.*

import javax.inject.Inject

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
     * Sets default prefix.
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
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultDebugLevels(RoundcubeService service) {
        int roundcubeLevel = profileNumberProperty "roundcube_default_debug_level_roundcube", roundcubeProperties
        int smtplogLevel = profileNumberProperty "roundcube_default_debug_level_smtplog", roundcubeProperties
        int loginsLevel = profileNumberProperty "roundcube_default_debug_level_logins", roundcubeProperties
        int sessionLevel = profileNumberProperty "roundcube_default_debug_level_session", roundcubeProperties
        int sqlLevel = profileNumberProperty "roundcube_default_debug_level_sql", roundcubeProperties
        int imapLevel = profileNumberProperty "roundcube_default_debug_level_imap", roundcubeProperties
        int ldapLevel = profileNumberProperty "roundcube_default_debug_level_ldap", roundcubeProperties
        int smtpLevel = profileNumberProperty "roundcube_default_debug_level_smtp", roundcubeProperties
        int phpLevel = profileNumberProperty "roundcube_default_debug_level_php", roundcubeProperties
        if (service.debug == null) {
            service.debug "roundcube", level: roundcubeLevel
            service.debug "smtplog", level: smtplogLevel
            service.debug "logins", level: loginsLevel
            service.debug "session", level: sessionLevel
            service.debug "sql", level: sqlLevel
            service.debug "imap", level: imapLevel
            service.debug "ldap", level: ldapLevel
            service.debug "php", level: phpLevel
        }
        service.debug.roundcube == null ? service.debug("roundcube", level: roundcubeLevel) : false
        service.debug.smtplog == null ? service.debug("smtplog", level: smtplogLevel) : false
        service.debug.logins == null ? service.debug("logins", level: loginsLevel) : false
        service.debug.session == null ? service.debug("session", level: sessionLevel) : false
        service.debug.sql == null ? service.debug("sql", level: sqlLevel) : false
        service.debug.imap == null ? service.debug("imap", level: imapLevel) : false
        service.debug.ldap == null ? service.debug("ldap", level: ldapLevel) : false
        service.debug.php == null ? service.debug("php", level: phpLevel) : false
    }

    /**
     * Sets default database settings.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultDatabase(RoundcubeService service) {
        def databaseName = service.database.database
        def databaseHost = profileProperty "roundcube_default_database_host", roundcubeProperties
        def databaseDriver = profileProperty "roundcube_default_database_driver", roundcubeProperties
        // database "roundcubedb", user: "userdb", password: "userpassdb", host: "localhost", driver: "mysql"
        service.database.host == null ? service.database(databaseName, host: databaseHost) : false
        service.database.driver == null ? service.database(databaseName, driver: databaseDriver) : false
    }

    /**
     * Sets default SMTP settings.
     *
     * @param service
     *            the {@link RoundcubeService} service.
     */
    void setupDefaultSmtp(RoundcubeService service) {
        def smtpHost = profileProperty "roundcube_default_smtp_host", roundcubeProperties
        def smtpUser = profileProperty "roundcube_default_smtp_user", roundcubeProperties
        def smtpPassword = profileProperty "roundcube_default_smtp_password", roundcubeProperties
        // smtp "tls://%h", user: "usersmtp", password: "passwordsmtp"
        service.smtpServer.server == null ? service.smtp(smtpHost) : false
        def smtpServer = service.smtpServer.servers
        service.smtpServer.user == null ? service.smtp(smtpServer, user: smtpUser) : false
        service.smtpServer.password == null ? service.smtp(smtpServer, password: smtpPassword) : false
    }

    /**
     * Returns the list of needed packages for the <i>Roundcube</i> service.
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
     * Returns the list of needed <i>Apache</i> mods.
     *
     * <ul>
     * <li>profile property {@code "roundcube_mods"}</li>
     * </ul>
     *
     * @see #getRoundcubeProperties()
     */
    List getRoundcubeMods() {
        profileListProperty "roundcube_mods", roundcubeProperties
    }

    /**
     * Returns default <i>Roundcube</i> service prefix, for
     * example {@code "roundcube_1_0"}.
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
     * @see #getRoundcubeProperties()
     */
    List roundcubeDatabasePackages(String driver) {
        profileListProperty "roundcube_${driver}_packages", roundcubeProperties
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
