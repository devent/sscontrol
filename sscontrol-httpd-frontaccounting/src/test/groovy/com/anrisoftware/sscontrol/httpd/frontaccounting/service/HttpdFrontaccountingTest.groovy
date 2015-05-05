/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.frontaccounting.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.frontaccounting.FrontaccountingService
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see FrontaccountingService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdFrontaccountingTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "frontaccounting service"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService frontaccountingScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        FrontaccountingService webservice = domain.services[0]
        assert webservice.name == "frontaccounting_2_3"
        assert webservice.id == "faccid"
        assert webservice.alias == "account"
        assert webservice.prefix == "frontaccounting_2_3"
        assert webservice.ref == "faccidref"
        assert webservice.debugLogging("level").size() == 6
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.debugLogging("level")["sql"] == 1
        assert webservice.debugLogging("level")["go"] == 1
        assert webservice.debugLogging("level")["pdf"] == 1
        assert webservice.debugLogging("level")["sqltrail"] == 1
        assert webservice.debugLogging("level")["select"] == 1
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "faccountingdb"
        assert webservice.database.user == "faccountinguser"
        assert webservice.database.password == "faccountingpass"
        assert webservice.database.host == "localhost"
        assert webservice.database.port == 3306
        assert webservice.siteTitle == "My Company Pvt Ltd"
        assert webservice.locales.size() == 2
        assert webservice.locales.containsAll(["de", "pt"])
    }
}
