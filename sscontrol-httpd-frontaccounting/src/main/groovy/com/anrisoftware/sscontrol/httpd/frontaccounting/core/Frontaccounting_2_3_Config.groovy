/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.core

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService

/**
 * <i>FrontAccounting 2.3</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Frontaccounting_2_3_Config {

    private LinuxScript script

    @Inject
    private Frontaccounting_2_3_ConfigLogger log

    @Inject
    private Frontaccounting_2_3_ConfigFile frontaccountingConfigFile

    @Inject
    private Frontaccounting_2_3_Permissions frontaccountingPermissions

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     */
    void setupDefaults(Domain domain, FrontaccountingService service) {
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
     *            the {@link FrontaccountingService} service.
     */
    void setupDefaultService(Domain domain, FrontaccountingService service) {
        if (service.alias == null) {
            service.alias = frontaccountingDefaultAlias
        }
        if (service.prefix == null) {
            service.prefix = frontaccountingDefaultPrefix
        }
        if (service.overrideMode == null) {
            service.override mode: frontaccountingDefaultOverrideMode
        }
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     */
    void setupDefaultDebug(Domain domain, FrontaccountingService service) {
        if (service.debugLogging("level") == null || service.debugLogging("level")["php"] == null) {
            service.debug "php", level: frontaccountingDefaultDebugPhpLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["sql"] == null) {
            service.debug "sql", level: frontaccountingDefaultDebugSqlLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["go"] == null) {
            service.debug "go", level: frontaccountingDefaultDebugGoLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["pdf"] == null) {
            service.debug "pdf", level: frontaccountingDefaultDebugPdfLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["sqltrail"] == null) {
            service.debug "sqltrail", level: frontaccountingDefaultDebugSqlTrailLevel
        }
        if (service.debugLogging("level") == null || service.debugLogging("level")["select"] == null) {
            service.debug "select", level: frontaccountingDefaultDebugSelectLevel
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
     *            the {@link FrontaccountingService} service.
     */
    void setupDefaultDatabase(Domain domain, FrontaccountingService service) {
        def db = service.database.database
        if (service.database.host == null) {
            service.database db, host: frontaccountingDefaultDatabaseHost
        }
        if (service.database.port == null) {
            service.database db, port: frontaccountingDefaultDatabasePort
        }
        if (service.database.driver == null && !frontaccountingDefaultDatabaseDriver.empty) {
            service.database db, driver: frontaccountingDefaultDatabaseDriver
        }
        log.setupDefaultDatabase this, domain, service
    }

    /**
     * Deploys the <i>FrontAccounting</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link FrontaccountingService}.
     *
     * @see #yourlsConfigFile(Domain, FrontaccountingService)
     */
    void deployConfig(Domain domain, FrontaccountingService service) {
        frontaccountingConfigFile.deployConfig domain, service
    }

    /**
     * Sets the owner and permissions of the <i>FrontAccounting</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     */
    void setupPermissions(Domain domain, FrontaccountingService service) {
        frontaccountingPermissions.setupPermissions domain, service
    }

    /**
     * Returns the <i>FrontAccounting</i> configuration file, for
     * example {@code "config.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_configuration_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #frontaccountingDir(Domain, FrontaccountingService)
     * @see #getFrontaccountingProperties()
     */
    File frontaccountingConfigFile(Domain domain, FrontaccountingService service) {
        profileFileProperty "frontaccounting_configuration_file", frontaccountingDir(domain, service), frontaccountingProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> configuration file, for
     * example {@code "config.default.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_configuration_default_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #frontaccountingDir(Domain, FrontaccountingService)
     * @see #getFrontaccountingProperties()
     */
    File frontaccountingConfigDefaultFile(Domain domain, FrontaccountingService service) {
        profileFileProperty "frontaccounting_configuration_default_file", frontaccountingDir(domain, service), frontaccountingProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> database configuration file, for
     * example {@code "config_db.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_database_configuration_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #frontaccountingDir(Domain, FrontaccountingService)
     * @see #getFrontaccountingProperties()
     */
    File frontaccountingDatabaseConfigFile(Domain domain, FrontaccountingService service) {
        profileFileProperty "frontaccounting_database_configuration_file", frontaccountingDir(domain, service), frontaccountingProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> packages, for
     * example {@code "php5, php5-gd, php5-mysql, mysql-client, mysql-server"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_packages"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    List getFrontaccountingPackages() {
        profileListProperty "frontaccounting_packages", frontaccountingProperties
    }

    /**
     * Returns default <i>FrontAccounting</i> alias, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_alias"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingDefaultAlias() {
        profileProperty "frontaccounting_default_alias", frontaccountingProperties
    }

    /**
     * Returns default <i>FrontAccounting</i> prefix, for
     * example {@code "frontaccounting_1"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_prefix"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingDefaultPrefix() {
        profileProperty "frontaccounting_default_prefix", frontaccountingProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "upgrade"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_override_mode"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    OverrideMode getFrontaccountingDefaultOverrideMode() {
        OverrideMode.valueOf profileProperty("frontaccounting_default_override_mode", frontaccountingProperties)
    }

    /**
     * Returns the default PHP logging level, for example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_php_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugPhpLevel() {
        profileNumberProperty "frontaccounting_default_debug_php_level", frontaccountingProperties
    }

    /**
     * Returns the default SQL queries logging level, for
     * example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_sql_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugSqlLevel() {
        profileNumberProperty "frontaccounting_default_debug_sql_level", frontaccountingProperties
    }

    /**
     * Returns the default warnings, errors and notices logging level, for
     * example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_go_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugGoLevel() {
        profileNumberProperty "frontaccounting_default_debug_go_level", frontaccountingProperties
    }

    /**
     * Returns the default PDF debug logging level, for
     * example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_pdf_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugPdfLevel() {
        profileNumberProperty "frontaccounting_default_debug_pdf_level", frontaccountingProperties
    }

    /**
     * Returns the default SQL trail debug logging level, for
     * example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_sqltrail_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugSqlTrailLevel() {
        profileNumberProperty "frontaccounting_default_debug_sqltrail_level", frontaccountingProperties
    }

    /**
     * Returns the default SQL select debug logging level, for
     * example {@code "0"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_debug_select_level"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDebugSelectLevel() {
        profileNumberProperty "frontaccounting_default_debug_select_level", frontaccountingProperties
    }

    /**
     * Returns the default database host, for
     * example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_database_host"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingDefaultDatabaseHost() {
        profileProperty "frontaccounting_default_database_host", frontaccountingProperties
    }

    /**
     * Returns the default database host port, for
     * example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_database_port"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    int getFrontaccountingDefaultDatabasePort() {
        profileNumberProperty "frontaccounting_default_database_port", frontaccountingProperties
    }

    /**
     * Returns the default database table prefix, for
     * example {@code "frontaccounting_"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_database_table_prefix"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingDefaultDatabaseTablePrefix() {
        profileProperty "frontaccounting_default_database_table_prefix", frontaccountingProperties
    }

    /**
     * Returns the default database schema, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_default_database_driver"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingDefaultDatabaseDriver() {
        profileProperty "frontaccounting_default_database_driver", frontaccountingProperties
    }

    /**
     * Returns the <i>FrontAccounting</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FrontaccountingService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see FrontaccountingService#getPrefix()
     */
    File frontaccountingDir(Domain domain, FrontaccountingService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the temporary directory path, for
     * example {@code "tmp"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_temp_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingTempDirectory() {
        profileProperty "frontaccounting_temp_directory", frontaccountingProperties
    }

    /**
     * Returns the language directory path, for
     * example {@code "lang"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_lang_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingLangDirectory() {
        profileProperty "frontaccounting_lang_directory", frontaccountingProperties
    }

    /**
     * Returns the modules directory path, for
     * example {@code "modules"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_modules_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingModulesDirectory() {
        profileProperty "frontaccounting_modules_directory", frontaccountingProperties
    }

    /**
     * Returns the company directory path, for
     * example {@code "company"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_company_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingCompanyDirectory() {
        profileProperty "frontaccounting_company_directory", frontaccountingProperties
    }

    /**
     * Returns the themes directory path, for
     * example {@code "themes"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_themes_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingThemesDirectory() {
        profileProperty "frontaccounting_themes_directory", frontaccountingProperties
    }

    /**
     * Returns the SQL directory path, for
     * example {@code "sql"}.
     *
     * <ul>
     * <li>profile property {@code "frontaccounting_sql_directory"}</li>
     * </ul>
     *
     * @see #getFrontaccountingProperties()
     */
    String getFrontaccountingSqlDirectory() {
        profileProperty "frontaccounting_sql_directory", frontaccountingProperties
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link FrontaccountingService}.
     *
     * @return the location.
     */
    String serviceLocation(FrontaccountingService service) {
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
     *            the <i>FrontAccounting</i> {@link FrontaccountingService} service.
     *
     * @see #serviceDir(Domain, Domain, FrontaccountingService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, FrontaccountingService service) {
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
     *            the <i>FrontAccounting</i> {@link FrontaccountingService} service.
     *
     * @see #frontaccountingDir(Domain, FrontaccountingService)
     */
    String serviceDir(Domain domain, Domain refDomain, FrontaccountingService service) {
        refDomain == null ? frontaccountingDir(domain, service).absolutePath :
                frontaccountingDir(refDomain, service).absolutePath
    }

    /**
     * Returns the default <i>FrontAccounting</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFrontaccountingProperties()

    /**
     * Returns the <i>FrontAccounting</i> service name.
     */
    abstract String getServiceName()

    /**
     * Returns the profile name.
     */
    abstract String getProfile()

    /**
     * Sets the parent script.
     */
    void setScript(LinuxScript script) {
        this.script = script
        frontaccountingConfigFile.setScript this
        frontaccountingPermissions.setScript this
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
