/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.phpmyadmin

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.phpmyadmin.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.testutils.resources.HttpdTestEnvironment

/**
 * @see PhpmyadminService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdPhpmyadminTest extends HttpdTestEnvironment {

    @Test
    void "phpmyadmin"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService phpmyadminScript.resource, profile
        HttpdService service = registry.getService("httpd")[0]

        Domain domain = service.domains[0]
        PhpmyadminService phpservice = domain.services[0]
        assert phpservice.name == "phpmyadmin"
        assert phpservice.alias == "phpmyadmin"
        assert phpservice.adminUser == "root"
        assert phpservice.adminPassword == "rootpass"
        assert phpservice.controlUser == "phpmyadmin"
        assert phpservice.controlPassword == "somepass"
        assert phpservice.controlDatabase == "phpmyadmin"
        assert phpservice.server == "127.0.0.1"
        assert phpservice.serverPort == 3306
    }
}
