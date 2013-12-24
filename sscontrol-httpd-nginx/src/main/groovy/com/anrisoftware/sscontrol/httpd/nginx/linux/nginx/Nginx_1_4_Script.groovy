/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.linux.nginx

import static com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx.Ubuntu10_04ScriptFactory.PROFILE
import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Configures Nginx 1.4 service.
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
    DebugLoggingRenderer debugLoggingRenderer

    @Inject
    RedirectConfig deployRedirectToWwwHttp

    @Inject
    RedirectConfig deployRedirectToWwwHttps

    /**
     * The {@link Templates} for the script.
     */
    Templates nginxTemplates

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
        nginxTemplates = templatesFactory.create "Nginx_1_4", ["renderers": [debugLoggingRenderer]]
        configTemplate = nginxTemplates.getResource "config"
        nginxCommandsTemplate = nginxTemplates.getResource "commands"
        domainConfig.script = this
        sslDomainConfig.script = this
        super.run()
        beforeConfiguration()
        setupDefaultBinding()
        setupDefaultLogging()
        createSitesDirectories()
        deployNginxConfig()
        deployIncludedConfig()
        deployConfig()
    }

    /**
     * Called before the configuration.
     */
    abstract void beforeConfiguration()

    void setupDefaultBinding() {
        if (service.binding.size() == 0) {
            defaultBinding.each { service.binding.addAddress(it) }
        }
    }

    void setupDefaultLogging() {
        if (!service.debug.args.containsKey("storage")) {
            service.debug.args.storage = loggingStorage
        }
    }

    /**
     * Creates sites configuration directories.
     *
     * @see #getSitesAvailableDir()
     * @see #getSitesEnabledDir()
     */
    void createSitesDirectories() {
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
        ]
    }

    def configErrorLog(HttpdService service) {
        def search = configTemplate.getText(true, "errorLog_search")
        def replace = configTemplate.getText(true, "errorLog", "debug", service.debug)
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the included configuration.
     */
    void deployIncludedConfig() {
        def file = configIncludeFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, includedConfigs(service), file
    }

    /**
     * Returns the included configuration.
     */
    List includedConfigs(HttpdService service) {
        [
            domainCompression(service),
            compressionLevel(service),
            compressionMinSize(service),
            compressionVary(service),
            compressionTypes(service),
            compressionException(service),
            compressionHttpVersion(service),
            includeSitesEnabled(service),
        ]
    }

    def domainCompression(HttpdService service) {
        def search = configTemplate.getText(true, "domainCompression_search")
        def replace = configTemplate.getText(true, "domainCompression", "enabled", domainCompression)
        new TokenTemplate(search, replace)
    }

    def compressionLevel(HttpdService service) {
        def search = configTemplate.getText(true, "compressionLevel_search")
        def replace = configTemplate.getText(true, "compressionLevel", "level", compressionLevel)
        new TokenTemplate(search, replace)
    }

    def compressionMinSize(HttpdService service) {
        def search = configTemplate.getText(true, "compressionMinSize_search")
        def replace = configTemplate.getText(true, "compressionMinSize", "size", compressionMinSize)
        new TokenTemplate(search, replace)
    }

    def compressionVary(HttpdService service) {
        def search = configTemplate.getText(true, "compressionVary_search")
        def replace = configTemplate.getText(true, "compressionVary", "enabled", compressionVary)
        new TokenTemplate(search, replace)
    }

    def compressionTypes(HttpdService service) {
        def search = configTemplate.getText(true, "compressionTypes_search")
        def replace = configTemplate.getText(true, "compressionTypes", "types", compressionTypes)
        new TokenTemplate(search, replace)
    }

    def compressionException(HttpdService service) {
        def search = configTemplate.getText(true, "compressionException_search")
        def replace = configTemplate.getText(true, "compressionException", "exception", compressionException)
        new TokenTemplate(search, replace)
    }

    def compressionHttpVersion(HttpdService service) {
        def search = configTemplate.getText(true, "compressionHttpVersion_search")
        def replace = configTemplate.getText(true, "compressionHttpVersion", "version", compressionHttpVersion)
        new TokenTemplate(search, replace)
    }

    def includeSitesEnabled(HttpdService service) {
        def search = configTemplate.getText(true, "includeSitesEnabled_search", "dir", sitesEnabledDir)
        def replace = configTemplate.getText(true, "includeSitesEnabled", "dir", sitesEnabledDir)
        new TokenTemplate(search, replace)
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
            enableSites([domain.fileName])
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
        def file = new File(sitesAvailableDir, domain.fileName)
        FileUtils.write file, string
        log.deployDomainConfig this, domain, file
    }
}
