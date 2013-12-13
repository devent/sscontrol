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
package com.anrisoftware.sscontrol.httpd.apache.linux.apache

import static com.anrisoftware.sscontrol.httpd.apache.ubuntu_10_04.apache.Ubuntu10_04ScriptFactory.PROFILE
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
 * Configures Apache 2.2 service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Apache_2_2Script extends ApacheScript {

    @Inject
    Apache_2_2ScriptLogger log

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    @Inject
    RedirectConfig deployRedirectHttpToHttps

    @Inject
    RedirectConfig deployRedirectToWwwHttp

    @Inject
    RedirectConfig deployRedirectToWwwHttps

    /**
     * The {@link Templates} for the script.
     */
    Templates apacheTemplates

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
        configTemplate = apacheTemplates.getResource "config"
        apacheCommandsTemplate = apacheTemplates.getResource "commands"
        domainConfig.script = this
        sslDomainConfig.script = this
        deployRedirectHttpToHttps.script = this
        deployRedirectToWwwHttp.script = this
        deployRedirectToWwwHttps.script = this
        super.run()
        deployDefaultConfig()
        deployDomainsConfig()
        enableDefaultMods()
        deployConfig()
        restartServices()
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
        List serviceConfig = []
        uniqueDomains.each { deployDomain it }
        service.domains.each { Domain domain ->
            deployRedirect domain
            deployAuth domain, serviceConfig
            deployService domain, serviceConfig
            deployDomainConfig domain, serviceConfig
            deploySslDomain domain
            enableSites domain.fileName
        }
    }

    def deployService(Domain domain, List serviceConfig) {
        domain.services.each { WebService service ->
            def config = serviceConfigs["${PROFILE}.${service.name}"]
            log.checkServiceConfig config, service
            config.deployService(domain, service, serviceConfig)
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