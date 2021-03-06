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
package com.anrisoftware.sscontrol.httpd.nginx.proxypass.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.proxy.linux.AbstractNginxProxyConfig
import com.anrisoftware.sscontrol.httpd.proxy.ProxyService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * Proxy pass.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class ProxyPassConfig extends AbstractNginxProxyConfig {

    /**
     * Proxy pass name.
     */
    public static final String SERVICE_NAME = "pass"

    @Inject
    private ProxyPassConfigLogger log

    @Inject
    TemplatesFactory templatesFactory

    /**
     * The {@link Templates} for the proxy configuration.
     */
    Templates proxyTemplates

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource proxyConfigTemplate

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        createDomainConfig domain, refDomain, service, config
    }

    void deployService(Domain domain, WebService service, List config) {
        createDomainConfig domain, null, service, config
    }

    List createDomainConfig(Domain domain, Domain refDomain, ProxyService service, List config) {
        def setupProxyTimeouts = config.find { String it ->
            it.contains("# proxy timeouts\n")
        }
        def setupProxyHeaders = config.find { String it ->
            it.contains("# proxy headers\n")
        }
        def configstr = proxyConfigTemplate.getText(
                true,
                "domainConfig",
                "args",
                [
                    properties: this,
                    script: script,
                    domain: domain,
                    proxy: service,
                    location: proxyLocation(service),
                    errorPagesDir: errorPagesDir,
                    setupProxyTimeouts: setupProxyTimeouts == null,
                    setupProxyHeaders: setupProxyHeaders == null,
                ])
        log.domainConfigCreated script, domain, configstr
        config << configstr
    }

    @Override
    TemplateResource getProxyConfigTemplate() {
        proxyConfigTemplate
    }

    void setScript(LinuxScript script) {
        super.setScript script
        this.proxyTemplates = templatesFactory.create "ProxyPassConfig"
        this.proxyConfigTemplate = proxyTemplates.getResource "config"
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }
}
