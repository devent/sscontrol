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
package com.anrisoftware.sscontrol.httpd.piwik.nginx_proxy_ubuntu_12_04

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_12_04.Ubuntu_12_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.piwik.nginx_proxy.AbstractNginxProxyConfig
import com.anrisoftware.sscontrol.httpd.proxy.ProxyConfig
import com.anrisoftware.sscontrol.httpd.proxy.ProxyService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Ubuntu 12.04 Piwik Nginx</i> proxy configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends AbstractNginxProxyConfig implements ProxyConfig {

    /**
     * <i>Piwik</i> proxy name.
     */
    public static final String SERVICE_NAME = "piwik"

    @Inject
    UbuntuPropertiesProvider ubuntuPropertiesProvider

    Templates proxyTemplates

    TemplateResource proxyConfigTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.proxyTemplates = factory.create "Piwik_Proxy_Nginx_Ubuntu_12_04"
        this.proxyConfigTemplate = proxyTemplates.getResource "config"
    }

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

    List createDomainConfig(Domain domain, Domain refDomain, ProxyService service) {
        List list = []
        def configstr = proxyConfigTemplate.getText(
                true,
                "piwikProxy",
                "properties", this,
                "script", script,
                "domain", domain,
                "proxy", service,
                "location", proxyLocation(service))
        list << configstr
    }

    @Override
    ContextProperties getProxyProperties() {
        ubuntuPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ScriptFactory.PROFILE
    }

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource getProxyConfigTemplate() {
        proxyConfigTemplate
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }
}
