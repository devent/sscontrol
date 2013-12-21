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
package com.anrisoftware.sscontrol.httpd.nginx.linux.nginx

import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx.Ubuntu10_04ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * Configures Nginx 1.4 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Nginx_1_4_Script extends NginxScript {

    @Inject
    Nginx_1_4_ScriptLogger log

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    /**
     * The {@link Templates} for the script.
     */
    Templates apacheTemplates

    /**
     * Resource containing the configuration templates.
     */
    TemplateResource configTemplate

    /**
     * Resource containing the commands templates.
     */
    TemplateResource nginxCommandsTemplate

    @Override
    def run() {
        apacheTemplates = templatesFactory.create "Nginx_1_4"
        configTemplate = apacheTemplates.getResource "config"
        nginxCommandsTemplate = apacheTemplates.getResource "commands"
        domainConfig.script = this
        sslDomainConfig.script = this
        super.run()
        setupDefaultBinding()
        deployDefaultConfig()
        deployDomainsConfig()
        enableDefaultMods()
        deployConfig()
        restartServices()
    }

    def setupDefaultBinding() {
        if (service.binding.size() == 0) {
            defaultBinding.each { service.binding.addAddress(it) }
        }
    }

    def deployDefaultConfig() {
        def string = configTemplate.getText true, "defaultConfiguration"
        FileUtils.write defaultConfigFile, string
    }

    def deployDomainsConfig() {
        def string = configTemplate.getText true, "domainsConfiguration", "service", service
        FileUtils.write domainsConfigFile, string
    }

    def enableDefaultMods() {
        enableMods "suexec"
    }

    def deployConfig() {
        uniqueDomains.each { deployDomain it }
        service.domains.each { Domain domain ->
            List serviceConfig = []
            deployRedirect domain
            deployAuth domain, serviceConfig
            deployService domain, serviceConfig
            deployDomainConfig domain, serviceConfig
            deploySslDomain domain
            enableSites domain.fileName
        }
    }

    def deployService(Domain domain, List serviceConfig) {
        domain.services.findAll { WebService service ->
            service.domain == domain
        }.each { WebService service ->
            def reftarget = findReferencedService service
            def config = serviceConfigs["${PROFILE}.${service.name}"]
            if (reftarget == null) {
                log.checkServiceConfig config, service
                config.deployService domain, service, serviceConfig
            } else {
                log.checkServiceConfig config, service
                def refdomain = findReferencedDomain service
                if (refdomain == null) {
                    config.deployDomain domain, null, reftarget, serviceConfig
                } else {
                    config.deployDomain domain, refdomain, reftarget, serviceConfig
                }
            }
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

    def deployRedirect(Domain domain) {
        domain.redirects.each {
            this."deploy${it.class.simpleName}".deployRedirect(domain, it)
        }
    }

    def deployAuth(Domain domain, List serviceConfig) {
        domain.auths.each { AbstractAuth auth ->
            def config = authConfigs["${PROFILE}.${auth.class.simpleName}"]
            log.checkAuthConfig config, auth
            config.deployAuth(domain, auth, serviceConfig)
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
