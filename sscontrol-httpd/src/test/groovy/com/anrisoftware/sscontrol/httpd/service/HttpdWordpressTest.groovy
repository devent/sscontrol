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
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService
import com.anrisoftware.sscontrol.httpd.statements.wordpress.MultiSite
import com.anrisoftware.sscontrol.httpd.statements.wordpress.WordpressService

/**
 * @see WordpressService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdWordpressTest extends HttpdTestUtil {

    @Test
    void "wordpress"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService wordpressScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]

        Domain domain = service.domains[0]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.50"
        assert (domain instanceof Domain)

        domain = service.domains[1]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.50"
        assert (domain instanceof SslDomain)

        domain = service.domains[2]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof Domain)
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        WebService webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.id == "wordpress3"
        assert webservice.ref == null
        assert webservice.alias == "wordpress3"
        assert webservice.database.database == "wordpress3"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"

        domain = service.domains[3]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof SslDomain)
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.id == null
        assert webservice.ref == "wordpress3"
        assert webservice.database == null

        domain = service.domains[4]
        assert domain.name == "www.test2.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof Domain)
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.multiSite == MultiSite.subdir

        domain = service.domains[5]
        assert domain.name == "www.test3.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof Domain)
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.multiSite == MultiSite.subdomain

        domain = service.domains[6]
        assert domain.name == "www.testid.com"
        assert domain.address == "192.168.0.51"
        assert domain.id == "testid"
        assert (domain instanceof Domain)
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.id == "wordpress3"

        domain = service.domains[7]
        assert domain.name == "www.testref.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof Domain)
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress"
        assert webservice.ref == "wordpress3"
        assert webservice.refDomain == "testid"
    }
}
