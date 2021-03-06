/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.domain.SslDomainImpl
import com.anrisoftware.sscontrol.httpd.domain.linux.DomainConfig
import com.anrisoftware.sscontrol.httpd.domain.linux.SslDomainConfig
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.FindServiceConfigWorkerFactory
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.NginxScript
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.SimpleDurationRenderer
import com.anrisoftware.sscontrol.httpd.nginx.nginxconfig.NginxConfigListFactory
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.WebService
import com.anrisoftware.sscontrol.scripts.unix.StopServicesFactory

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
    NginxConfigListFactory nginxConfigListFactory

    @Inject
    FindServiceConfigWorkerFactory findServiceConfigWorkerFactory

    @Inject
    DebugLoggingRenderer debugLoggingRenderer

    @Inject
    DomainConfig domainConfig

    @Inject
    SslDomainConfig sslDomainConfig

    @Inject
    RedirectConfig redirectConfig

    @Inject
    ErrorPageConfig errorPageConfig

    @Inject
    StopServicesFactory stopServicesFactory

    /**
     * Resource containing the default configuration.
     */
    TemplateResource defaultConfigTemplate

    /**
     * Resource containing the domain configuration.
     */
    TemplateResource domainConfigTemplate

    @Override
    def run() {
        super.run()
        domainConfig.script = this
        sslDomainConfig.script = this
        redirectConfig.script = this
        errorPageConfig.script = this
        stopServices()
        beforeConfiguration()
        createSitesDirectories()
        deployNginxConfig service
        deployIncludedConfig()
        deployConfig()
        createCacheDirectory()
    }

    @Inject
    final void setNginx14ScriptTemplates(TemplatesFactory factory,
            ResourceURIAttributeRenderer resourceURIRenderer,
            SimpleDurationRenderer simpleDurationRenderer) {
        def templates = factory.create "Nginx_1_4", ["renderers": [
                resourceURIRenderer,
                simpleDurationRenderer
            ]]
        this.defaultConfigTemplate = templates.getResource "default_config"
        this.domainConfigTemplate = templates.getResource "domain_config"
    }

    /**
     * Stop <i>Nginx</i> services.
     */
    void stopServices() {
        if (new File(stopCommand).exists()) {
            stopServicesFactory.create(
                    log: log.log,
                    runCommands: runCommands,
                    command: stopCommand,
                    services: stopServices,
                    flags: stopFlags,
                    this, threads)()
        }
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
     * Creates the cache directory.
     *
     */
    void createCacheDirectory() {
        proxyCacheDir.mkdirs()
    }

    /**
     * Deploys the configuration of the <i>Nginx</i> service.
     *
     * @param service
     *            the {@link HttpdService} httpd service.
     */
    void deployNginxConfig(HttpdService service) {
        def file = nginxConfigFile
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, mainConfigurations(service), file
    }

    /**
     * Returns the <i>Nginx</i> service configurations.
     */
    List mainConfigurations(HttpdService service) {
        [
            configErrorLog(service),
            configWorkerProcesses(service),
            configGzipOn(service),
            configGzipDisable(service),
        ]
    }

    def configErrorLog(HttpdService service) {
        def search = defaultConfigTemplate.getText(true, "errorLog_search")
        def replace = defaultConfigTemplate.getText(true, "errorLog", "debug", debugLoggingRenderer.toString(service))
        new TokenTemplate(search, replace)
    }

    def configWorkerProcesses(HttpdService service) {
        def search = defaultConfigTemplate.getText(true, "workerProcesses_search")
        def replace = defaultConfigTemplate.getText(true, "workerProcesses", "processes", workerProcesses)
        new TokenTemplate(search, replace)
    }

    def configGzipOn(HttpdService service) {
        def search = defaultConfigTemplate.getText(true, "gzipOnConfigSearch")
        def replace = defaultConfigTemplate.getText(true, "gzipOnConfig")
        new TokenTemplate(search, replace)
    }

    def configGzipDisable(HttpdService service) {
        def search = defaultConfigTemplate.getText(true, "gzipDisableConfigSearch")
        def replace = defaultConfigTemplate.getText(true, "gzipDisableConfig")
        new TokenTemplate(search, replace)
    }

    /**
     * Deploys the included configuration.
     */
    void deployIncludedConfig() {
        def file = configIncludeFile
        def confstr = defaultConfigTemplate.getText(true, "defaultConfig", "properties", this)
        FileUtils.write file, confstr, charset
    }

    void deployConfig() {
        uniqueDomains.each { deployDomain it }
        service.domains.each { Domain domain ->
            List serviceConfig = nginxConfigListFactory.create()
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
        def config = findServiceConfigWorkerFactory.create(this).findServiceConfig profile, service
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
        def args = [:]
        args.servicesConfig = servicesConfig
        args.sitesDirectory = sitesDirectory
        args.indexFiles = indexFiles
        args.clientMaxBodySize = toMegabytes(domain.memory.upload)
        args.sslSubdirectory = sslSubdirectory
        args.sslSessionTimeout = sslSessionTimeout
        args.sslProtocols = sslProtocols
        args.sslCiphers = sslCiphers
        args.sslSessionCache = sslSessionCache
        args.sslPreferServerCiphers = sslPreferServerCiphers
        args.domainName = domain.name
        args.domainAddress = domain.address
        args.domainPort = domain.port
        args.domainSiteDirectory = domain.siteDirectory
        if (domain instanceof SslDomain) {
            args.domainCertResource = domain.certResource
            args.domainKeyResource = domain.keyResource
        }
        def string = domainConfigTemplate.getText(true, domain.class.simpleName, "args", args)
        def file = new File(sitesAvailableDir, domain.fileName)
        FileUtils.write file, string
        log.deployDomainConfig this, domain, file
    }
}
