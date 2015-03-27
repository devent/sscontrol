/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.core

import javax.inject.Inject

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.yourls.Access
import com.anrisoftware.sscontrol.httpd.yourls.Convert
import com.anrisoftware.sscontrol.httpd.yourls.YourlsService

/**
 * <i>Yourls 1.7</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Yourls_1_7_Config {

    private LinuxScript script

    @Inject
    private Yourls_1_7_ConfigLogger log

    @Inject
    private Yourls_1_7_ConfigFile yourlsConfigFile

    @Inject
    private Yourls_1_7_Permissions yourlsPermissions

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupDefaults(Domain domain, YourlsService service) {
        setupDefaultService domain, service
        setupDefaultDebug domain, service
        setupDefaultDatabase domain, service
    }

    /**
     * Sets default service settings.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupDefaultService(Domain domain, YourlsService service) {
        if (service.alias == null) {
            service.alias = yourlsDefaultAlias
        }
        if (service.prefix == null) {
            service.prefix = yourlsDefaultPrefix
        }
        if (service.site == null) {
            service.site "${domain.proto}${domain.name}"
        }
        if (service.language == null) {
            service.language yourlsDefaultLanguage
        }
        if (service.overrideMode == null) {
            service.override mode: yourlsDefaultOverrideMode
        }
        if (service.siteAccess == null) {
            service.access yourlsDefaultSiteAccess
        }
        if (service.statsAccess == null) {
            service.access stats: yourlsDefaultStatsAccess
        }
        if (service.apiAccess == null) {
            service.access api: yourlsDefaultApiAccess
        }
        if (service.gmtOffset == null && yourlsDefaultGmtOffset != null) {
            service.gmt offset: yourlsDefaultOverrideMode
        }
        if (service.uniqueUrls == null) {
            service.unique urls: yourlsDefaultUniqueUrls
        }
        if (service.urlConvertMode == null) {
            service.convert mode: yourlsDefaultUrlConvertMode
        }
        if (service.reserved == null) {
            service.reserved yourlsDefaultReservedKeywords.join(",")
        }
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupDefaultDebug(Domain domain, YourlsService service) {
        if (service.debugLogging("level") == null || service.debugLogging("level")["yourls"] == null) {
            service.debug "yourls", level: yourlsDefaultDebugYourlsLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["php"] == null) {
            service.debug "php", level: yourlsDefaultDebugPhpLevel
        }
        log.setupDefaultDebug this, domain, service
    }

    /**
     * Setups the default database.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupDefaultDatabase(Domain domain, YourlsService service) {
        def db = service.database.database
        if (service.database.host == null) {
            service.database db, host: yourlsDefaultDatabaseHost
        }
        if (service.database.port == null) {
            service.database db, port: yourlsDefaultDatabasePort
        }
        if (service.database.prefix == null) {
            service.database db, prefix: yourlsDefaultDatabaseTablePrefix
        }
        if (service.database.driver == null && !yourlsDefaultDatabaseDriver.empty) {
            service.database db, driver: yourlsDefaultDatabaseDriver
        }
        log.setupDefaultDatabase this, domain, service
    }

    /**
     * Deploys the <i>Yourls</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link YourlsService}.
     *
     * @see #yourlsConfigFile(Domain, YourlsService)
     */
    void deployConfig(Domain domain, YourlsService service) {
        yourlsConfigFile.deployConfig domain, service
    }

    /**
     * Sets the owner and permissions of the <i>Yourls</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link YourlsService} service.
     */
    void setupPermissions(Domain domain, YourlsService service) {
        yourlsPermissions.setupPermissions domain, service
    }

    /**
     * Returns the <i>Yourls</i> configuration file, for
     * example {@code "user/config.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "yourls_configuration_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #yourlsDir(Domain, YourlsService)
     * @see #getYourlsProperties()
     */
    File yourlsConfigFile(Domain domain, YourlsService service) {
        profileFileProperty "yourls_configuration_file", yourlsDir(domain, service), yourlsProperties
    }

    /**
     * Returns the <i>Yourls</i> configuration file, for
     * example {@code "user/config-sample.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "yourls_configuration_sample_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #yourlsDir(Domain, YourlsService)
     * @see #getYourlsProperties()
     */
    File yourlsConfigSampleFile(Domain domain, YourlsService service) {
        profileFileProperty "yourls_configuration_sample_file", yourlsDir(domain, service), yourlsProperties
    }

    /**
     * Returns the <i>Yourls</i> packages, for
     * example {@code "php5, php5-gd, php5-mysql, mysql-client, mysql-server"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_packages"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    List getYourlsPackages() {
        profileListProperty "yourls_packages", yourlsProperties
    }

    /**
     * Returns the cookie key, for example {@code ""}, empty. If empty,
     * the key will be randomly generated.
     *
     * <ul>
     * <li>profile property {@code "yourls_cookie_key"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsCookieKey() {
        String value = profileStringProperty "yourls_cookie_key", yourlsProperties
        if (value == null || value.empty) {
            return RandomStringUtils.random(41)
        } else {
            return value
        }
    }

    /**
     * Returns default <i>Yourls</i> alias, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_alias"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultAlias() {
        profileProperty "yourls_default_alias", yourlsProperties
    }

    /**
     * Returns default <i>Yourls</i> prefix, for
     * example {@code "yourls_1"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_prefix"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultPrefix() {
        profileProperty "yourls_default_prefix", yourlsProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_override_mode"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    OverrideMode getYourlsDefaultOverrideMode() {
        OverrideMode.valueOf profileProperty("yourls_default_override_mode", yourlsProperties)
    }

    /**
     * Returns the default PHP logging level, for example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_debug_php_level"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    int getYourlsDefaultDebugPhpLevel() {
        profileNumberProperty "yourls_default_debug_php_level", yourlsProperties
    }

    /**
     * Returns the default <i>Yourls</i> logging level, for
     * example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_debug_yourls_level"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    int getYourlsDefaultDebugYourlsLevel() {
        profileNumberProperty "yourls_default_debug_yourls_level", yourlsProperties
    }

    /**
     * Returns the default database host, for
     * example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_database_host"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultDatabaseHost() {
        profileProperty "yourls_default_database_host", yourlsProperties
    }

    /**
     * Returns the default database host port, for
     * example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_database_port"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    int getYourlsDefaultDatabasePort() {
        profileNumberProperty "yourls_default_database_port", yourlsProperties
    }

    /**
     * Returns the default database table prefix, for
     * example {@code "yourls_"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_database_table_prefix"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultDatabaseTablePrefix() {
        profileProperty "yourls_default_database_table_prefix", yourlsProperties
    }

    /**
     * Returns the default database schema, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_database_driver"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultDatabaseDriver() {
        profileProperty "yourls_default_database_driver", yourlsProperties
    }

    /**
     * Returns the default site access mode, for
     * example {@code "open"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_site_access"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Access getYourlsDefaultSiteAccess() {
        Access.valueOf profileProperty("yourls_default_site_access", yourlsProperties)
    }

    /**
     * Returns the default stats access mode, for
     * example {@code "open"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_stats_access"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Access getYourlsDefaultStatsAccess() {
        Access.valueOf profileProperty("yourls_default_stats_access", yourlsProperties)
    }

    /**
     * Returns the default API access mode, for
     * example {@code "open"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_api_access"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Access getYourlsDefaultApiAccess() {
        Access.valueOf profileProperty("yourls_default_api_access", yourlsProperties)
    }

    /**
     * Returns the default timezone GMT offset, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_gmt_offset"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Integer getYourlsDefaultGmtOffset() {
        String value = profileProperty "yourls_default_gmt_offset", yourlsProperties
        if (!value.empty) {
            return Integer.valueOf(value)
        } else {
            return null
        }
    }

    /**
     * Returns the default URL convert mode, for
     * example {@code "base36"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_url_convert_mode"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Convert getYourlsDefaultUrlConvertMode() {
        Convert.valueOf profileProperty("yourls_default_url_convert_mode", yourlsProperties)
    }

    /**
     * Returns default unique URLs enabled, for
     * example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_unique_urls"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    Boolean getYourlsDefaultUniqueUrls() {
        profileBooleanProperty "yourls_default_unique_urls", yourlsProperties
    }

    /**
     * Returns default reserved keywords, for
     * example {@code "porn, faggot, sex, nigger, fuck, cunt, dick"}.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_reserved_keywords"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    List getYourlsDefaultReservedKeywords() {
        profileListProperty "yourls_default_reserved_keywords", yourlsProperties
    }

    /**
     * Returns default reserved keywords, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "yourls_default_language"}</li>
     * </ul>
     *
     * @see #getYourlsProperties()
     */
    String getYourlsDefaultLanguage() {
        profileProperty "yourls_default_language", yourlsProperties
    }

    /**
     * Returns the <i>Yourls</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link YourlsService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see YourlsService#getPrefix()
     */
    File yourlsDir(Domain domain, YourlsService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link YourlsService}.
     *
     * @return the location.
     */
    String serviceLocation(YourlsService service) {
        String location = service.alias == null ? "" : service.alias
        if (!location.startsWith("/")) {
            location = "/$location"
        }
        return location
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
     *            the <i>Yourls</i> {@link YourlsService} service.
     *
     * @see #serviceDir(Domain, Domain, YourlsService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, YourlsService service) {
        def serviceDir = serviceDir domain, refDomain, service
        service.alias.empty ? "/" : serviceDir
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
     *            the <i>Yourls</i> {@link YourlsService} service.
     *
     * @see #yourlsDir(Domain, YourlsService)
     */
    String serviceDir(Domain domain, Domain refDomain, YourlsService service) {
        refDomain == null ? yourlsDir(domain, service).absolutePath :
                yourlsDir(refDomain, service).absolutePath
    }

    /**
     * Returns the default <i>Yourls</i> properties.
     *
     * @return the <i>Yourls</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getYourlsProperties()

    /**
     * Returns the <i>Yourls</i> service name.
     */
    String getServiceName() {
        "yourls"
    }

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
        yourlsConfigFile.setScript this
        yourlsPermissions.setScript this
    }

    /**
     * Returns the parent script.
     */
    LinuxScript getScript() {
        script
    }

    /**
     * Delegates missing properties to {@link LinuxScript}.
     */
    def propertyMissing(String name) {
        script.getProperty name
    }

    /**
     * Delegates missing methods to {@link LinuxScript}.
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
