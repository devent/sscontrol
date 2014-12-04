/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.SslDomainImpl
import com.anrisoftware.sscontrol.httpd.domain.linux.DomainConfig
import com.anrisoftware.sscontrol.httpd.domain.linux.SslDomainConfig
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScript
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Configures <i>Nginx 1.4</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Nginx_1_4_Script extends NginxScript {

    @Inject
    private Nginx_1_4_ScriptLogger log

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    @Inject
    RedirectConfig redirectConfig

    @Inject
    ErrorPageConfig errorPageConfig

    /**
     * Resource containing the <i>Nginx</i> configuration templates.
     */
    TemplateResource nginxConfigTemplate

    /**
     * Resource containing the <i>Nginx</i> commands templates.
     */
    TemplateResource nginxCommandsTemplate

    @Override
    def run() {
        super.run()
        domainConfig.script = this
        sslDomainConfig.script = this
        redirectConfig.script = this
        errorPageConfig.script = this
        serviceConfigs.values().each { it.script = this }
        beforeConfiguration()
        createSitesDirectories()
        deployNginxConfig()
        deployIncludedConfig()
        deploySitesConfig()
        deployConfig()
    }

    @Inject
    final void setNginx14ScriptTemplates(
            TemplatesFactory factory,
            DebugLoggingRenderer debugLoggingRenderer,
            ResourceURIAttributeRenderer resourceURIAttributeRenderer) {
        def templates = factory.create "Nginx_1_4", ["renderers": [
                debugLoggingRenderer,
                resourceURIAttributeRenderer
            ]]
        this.nginxConfigTemplate = templates.getResource "config"
        this.nginxCommandsTemplate = templates.getResource "commands"
    }

    /**
     * Called before the configuration.
     */
    abstract void beforeConfiguration()

    /**
     * Creates sites configuration directories.
     *
     * @see #getSitesDirectory()
     * @see #getSitesAvailableDir()
     * @see #getSitesEnabledDir()
     */
    void createSitesDirectories() {
        sitesDirectory.mkdirs()
        sitesAvailableDir.mkdirs()
        sitesEnabledDir.mkdirs()
    }

    /**
     * Deploys the configuration of the Nginx service.
     */
    void deployNginxConfig() {
        def file = nginxConfigFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, mainConfigurations(service), file
    }

    /**
     * Returns the Nginx service configurations.
     */
    List mainConfigurations(HttpdService service) {
        [
            configErrorLog(service),
            configWorkerProcesses(service),
        ]
    }

    def configErrorLog(HttpdService service) {
        def search = nginxConfigTemplate.getText(true, "errorLog_search")
        def replace = nginxConfigTemplate.getText(true, "errorLog", "debug", service.debug)
        new TokenTemplate(search, replace)
    }

    def configWorkerProcesses(HttpdService service) {
        def search = nginxConfigTemplate.getText(true, "workerProcesses_search")
        def replace = nginxConfigTemplate.getText(true, "workerProcesses", "processes", workerProcesses)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the included configuration.
     */
    void deployIncludedConfig() {
        def file = configIncludeFile
        def confstr = nginxConfigTemplate.getText(true, "robobeeConfig", "properties", this)
        FileUtils.write file, confstr, charset
    }

    /**
     * Deploys the sites configuration.
     */
    void deploySitesConfig() {
        def file = configSitesFile
        def confstr = nginxConfigTemplate.getText(true, "sitesConfig", "properties", this)
        FileUtils.write file, confstr, charset
    }

    void deployConfig() {
        uniqueDomains.each { deployDomain it }
        service.domains.each { Domain domain ->
            List serviceConfig = []
            setupDefaults domain
            deployRedirect domain, serviceConfig
            deployService domain, serviceConfig
            deployErrorPage domain, serviceConfig
            deployDomainConfig domain, serviceConfig
            deploySslDomain domain
            enableSites([domain.fileName])
        }
    }

    void deployService(Domain domain, List serviceConfig) {
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

    void deployErrorPage(Domain domain, List serviceConfig) {
        errorPageConfig.deployConfig domain, serviceConfig
    }

    def deploySslDomain(Domain domain) {
        if (domain.class == SslDomainImpl) {
            sslDomainConfig.deployCertificates(domain)
        }
    }

    def deployDomain(Domain domain) {
        domainConfig.deployDomain domain
    }

    def deployDomainConfig(Domain domain, List servicesConfig) {
        def string = nginxConfigTemplate.getText(
                true, domain.class.simpleName,
                "properties", [
                    script: this,
                    domain: domain,
                    servicesConfig: servicesConfig,
                    upload: toMegabytes(domain.memory.upload)])
        def file = new File(sitesAvailableDir, domain.fileName)
        FileUtils.write file, string
        log.deployDomainConfig this, domain, file
    }
}
