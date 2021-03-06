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

import com.anrisoftware.sscontrol.httpd.webdav.WebdavService
import com.anrisoftware.sscontrol.testutils.resources.HttpdTestEnvironment

/**
 * @see HttpdService
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdWebdavTest extends HttpdTestEnvironment {

    @Test
    void "webdav"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService webdavScript.resource, profile
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1
        assert service.virtualDomains.size() == 1
        def domain = service.domains[0]
        WebdavService webservice = domain.services[0]
        assert webservice.name == "webdav"
        assert webservice.id == "webdav-test1.com"
        assert webservice.alias == "webdav"
        assert webservice.ref == null
        assert webservice.refDomain == null
        assert webservice.methods.containsAll([
            "PUT",
            "DELETE",
            "MKCOL",
            "COPY",
            "MOVE"
        ])
    }
}
