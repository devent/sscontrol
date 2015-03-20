/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.yourls.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.yourls.Access
import com.anrisoftware.sscontrol.httpd.yourls.YourlsService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see YourlsService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdYourlsTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "yourls service"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService yourlsScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        YourlsService webservice = domain.services[0]
        assert webservice.name == "yourls"
        assert webservice.id == "yourlsid"
        assert webservice.ref == "yourlsid"
        assert webservice.alias == "yourls"
        assert webservice.prefix == "test1comyourls"
        assert webservice.debugLogging("level").size() == 1
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "yourlsdb"
        assert webservice.database.user == "yourlsuser"
        assert webservice.database.password == "yourlspass"
        assert webservice.database.host == "localhost"
        assert webservice.database.port == 3306
        assert webservice.database.prefix == "yourls_"
        assert webservice.siteAccess == Access.open
        assert webservice.statsAccess == Access.open
        assert webservice.apiAccess == Access.open
        assert webservice.users.size() == 3
        assert webservice.users["admin"] == "mypass"
        assert webservice.users["foo"] == "foopass"
        assert webservice.users["bar"] == "barpass"
    }
}
