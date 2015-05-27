/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.DatabaseType
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService

/**
 * <i>FUDForum 3</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Fudforum_3_Config {

    private LinuxScript script

    @Inject
    private Fudforum_3_ConfigLogger log

    @Inject
    private Fudforum_3_InstallFile fudforumInstallFile

    @Inject
    private Fudforum_3_Permissions fudforumPermissions

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void setupDefaults(Domain domain, FudforumService service) {
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
     *            the {@link FudforumService} service.
     */
    void setupDefaultService(Domain domain, FudforumService service) {
        if (service.alias == null) {
            service.alias = fudforumDefaultAlias
        }
        if (service.prefix == null) {
            service.prefix = fudforumDefaultPrefix
        }
        if (service.site == null) {
            def site = "${domain.proto}${domain.name}"
            if (service.alias != null && !service.alias.empty) {
                site += "/${service.alias}"
            }
            service.site site
        }
        if (service.overrideMode == null) {
            service.override mode: fudforumDefaultOverrideMode
        }
        if (service.template == null) {
            service.template fudforumDefaultTemplate
        }
        if (service.language == null) {
            service.language fudforumDefaultLanguage
        }
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void setupDefaultDebug(Domain domain, FudforumService service) {
        if (service.debugLogging("level") == null || service.debugLogging("level")["php"] == null) {
            service.debug "php", level: fudforumDefaultDebugPhpLevel
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
     *            the {@link FudforumService} service.
     */
    void setupDefaultDatabase(Domain domain, FudforumService service) {
        def db = service.database.database
        if (service.database.host == null) {
            service.database db, host: fudforumDefaultDatabaseHost
        }
        if (service.database.port == null) {
            service.database db, port: fudforumDefaultDatabasePort
        }
        if (service.database.prefix == null) {
            service.database db, prefix: fudforumDefaultDatabaseTablePrefix
        }
        log.setupDefaultDatabase this, domain, service
    }

    /**
     * Deploys the <i>FUDForum</i> install file.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link FudforumService}.
     *
     * @see #fudforumConfigFile(Domain, FudforumService)
     */
    void deployInstall(Domain domain, FudforumService service) {
        fudforumInstallFile.deployConfig domain, service
    }

    /**
     * Sets the owner and permissions of the <i>FUDForum</i> service.
     *
     * @param domain
     *            the {@link Domain} domain.
     *
     * @param service
     *            the {@link FudforumService} service.
     */
    void setupPermissions(Domain domain, FudforumService service) {
        fudforumPermissions.setupPermissions domain, service
    }

    /**
     * Returns the <i>FUDForum</i> install file, for
     * example {@code "install.ini"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "fudforum_install_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #fudforumDir(Domain, FudforumService)
     * @see #getFudforumProperties()
     */
    File fudforumInstallFile(Domain domain, FudforumService service) {
        profileFileProperty "fudforum_install_file", fudforumDir(domain, service), fudforumProperties
    }

    /**
     * Returns the <i>FUDForum</i> packages, for
     * example {@code "php5"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_packages"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    List getFudforumPackages() {
        profileListProperty "fudforum_packages", fudforumProperties
    }

    /**
     * Returns the database packages for the specified database type, for
     * example {@code "php5-mysql"}.
     *
     * @param type
     *            the {@link DatabaseType} type.
     *
     * <ul>
     * <li>profile property {@code "fudforum_mysql_packages"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    List fudforumDatabasePackages(DatabaseType type) {
        switch (type) {
            case DatabaseType.mysql:
                return profileListProperty("fudforum_mysql_packages", fudforumProperties)
        }
    }

    /**
     * Returns default <i>FUDForum</i> alias, for
     * example {@code ""}, empty.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_alias"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultAlias() {
        profileProperty "fudforum_default_alias", fudforumProperties
    }

    /**
     * Returns default <i>FUDForum</i> prefix, for
     * example {@code "fudforum_3"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_prefix"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultPrefix() {
        profileProperty "fudforum_default_prefix", fudforumProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_override_mode"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    OverrideMode getFudforumDefaultOverrideMode() {
        OverrideMode.valueOf profileProperty("fudforum_default_override_mode", fudforumProperties)
    }

    /**
     * Returns the default PHP logging level, for example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_debug_php_level"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    int getFudforumDefaultDebugPhpLevel() {
        profileNumberProperty "fudforum_default_debug_php_level", fudforumProperties
    }

    /**
     * Returns the default database host, for
     * example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_database_host"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultDatabaseHost() {
        profileProperty "fudforum_default_database_host", fudforumProperties
    }

    /**
     * Returns the default database host port, for
     * example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_database_port"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    int getFudforumDefaultDatabasePort() {
        profileNumberProperty "fudforum_default_database_port", fudforumProperties
    }

    /**
     * Returns the default database table prefix, for
     * example {@code "fudforum_"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_database_table_prefix"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultDatabaseTablePrefix() {
        profileProperty "fudforum_default_database_table_prefix", fudforumProperties
    }

    /**
     * Returns default template name, for
     * example {@code "default"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_template"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultTemplate() {
        profileProperty "fudforum_default_template", fudforumProperties
    }

    /**
     * Returns default language name, for
     * example {@code "en"}.
     *
     * <ul>
     * <li>profile property {@code "fudforum_default_language"}</li>
     * </ul>
     *
     * @see #getFudforumProperties()
     */
    String getFudforumDefaultLanguage() {
        profileProperty "fudforum_default_language", fudforumProperties
    }

    /**
     * Returns the <i>FUDForum</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link FudforumService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see FudforumService#getPrefix()
     */
    File fudforumDir(Domain domain, FudforumService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link FudforumService}.
     *
     * @return the location.
     */
    String serviceLocation(FudforumService service) {
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
     *            the <i>FUDForum</i> {@link FudforumService} service.
     *
     * @see #serviceDir(Domain, Domain, FudforumService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, FudforumService service) {
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
     *            the <i>FUDForum</i> {@link FudforumService} service.
     *
     * @see #fudforumDir(Domain, FudforumService)
     */
    String serviceDir(Domain domain, Domain refDomain, FudforumService service) {
        refDomain == null ? fudforumDir(domain, service).absolutePath :
                fudforumDir(refDomain, service).absolutePath
    }

    /**
     * Returns the default <i>FUDForum</i> properties.
     *
     * @return the {@link ContextProperties} properties.
     */
    abstract ContextProperties getFudforumProperties()

    /**
     * Returns the <i>FUDForum</i> service name.
     */
    String getServiceName() {
        "fudforum"
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
        fudforumInstallFile.setScript this
        fudforumPermissions.setScript this
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
