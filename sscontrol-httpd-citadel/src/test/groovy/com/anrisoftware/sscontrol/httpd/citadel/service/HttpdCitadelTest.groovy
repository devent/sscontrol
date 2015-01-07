/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.citadel.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.httpd.citadel.AuthMethod
import com.anrisoftware.sscontrol.httpd.citadel.CitadelService
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.service.HttpdService

/**
 * @see CitadelService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdCitadelTest extends HttpdTestUtil {

    @Test
    void "citadel"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService citadelScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        CitadelService webservice = domain.services[0]
        assert webservice.name == "citadel"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.alias == ""
        assert webservice.bindingAddresses.size() == 1
        assert webservice.bindingAddresses["0.0.0.0"] == [504]
        assert webservice.authMethod == AuthMethod.selfContained
        assert webservice.adminUser == "admin"
        assert webservice.adminPassword == "adminpass"
    }
}
