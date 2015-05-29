/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.proxy.linux.AbstractNginxProxyConfig
import com.anrisoftware.sscontrol.httpd.proxy.ProxyService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Wordpress Nginx</i> proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class WordpressNginxProxyConfig extends AbstractNginxProxyConfig {

    /**
     * <i>Wordpress</i> proxy name.
     */
    public static final String SERVICE_NAME = "wordpress"

    @Inject
    private WordpressNginxProxyConfigLogger log

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource proxyConfigTemplate

    @Inject
    void setTemplates(TemplatesFactory factory) {
        def templates = factory.create "WordpressNginxProxyConfig"
        this.proxyConfigTemplate = templates.getResource "config"
    }

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        config.addAll createDomainConfig(domain, refDomain, service)
    }

    void deployService(Domain domain, WebService service, List config) {
        deployProxyDomainConfig(service)
        config.addAll createDomainConfig(domain, null, service)
    }

    List createDomainConfig(Domain domain, Domain refDomain, ProxyService service) {
        List list = []
        def configstr = proxyConfigTemplate.getText(
                true,
                "wordpressProxy",
                "args", [
                    properties: this,
                    script: script,
                    domain: domain,
                    proxy: service,
                    location: proxyLocation(service),
                    errorPagesDir: errorPagesDir
                ])
        list << configstr
    }

    @Override
    TemplateResource getProxyConfigTemplate() {
        proxyConfigTemplate
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }
}
