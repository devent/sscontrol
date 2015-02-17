/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.stringtemplate.v4.ST

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>ownCloud 7.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Owncloud_7_Config {

    private LinuxScript script

    @Inject
    private Owncloud_7_ConfigLogger logg

    @Inject
    ConfigParser configParser

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaults(Domain domain, OwncloudService service) {
        setupDefaultAlias service
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultDebug domain, service
        setupDefaultDatabase domain, service
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaultDebug(Domain domain, OwncloudService service) {
        if (!service.debugLogging("level") || !service.debugLogging("level")["owncloud"]) {
            service.debug "owncloud", level: owncloudDefaultDebugLevel
        }
        logg.setupDefaultDebug this, domain, service
    }

    /**
     * Setups the default database.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaultDatabase(Domain domain, OwncloudService service) {
        def db = service.database.database
        if (service.database.host == null) {
            service.database db, host: owncloudDefaultDatabaseHost
        }
        if (service.database.port == null) {
            service.database db, port: owncloudDefaultDatabasePort
        }
        if (service.database.prefix == null) {
            service.database db, prefix: owncloudDefaultDatabaseTablePrefix
        }
        if (service.database.adapter == null) {
            service.database db, adapter: owncloudDefaultDatabaseAdapter
        }
        logg.setupDefaultDatabase this, domain, service
    }

    /**
     * Sets default alias.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaultAlias(OwncloudService service) {
        if (service.alias == null) {
            service.alias = ""
        }
    }

    /**
     * Sets default prefix.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaultPrefix(OwncloudService service) {
        if (service.prefix == null) {
            service.prefix = owncloudDefaultPrefix
        }
    }

    /**
     * Sets default override mode.
     *
     * @param service
     *            the {@link OwncloudService} service.
     */
    void setupDefaultOverrideMode(OwncloudService service) {
        if (service.overrideMode == null) {
            service.override mode: owncloudDefaultOverrideMode
        }
    }

    /**
     * Deploys the <i>ownCloud</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link OwncloudService}.
     *
     * @see #piwikConfigFile(Domain, OwncloudService)
     */
    void deployConfig(Domain domain, OwncloudService service) {
        configParser.deployConfig domain, service
    }

    /**
     * Sets the owner of <i>ownCloud</i> directory.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupPermissions(Domain domain, OwncloudService service) {
        def user = domain.domainUser
        def dir = owncloudDir domain, service
        def appsDir = owncloudAppsDirectory domain, service
        def configDir = owncloudConfigDirectory domain, service
        def dataDir = owncloudDataDirectory domain, service
        configDir.mkdirs()
        dataDir.mkdirs()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: "root",
                ownerGroup: "root",
                recursive: true,
                files: dir,
                this, threads)()
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                owner: user.name,
                ownerGroup: user.group,
                recursive: true,
                files: [appsDir, configDir, dataDir],
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                mod: "o-rX",
                files: [dataDir],
                this, threads)()
    }

    /**
     * Returns the default logging level, for example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_debug_level"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    int getOwncloudDefaultDebugLevel() {
        profileNumberProperty "owncloud_default_debug_level", owncloudProperties
    }

    /**
     * Returns the default debug file, for
     * example {@code "tmp/logs/piwik.log"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_debug_file"}</li>
     * </ul>
     */
    String getPiwikDefaultDebugFile() {
        profileProperty "owncloud_default_debug_file", owncloudProperties
    }

    /**
     * Returns the default logging writer, for
     * example {@code "screen, database, file"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_debug_writer"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getPiwikDefaultDebugWriter() {
        profileProperty "owncloud_default_debug_writer", owncloudProperties
    }

    /**
     * Returns the default database host, for
     * example {@code "localhost"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_database_host"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getOwncloudDefaultDatabaseHost() {
        profileProperty "owncloud_default_database_host", owncloudProperties
    }

    /**
     * Returns the default database host port, for
     * example {@code "3306"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_database_port"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    int getOwncloudDefaultDatabasePort() {
        profileNumberProperty "owncloud_default_database_port", owncloudProperties
    }

    /**
     * Returns the default database table prefix, for
     * example {@code "owncloud_"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_database_table_prefix"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getOwncloudDefaultDatabaseTablePrefix() {
        profileProperty "owncloud_default_database_table_prefix", owncloudProperties
    }

    /**
     * Returns the default database adapter, for
     * example {@code "mysql"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_database_adapter"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getOwncloudDefaultDatabaseAdapter() {
        profileProperty "owncloud_default_database_adapter", owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see OwncloudService#getPrefix()
     */
    File owncloudDir(Domain domain, OwncloudService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>ownCloud</i> configuration file, for
     * example {@code "config/config.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_config_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #owncloudDir(Domain, OwncloudService)
     * @see #getOwncloudProperties()
     */
    File owncloudConfigFile(Domain domain, OwncloudService service) {
        profileFileProperty "owncloud_config_file", owncloudDir(domain, service), owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> apps directory, for
     * example {@code "apps"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_apps_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #owncloudDir(Domain, OwncloudService)
     * @see #getOwncloudProperties()
     */
    File owncloudAppsDirectory(Domain domain, OwncloudService service) {
        profileFileProperty "owncloud_apps_directory", owncloudDir(domain, service), owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> configuration directory, for
     * example {@code "config"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_config_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #owncloudDir(Domain, OwncloudService)
     * @see #getOwncloudProperties()
     */
    File owncloudConfigDirectory(Domain domain, OwncloudService service) {
        profileFileProperty "owncloud_config_directory", owncloudDir(domain, service), owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> data directory, for
     * example {@code "data"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "owncloud_data_directory"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #owncloudDir(Domain, OwncloudService)
     * @see #getOwncloudProperties()
     */
    File owncloudDataDirectory(Domain domain, OwncloudService service) {
        profileFileProperty "owncloud_data_directory", owncloudDir(domain, service), owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> base URL, for
     * example {@code "<proto><domain.name><if(service.alias)>/<service.alias><endif>"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_overwrite_cli"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link OwncloudService} service.
     *
     * @see #getOwncloudProperties()
     */
    String owncloudOverwriteCliUrl(Domain domain, OwncloudService service) {
        def value = profileProperty "owncloud_overwrite_cli", owncloudProperties
        def proto = owncloudForcesslEnabled ? "https://" : domain.proto
        new ST(value).add("proto", proto).add("domain", domain).add("service", service).render()
    }

    /**
     * Returns enable force SSL, for example {@code "true"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_force_ssl_enabled"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    boolean getOwncloudForcesslEnabled() {
        profileBooleanProperty "owncloud_force_ssl_enabled", owncloudProperties
    }

    /**
     * Returns the <i>ownCloud</i> packages, for
     * example {@code "php5, php5-gd, php5-json, php5-mysql, php5-curl, php5-intl, php5-mcrypt, php5-imagick"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_packages"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    List getOwncloudPackages() {
        profileListProperty "owncloud_packages", owncloudProperties
    }

    /**
     * Returns default <i>ownCloud</i> prefix, for
     * example {@code "owncloud_7"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_prefix"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    String getOwncloudDefaultPrefix() {
        profileProperty "owncloud_default_prefix", owncloudProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "owncloud_default_override_mode"}</li>
     * </ul>
     *
     * @see #getOwncloudProperties()
     */
    OverrideMode getOwncloudDefaultOverrideMode() {
        def mode = profileProperty "owncloud_default_override_mode", owncloudProperties
        OverrideMode.valueOf mode
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link OwncloudService}.
     *
     * @return the location.
     */
    String serviceLocation(OwncloudService service) {
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
     *            the <i>ownCloud</i> {@link OwncloudService} service.
     *
     * @see #serviceDir(Domain, Domain, OwncloudService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, OwncloudService service) {
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
     *            the <i>ownCloud</i> {@link OwncloudService} service.
     *
     * @see #owncloudDir(Domain, OwncloudService)
     */
    String serviceDir(Domain domain, Domain refDomain, OwncloudService service) {
        refDomain == null ? owncloudDir(domain, service).absolutePath :
                owncloudDir(refDomain, service).absolutePath
    }

    /**
     * Returns the default <i>ownCloud</i> properties.
     *
     * @return the <i>ownCloud</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getOwncloudProperties()

    /**
     * Returns the <i>ownCloud</i> service name.
     */
    String getServiceName() {
        "owncloud_7"
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
        configParser.setScript this
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
