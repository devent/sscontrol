/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.core

import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.stringtemplate.v4.ST

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Configures <i>Piwik 2.3</i>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Piwik_2_3_Config {

    @Inject
    private Piwik_2_3_ConfigLogger logg

    @Inject
    DebugLoggingFactory debugLoggingFactory

    @Inject
    DebugLevelRenderer debugLevelRenderer

    /**
     * @see ServiceConfig#getScript()
     */
    LinuxScript script

    /**
     * @see ServiceConfig#deployDomain(Domain, Domain, WebService, List)
     */
    abstract void deployDomain(Domain domain, Domain refDomain, WebService service, List config)

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    abstract void deployService(Domain domain, WebService service, List config)

    /**
     * Setups default options.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link PiwikService}.
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
        def file = piwikDefaultDebugFile domain, service
        service.debug = service.debug == null ? debugLoggingFactory.create(piwikDefaultDebugLevel) : service.debug
        service.debug.level = service.debug.level == -1 ? piwikDefaultDebugLevel : service.debug.level
        service.debug.args.file = service.debug.args.file == null ? file : service.debug.args.file
        service.debug.args.writer = service.debug.args.writer == null ? piwikDefaultDebugWriter : service.debug.args.writer
        logg.setupDefaultDebug this, service.debug
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
            service.overrideMode = piwikDefaultOverrideMode
        }
    }

    /**
     * Returns the default logging level.
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
     * example {@code "<piwikDir>/tmp/logs/piwik.log".} The placeholder
     * {@code "piwikDir"} is replaced by the specified <i>Piwik</i> directory.
     *
     * @param domain
     *            the {@link Domain} domain of the service.
     *
     * @param service
     *            the {@link PiwikService} service.
     *
     * @return the default debug {@link File} file.
     *
     * <ul>
     * <li>profile property {@code "piwik_default_debug_file"}</li>
     * </ul>
     */
    File piwikDefaultDebugFile(Domain domain, PiwikService service) {
        def str = profileProperty "piwik_default_debug_file", piwikProperties
        def dir = piwikDir domain, service
        new File(new ST(str).add("piwikDir", dir).render())
    }

    /**
     * Returns the default logging writer, for example screen, database, file.
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
     * Deploys the <i>Piwik</i> configuration.
     *
     * @param domain
     *            the service {@link Domain}.
     *
     * @param service
     *            the {@link PiwikService}.
     *
     * @see #gititConfigFile(Domain, PiwikService)
     */
    void deployConfig(Domain domain, PiwikService service) {
        def configs = [
            configToken("debugLogWriters", "debug", service.debug),
            configToken("debugLogLevel", "debug", service.debug),
            configToken("debugLogFile", "debug", service.debug),
        ]
        def file = piwikGlobalConfigFile domain, service
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(';'), conf, configs, file
    }

    TokenTemplate configToken(Object[] args) {
        def search = piwikConfigTemplate.getText(true, "${args[0]}Search")
        def replace = piwikConfigTemplate.getText(true, args)
        new TokenTemplate(search, replace)
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
     * Returns the <i>Piwik</i> global configuration file inside the domain, for
     * example {@code "/var/www/domain.com/piwik/config/global.ini.php".}
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
     */
    File piwikGlobalConfigFile(Domain domain, PiwikService service) {
        def dir = piwikDir domain, service
        new File(dir, piwikGlobalConfigFile)
    }

    /**
     * Returns the <i>Piwik</i> global configuration file property, for
     * example {@code "config/global.ini.php".}
     *
     * <ul>
     * <li>profile property {@code "piwik_global_configuration_file"}</li>
     * </ul>
     *
     * @see #getPiwikProperties()
     */
    String getPiwikGlobalConfigFile() {
        profileProperty "piwik_global_configuration_file", piwikProperties
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
     * Returns the <i>Piwik</i> service configuration template.
     *
     * @return the {@link TemplateResource}.
     */
    abstract TemplateResource getPiwikConfigTemplate()

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
     * @see ServiceConfig#setScript(LinuxScript)
     */
    void setScript(LinuxScript script) {
        this.script = script
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
