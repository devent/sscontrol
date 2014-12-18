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
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2

import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.SslDomainImpl
import com.anrisoftware.sscontrol.httpd.domain.linux.DomainConfig
import com.anrisoftware.sscontrol.httpd.domain.linux.SslDomainConfig
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.RestartServicesFactory
import com.anrisoftware.sscontrol.scripts.unix.StopServicesFactory

/**
 * <i>Apache 2.2</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
abstract class Apache_2_2_Script extends ApacheScript {

    @Inject
    Apache_2_2_ScriptLogger logg

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    @Inject
    RedirectConfig redirectConfig

    @Inject
    RestartServicesFactory restartServicesFactory

    @Inject
    StopServicesFactory stopServicesFactory

    @Inject
    ResourceURIAttributeRenderer resourceURIRenderer

    @Inject
    LogLevelAttributeRenderer logLevelRenderer

    /**
     * Resource containing the Apache ports configuration templates.
     */
    TemplateResource portsConfigTemplate

    /**
     * Resource containing the Apache defaults configuration templates.
     */
    TemplateResource defaultsConfigTemplate

    /**
     * Resource containing the Apache domains configuration templates.
     */
    TemplateResource domainsConfiguration

    /**
     * Resource containing the Apache configuration templates.
     */
    TemplateResource configTemplate

    /**
     * Resource containing the Apache commands templates.
     */
    TemplateResource apacheCommandsTemplate

    @Override
    def run() {
        domainConfig.script = this
        sslDomainConfig.script = this
        redirectConfig.script = this
        stopServices()
        super.run()
        deployPortsConfig service
        deployDefaultConfig service
        deployDomainsConfig service
        enableDefaultMods()
        deployConfig()
        restartServices()
    }

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "Apache_2_2", [renderers: [
                logLevelRenderer,
                resourceURIRenderer
            ]]
        portsConfigTemplate = templates.getResource "portsconf"
        defaultsConfigTemplate = templates.getResource "defaultsconf"
        domainsConfiguration = templates.getResource "domainsconf"
        configTemplate = templates.getResource "config"
        apacheCommandsTemplate = templates.getResource "commands"
    }

    /**
     * Deploys the ports {@code Listen} configuration to
     * the {@code "/etc/apache2/ports.conf"} file.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     *
     * @see #getPortsConfigFile()
     */
    void deployPortsConfig(HttpdService service) {
        def bindingAddresses = service.bindingAddresses
        def addresses = bindingAddresses.inject([]) { list, address, ports ->
            ports.each { port -> list << "$address:$port" }
            list
        }
        def string = portsConfigTemplate.getText true, "portsConfiguration", "addresses", addresses
        FileUtils.write portsConfigFile, string
    }

    /**
     * Deploys the default settings configuration to
     * the {@code "/etc/apache2/conf.d/000-robobee-default.conf"} file.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     *
     * @see #getDefaultConfigFile()
     */
    void deployDefaultConfig(HttpdService service) {
        def configs = []
        configs << defaultsConfigTemplate.getText(true, "defaultSettingsHeader")
        configs << defaultsConfigTemplate.getText(true, "logLevelConfig", "level", service.debugLogging("level")["error"])
        configs << defaultsConfigTemplate.getText(true, "defaultDirectories")
        FileUtils.writeLines defaultConfigFile, charset.name(), configs
    }

    /**
     * Deploys the domains configuration to
     * the {@code "/etc/apache2/conf.d/000-robobee-domains.conf"} file.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     *
     * @see #getDefaultConfigFile()
     */
    void deployDomainsConfig(HttpdService service) {
        def string = domainsConfiguration.getText true, "domainsConfiguration", "service", service
        FileUtils.write domainsConfigFile, string
    }

    /**
     * Enables default <i>Apache</i> mods.
     */
    void enableDefaultMods() {
        enableMod "suexec"
        enableMods additionalMods
    }

    /**
     * Stops the <i>Apache</i> services.
     */
    void stopServices() {
        stopServicesFactory.create(
                log: log,
                command: stopCommand,
                services: stopServices,
                exitCode: null,
                this, threads)()
    }

    /**
     * Restarts the <i>Apache</i> services.
     */
    void restartServices() {
        restartServicesFactory.create(
                log: log,
                command: restartCommand,
                services: restartServices,
                this, threads)()
    }

    void deployConfig() {
        uniqueDomains.each { deployDomain it }
        service.domains.each { Domain domain ->
            List serviceConfig = []
            deployRedirect domain, serviceConfig
            deployDomainService domain, serviceConfig
            deployDomainConfig domain, serviceConfig
            deploySslDomain domain
            enableSites domain.fileName
        }
    }

    void deployDomainService(Domain domain, List serviceConfig) {
        domain.services.findAll { WebService service ->
            service.domain == domain
        }.each { WebService service ->
            deployWebService domain, service, serviceConfig
        }
    }

    void deployWebService(Domain domain, WebService service, List serviceConfig) {
        def reftarget = findReferencedService service
        def profile = profileName
        def config = findServiceConfig profile, service
        if (reftarget == null) {
            config.deployService domain, service, serviceConfig
        } else {
            def refdomain = findReferencedDomain service
            config.deployDomain domain, refdomain, reftarget, serviceConfig
        }
    }

    WebService findReferencedService(WebService service) {
        WebService refservice = null
        for (Domain domain : this.service.domains) {
            refservice = domain.services.find { WebService s ->
                s.id != null && s.id == service.ref
            }
            if (refservice != null) {
                break
            }
        }
        return refservice
    }

    Domain findReferencedDomain(WebService service) {
        this.service.domains.find { Domain domain ->
            domain.id != null && domain.id == service.refDomain
        }
    }

    def deployRedirect(Domain domain, List serviceConfig) {
        domain.redirects.each {
            redirectConfig.deployRedirect(domain, it, serviceConfig)
        }
    }

    def deploySslDomain(Domain domain) {
        if (domain.class == SslDomainImpl) {
            enableMod "ssl"
            sslDomainConfig.deployCertificates(domain)
        }
    }

    def deployDomain(Domain domain) {
        domainConfig.deployDomain domain
    }

    def deployDomainConfig(Domain domain, List servicesConfig) {
        def string = configTemplate.getText(
                true, domain.class.simpleName,
                "properties", this,
                "domain", domain,
                "servicesConfig", servicesConfig)
        FileUtils.write new File(sitesAvailableDir, domain.fileName), string
    }
}
