/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.proxy.ProxyService
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * @see WordpressService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdProxyTest extends HttpdTestUtil {

    @Test
    void "proxy"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        profile.getEntry("httpd").service(["idapache2": "apache"], ["idproxy": "nginx"])
        loader.loadService proxyDomainsScript.resource, profile
        loader.loadService proxyScript.resource, profile

        HttpdServiceImpl service = registry.getService("httpd")[0]
        assert service.domains.size() == 2
        assert service.virtualDomains.size() == 2

        service = registry.getService("httpd")[1]
        assert service.domains.size() == 2
        assert service.virtualDomains.size() == 2

        Domain domain = service.domains[0]
        WebService webservice = domain.services[0]
        assert webservice.class == ProxyService
        assert webservice.name == "proxy.servicefoo"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.service == "servicefoo"
        assert webservice.address == "http://127.0.0.1:8080"
        assert webservice.alias == "fooalias"
        assert webservice.proxyName == "servicefoo_fooalias"

        domain = service.domains[1]
        webservice = domain.services[0]
        assert webservice.class == ProxyService
        assert webservice.name == "proxy.servicebar"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.service == "servicebar"
        assert webservice.address == "http://127.0.0.1:8080"
        assert webservice.proxyName == "bar"
    }
}
