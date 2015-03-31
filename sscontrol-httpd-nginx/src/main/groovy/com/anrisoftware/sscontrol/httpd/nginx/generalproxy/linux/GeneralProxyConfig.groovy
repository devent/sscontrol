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
package com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux

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
    Templates proxyTemplates

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource proxyConfigTemplate

    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        createDomainConfig domain, refDomain, service, config
    }

    void deployService(Domain domain, WebService service, List config) {
        deployProxyDomainConfig service
        createDomainConfig domain, null, service, config
    }

    List createDomainConfig(Domain domain, Domain refDomain, ProxyService service, List config) {
        def setupProxyTimeouts = config.find { String it ->
            it.contains("# proxy timeouts\n")
        }
        def setupProxyHeaders = config.find { String it ->
            it.contains("# proxy headers\n")
        }
        def args = [:]
        args.properties = this
        args.domain = domain
        args.proxy = service
        args.location = proxyLocation(service)
        args.errorPagesDir = errorPagesDir
        if (setupProxyTimeouts == null) {
            createConfig config, "proxyTimeouts", domain, args
        }
        if (setupProxyHeaders == null) {
            createConfig config, "proxyHeaders", domain, args
        }
        createConfig config, "domainConfig", domain, args
        if (service.cacheStaticFiles) {
            createConfig config, "domainCacheStaticFiles", domain, args
        }
        if (service.cacheFeeds) {
            createConfig config, "domainCacheFeeds", domain, args
        }
        config
    }

    List createConfig(List config, String name, def domain, def args) {
        def configstr = proxyConfigTemplate.getText(true, name, "args", args)
        config << configstr
        log.domainConfigCreated script, domain, configstr
        config
    }

    /**
     * Returns the list of static files, for
     * example {@code "jpg, png, gif, jpeg, css, js, mp3, wav, swf, mov, doc, pdf, xls, ppt, docx, pptx, xlsx".}
     *
     * <ul>
     * <li>profile property {@code "proxy_static_files"}</li>
     * </ul>
     *
     * @see #getProxyProperties()
     */
    List getStaticFiles() {
        profileListProperty "proxy_static_files", proxyProperties
    }

    /**
     * Resource containing the proxy configuration templates.
     */
    TemplateResource getProxyConfigTemplate() {
        proxyConfigTemplate
    }

    void setScript(LinuxScript script) {
        super.setScript script
        this.proxyTemplates = templatesFactory.create "GeneralProxyConfig"
        this.proxyConfigTemplate = proxyTemplates.getResource "config"
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }
}
