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
package com.anrisoftware.sscontrol.httpd.nginx.staticcache.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.nginx.nginx.linux.ExpiresDurationRenderer
import com.anrisoftware.sscontrol.httpd.staticservice.IndexMode
import com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Nginx</i> static files cache configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class NginxStaticCacheConfig extends AbstractStaticCacheConfig {

    /**
     * Static files cache service name.
     */
    public static final String SERVICE_NAME = "static-cache"

    @Inject
    private NginxStaticCacheConfigLogger log

    @Inject
    private ExpiresDurationRenderer expiresDurationRenderer

    /**
     * Resource containing the static file domain configuration templates.
     */
    TemplateResource domainConfigTemplate

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        def templates = factory.create "StaticCacheConfig", [renderers: [expiresDurationRenderer]]
        this.domainConfigTemplate = templates.getResource "domain_config"
    }

    @Override
    void deployDomain(Domain domain, Domain refDomain, WebService service, List config) {
        setupDefaults service
        createDomainConfig domain, refDomain, service, config
        attachReferencedServices domain, service, config
    }

    @Override
    void deployService(Domain domain, WebService service, List config) {
        setupDefaults service
        createDomainConfig domain, null, service, config
        attachReferencedServices domain, service, config
    }

    List createDomainConfig(Domain domain, Domain refDomain, StaticCacheService service, List config) {
        def args = [:]
        args.location = staticLocation(service)
        args.indexFiles = service.indexFiles
        args.autoIndex = service.indexMode == IndexMode.auto
        args.staticFiles = staticFiles
        args.expiresDuration = service.expiresDuration
        args.enabledAccessLog = service.enabledAccessLog
        createConfig config, "domainConfig", domain, args
        config
    }

    List createConfig(List config, String name, def domain, def args) {
        def configstr = domainConfigTemplate.getText(true, name, "args", args)
        config << configstr
        log.domainConfigCreated script, domain, configstr
        config
    }

    String getLocationConfig(Domain domain, StaticCacheService service) {
        def args = [:]
        args.location = staticLocation(service)
        args.staticFiles = staticFiles
        domainConfigTemplate.getText true, "locationConfig", "args", args
    }

    @Override
    String getServiceName() {
        SERVICE_NAME
    }
}
