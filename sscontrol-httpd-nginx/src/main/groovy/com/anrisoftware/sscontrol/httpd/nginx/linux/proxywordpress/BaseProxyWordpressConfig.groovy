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
package com.anrisoftware.sscontrol.httpd.nginx.linux.proxywordpress

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.httpd.nginx.linux.nginx.NginxScript
import com.anrisoftware.sscontrol.httpd.nginx.linux.proxy.BaseProxyConfig
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyService
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * Configures proxy service for the Wordpress service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class BaseProxyWordpressConfig extends BaseProxyConfig {

    @Inject
    private BaseProxyWordpressConfigLogger log

    /**
     * The {@link Templates} for the proxy configuration.
     */
    Templates wordpressProxyTemplates

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource wordpressProxyConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        config.addAll createDomainConfig(domain, refDomain, service)
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        deployProxyConfig(service)
        config.addAll createDomainConfig(domain, null, service)
    }

    /**
     * Deploys the proxy configuration.
     */
    void deployProxyConfig(ProxyService service) {
        def file = proxyConfigFile service
        def conf = currentConfiguration file
        deployConfiguration configurationTokens(), conf, proxyConfigurations(service), file
    }

    /**
     * Returns the proxy configuration file.
     */
    File proxyConfigFile(ProxyService service) {
        new File(script.configIncludeDir, "100-robobee-${service.domain.name}-proxy.conf")
    }

    /**
     * Returns the included configuration.
     */
    List proxyConfigurations(ProxyService service) {
        [
            proxyCachePathConfig(service),
            proxyConnectTimeoutConfig(service),
            proxyReadTimeoutConfig(service),
            proxySendTimeoutConfig(service),
        ]
    }

    List createDomainConfig(Domain domain, Domain refDomain, ProxyService service) {
        List list = []
        def configStr = proxyConfigTemplate.getText(true,
                "wordpressProxy",
                "properties", script,
                "domain", domain,
                "proxy", service)
        list << configStr
    }

    /**
     * Sets the parent script with the properties.
     *
     * @param script
     *            the {@link NginxScript}.
     */
    @Override
    void setScript(NginxScript script) {
        super.setScript script
        this.wordpressProxyTemplates = templatesFactory.create "BaseProxyWordpressConfig"
        this.wordpressProxyConfigTemplate = proxyTemplates.getResource "config"
    }
}
