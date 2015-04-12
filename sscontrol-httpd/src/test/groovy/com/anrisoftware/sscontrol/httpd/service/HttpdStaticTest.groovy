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

import com.anrisoftware.sscontrol.httpd.staticservice.IndexMode
import com.anrisoftware.sscontrol.httpd.staticservice.StaticCacheService
import com.anrisoftware.sscontrol.httpd.staticservice.StaticService
import com.anrisoftware.sscontrol.testutils.resources.HttpdTestEnvironment

/**
 * @see HttpdService
 * @see Httpd
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdStaticTest extends HttpdTestEnvironment {

    @Test
    void "httpd static"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService staticScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1

        StaticService webservice = service.domains[0].services[0]
        assert webservice.name == "static"
        assert webservice.id == "static-test1.com"
        assert webservice.alias == "static"
        assert webservice.indexFiles.size() == 3
        assert webservice.indexFiles.containsAll([
            "index.\$geo.html",
            "index.htm",
            "index.html"
        ])
        assert webservice.indexMode == IndexMode.auto
    }

    @Test
    void "httpd static cache"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService staticCacheScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        assert service.domains.size() == 1

        StaticCacheService webservice = service.domains[0].services[0]
        assert webservice.name == "static-cache"
        assert webservice.id == "static-cache-test1.com"
        assert webservice.alias == "static"
        assert webservice.indexFiles.size() == 3
        assert webservice.indexFiles.containsAll([
            "index.\$geo.html",
            "index.htm",
            "index.html"
        ])
        assert webservice.indexMode == IndexMode.auto
        assert webservice.includeRefs.size() == 2
        assert webservice.includeRefs.containsAll([
            "webdav-test1.com",
            "auth-test1.com"
        ])

        assert webservice.expiresDuration.toString() == "PT86400S"
        assert webservice.enabledAccessLog == true
        assert webservice.headersValues.size() == 1
        assert webservice.headersValues["Cache-Control"] == "public"
        assert webservice.includeRefs.size() == 2
        assert webservice.includeRefs.containsAll([
            "webdav-test1.com",
            "auth-test1.com"
        ])
    }
}
