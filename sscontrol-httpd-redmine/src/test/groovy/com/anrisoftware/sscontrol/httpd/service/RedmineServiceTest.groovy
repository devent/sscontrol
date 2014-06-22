/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.redmine.RedmineService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

/**
 * @see RedmineService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RedmineServiceTest extends HttpdTestUtil {

    @Test
    void "redmine"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService redmineScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        RedmineService webservice = domain.services[0]
        assert webservice.name == "redmine"
        assert webservice.id == "redmineid"
        assert webservice.backend == "thin"
        assert webservice.ref == null
        assert webservice.alias == null
        assert webservice.prefix == null
        assert webservice.ref == null
        assert webservice.refDomain == null
        assert webservice.debug.level == 4
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.database.database == "redmine2"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"

        domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        webservice = domain.services[0]
        assert webservice.name == "redmine"
        assert webservice.id == null
        assert webservice.backend == "thin"
        assert webservice.alias == null
        assert webservice.prefix == null
        assert webservice.ref == "redmineid"
        assert webservice.refDomain == null
        assert webservice.overrideMode == null

        domain = service.domains[d++]
        assert domain.name == "test2.com"
        assert domain.address == "192.168.0.52"

        webservice = domain.services[0]
        assert webservice.name == "redmine"
        assert webservice.id == "redmineid"
        assert webservice.backend == "thin"
        assert webservice.alias == "redmine"
        assert webservice.prefix == "test2redmine"
        assert webservice.ref == null
        assert webservice.refDomain == null
        assert webservice.overrideMode == null
    }
}
