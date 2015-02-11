/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

/**
 * <i>Piwik 2.x</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Piwik_2_Config {

    private LinuxScript script

    @Inject
    private Piwik_2_ConfigLogger logg

    @Inject
    ConfigParser configParser

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain} domain.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupDefaults(Domain domain, PiwikService service) {
        setupDefaultAlias service
        setupDefaultPrefix service
        setupDefaultOverrideMode service
        setupDefaultDebug service, domain
    }

    /**
     * Setups the default debug.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link PiwikService}.
     */
    void setupDefaultDebug(PiwikService service, Domain domain) {
        if (!service.debugLogging("level") || !service.debugLogging("level")["piwik"]) {
            service.debug "piwik", level: piwikDefaultDebugLevel
        }
        if (!service.debugLogging("file") || !service.debugLogging("file")["piwik"]) {
            service.debug "piwik", file: piwikDefaultDebugFile
        }
        if (!service.debugLogging("writer") || !service.debugLogging("writer")["piwik"]) {
            service.debug "piwik", writer: piwikDefaultDebugWriter
        }
        logg.setupDefaultDebug this
    }

    /**
     * Sets default alias.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupDefaultAlias(PiwikService service) {
        if (service.alias == null) {
            service.alias = ""
        }
    }

    /**
     * Sets default prefix.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupDefaultPrefix(PiwikService service) {
        if (service.prefix == null) {
            service.prefix = piwikDefaultPrefix
        }
    }

    /**
     * Sets default override mode.
     *
     * @param service
     *            the {@link PiwikService} service.
     */
    void setupDefaultOverrideMode(PiwikService service) {
        if (service.overrideMode == null) {
            service.override mode: piwikDefaultOverrideMode
        }
    }

    /**
     * Deploys the <i>Piwik</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link PiwikService}.
     *
     * @see #piwikConfigFile(Domain, PiwikService)
     */
    void deployConfig(Domain domain, PiwikService service) {
        configParser.deployConfig domain, service
    }

    /**
     * Returns the default logging level, for example {@code "1"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_debug_level"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    int getPiwikDefaultDebugLevel() {
        profileNumberProperty "piwik_default_debug_level", piwikProperties
    }

    /**
     * Returns the default debug file, for
     * example {@code "tmp/logs/piwik.log"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_debug_file"}</li>
     * </ul>
     */
    String getPiwikDefaultDebugFile() {
        profileProperty "piwik_default_debug_file", piwikProperties
    }

    /**
     * Returns the default logging writer, for
     * example {@code "screen, database, file"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_debug_writer"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikDefaultDebugWriter() {
        profileProperty "piwik_default_debug_writer", piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> installation directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return the installation {@link File} directory.
     *
     * @see #domainDir(Domain)
     * @see PiwikService#getPrefix()
     */
    File piwikDir(Domain domain, PiwikService service) {
        new File(domainDir(domain), service.prefix)
    }

    /**
     * Returns the <i>Piwik</i> configuration file inside the domain, for
     * example {@code "config/config.ini.php"}. If the path is not absolute, the
     * path is assumed under the service installation directory.
     *
     * <ul>
     * <li>profile property {@code "piwik_configuration_file"}</li>
     * </ul>
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return the configuration {@link File} file.
     *
     * @see #piwikDir(Domain, PiwikService)
     * @see #getPiwikProperties()
     */
    File piwikConfigFile(Domain domain, PiwikService service) {
        profileFileProperty "piwik_configuration_file", piwikDir(domain, service), piwikProperties
    }

    /**
     * Returns the <i>Piwik</i> packages, for
     * example {@code "php5, php5-gd, php5-mysql, mysql-client, mysql-server"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_packages"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    List getPiwikPackages() {
        profileListProperty "piwik_packages", piwikProperties
    }

    /**
     * Returns default <i>Piwik</i> prefix, for
     * example {@code "piwik_2"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_prefix"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikDefaultPrefix() {
        profileProperty "piwik_default_prefix", piwikProperties
    }

    /**
     * Returns the default override mode, for
     * example {@code "update"}.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_override_mode"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    OverrideMode getPiwikDefaultOverrideMode() {
        def mode = profileProperty "piwik_default_override_mode", piwikProperties
        OverrideMode.valueOf mode
    }

    /**
     * Returns the service location.
     *
     * @param service
     *            the {@link PiwikService}.
     *
     * @return the location.
     */
    String serviceLocation(PiwikService service) {
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
     *            the <i>Piwik</i> {@link PiwikService} service.
     *
     * @see #serviceDir(Domain, Domain, PiwikService)
     */
    String serviceAliasDir(Domain domain, Domain refDomain, PiwikService service) {
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
     *            the <i>Piwik</i> {@link PiwikService} service.
     *
     * @see #piwikDir(Domain, PiwikService)
     */
    String serviceDir(Domain domain, Domain refDomain, PiwikService service) {
        refDomain == null ? piwikDir(domain, service).absolutePath :
                piwikDir(refDomain, service).absolutePath
    }

    /**
     * Returns the default <i>Piwik</i> properties.
     *
     * @return the <i>Piwik</i> {@link ContextProperties} properties.
     */
    abstract ContextProperties getPiwikProperties()

    /**
     * Returns the <i>Piwik</i> service name.
     */
    String getServiceName() {
        "piwik"
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
