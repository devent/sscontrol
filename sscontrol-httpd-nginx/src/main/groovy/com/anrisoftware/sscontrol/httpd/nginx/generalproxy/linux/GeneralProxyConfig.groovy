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
package com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.proxy.linux.AbstractNginxProxyConfig
import com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceImpl
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * General proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class GeneralProxyConfig extends AbstractNginxProxyConfig {

    /**
     * General Proxy name.
     */
    public static final String SERVICE_NAME = "general"

    @Inject
    private GeneralProxyConfigLogger log

    @Inject
    TemplatesFactory templatesFactory

    /**
     * The {@link Templates} for the proxy configuration.
     */
    Templates generalProxyTemplates

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource generalProxyConfigTemplate

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        config.addAll createDomainConfig(domain, refDomain, service)
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        deployProxyConfiguration()
        deployProxyDomainConfig(service)
        config.addAll createDomainConfig(domain, null, service)
    }

    List createDomainConfig(Domain domain, Domain refDomain, ProxyServiceImpl service) {
        List list = []
        def configstr = generalProxyConfigTemplate.getText(
                true,
                "proxy",
                "properties", this,
                "script", script,
                "domain", domain,
                "proxy", service,
                "location", proxyLocation(service))
        log.domainConfigCreated script, domain, configstr
        list << configstr
    }

    void setScript(LinuxScript script) {
        super.setScript script
        this.generalProxyTemplates = templatesFactory.create "GeneralProxyConfig"
        this.generalProxyConfigTemplate = generalProxyTemplates.getResource "config"
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }
}
