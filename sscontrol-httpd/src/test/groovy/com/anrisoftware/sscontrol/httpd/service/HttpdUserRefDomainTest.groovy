/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

/**
 * @see HttpdServiceImpl
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdUserRefDomainTest extends HttpdTestUtil {

    @Test
    void "user ref domain"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService httpdUserRefDomainScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]

        assert service.domains.size() == 8
        assert service.virtualDomains.size() == 2

        int i = 0
        def domain = service.domains[i++]
        assert domain.name == "test1.com"
        assert domain.id == "test1"
        assert domain.domainUser.ref == null
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "test1.com"
        assert domain.id == null
        assert domain.domainUser.ref == "test1"
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "test2.com"
        assert domain.id == null
        assert domain.domainUser.ref == null
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "www.test1.com"
        assert domain.id == null
        assert domain.domainUser.ref == null
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "test3.com"
        assert domain.id == "test3"
        assert domain.domainUser.ref == null
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "www.test3.com"
        assert domain.domainUser.ref == "test3"
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "test4.com"
        assert domain.domainUser.ref == "test3"
        assert domain.domainUser.name == null

        domain = service.domains[i++]
        assert domain.name == "www.test4.com"
        assert domain.domainUser.ref == "test3"
        assert domain.domainUser.name == null
    }
}
