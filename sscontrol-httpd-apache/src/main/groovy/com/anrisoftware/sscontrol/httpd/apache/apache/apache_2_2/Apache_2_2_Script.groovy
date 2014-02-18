/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.ApacheScript
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.DomainConfig
import com.anrisoftware.sscontrol.httpd.apache.apache.linux.SslDomainConfig
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Configures Apache 2.2 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Apache_2_2_Script extends ApacheScript {

    @Inject
    Apache_2_2_ScriptLogger log

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    @Inject
    RedirectConfig redirectConfig

    /**
     * The {@link Templates} for the script.
     */
    Templates apacheTemplates

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
        apacheTemplates = templatesFactory.create "Apache_2_2"
        portsConfigTemplate = apacheTemplates.getResource "portsconf"
        defaultsConfigTemplate = apacheTemplates.getResource "defaultsconf"
        domainsConfiguration = apacheTemplates.getResource "domainsconf"
        configTemplate = apacheTemplates.getResource "config"
        apacheCommandsTemplate = apacheTemplates.getResource "commands"
        domainConfig.script = this
        sslDomainConfig.script = this
        redirectConfig.script = this
        super.run()
        deployPortsConfig()
        deployDefaultConfig()
        deployDomainsConfig()
        enableDefaultMods()
        deployConfig()
        restartServices()
    }

    def deployPortsConfig() {
        def string = portsConfigTemplate.getText true, "portsConfiguration", "service", service
        FileUtils.write portsConfigFile, string
    }

    def deployDefaultConfig() {
        def string = defaultsConfigTemplate.getText true, "defaultsConfiguration"
        FileUtils.write defaultConfigFile, string
    }

    def deployDomainsConfig() {
        def string = domainsConfiguration.getText true, "domainsConfiguration", "service", service
        FileUtils.write domainsConfigFile, string
    }

    /**
     * Enables default Apache/mods.
     */
    void enableDefaultMods() {
        enableMod "suexec"
        enableMods additionalMods
    }

    def deployConfig() {
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

    def deployDomainService(Domain domain, List serviceConfig) {
        domain.services.findAll { WebService service ->
            service.domain == domain
        }.each { WebService service ->
            deployWebService domain, service, serviceConfig
        }
    }

    void deployWebService(Domain domain, WebService service, List serviceConfig) {
        def reftarget = findReferencedService service
        def profile = profileName
        def config = serviceConfigs["${profile}.${service.name}"]
        if (reftarget == null) {
            log.checkServiceConfig config, service, profile
            config.deployService domain, service, serviceConfig
        } else {
            log.checkServiceConfig config, service, profile
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
        if (domain.class == SslDomain) {
            sslDomainConfig.enableSsl()
            sslDomainConfig.deployCertificates(domain)
        }
    }

    def deployDomain(Domain domain) {
        domainConfig.deployDomain domain
    }

    def deployDomainConfig(Domain domain, List servicesConfig) {
        def string = configTemplate.getText(true, domain.class.simpleName,
                "properties", this,
                "domain", domain,
                "servicesConfig", servicesConfig)
        FileUtils.write new File(sitesAvailableDir, domain.fileName), string
    }
}
