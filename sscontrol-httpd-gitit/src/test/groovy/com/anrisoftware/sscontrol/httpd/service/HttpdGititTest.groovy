/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.gitit.AuthMethod
import com.anrisoftware.sscontrol.httpd.gitit.GititService
import com.anrisoftware.sscontrol.httpd.gitit.LoginRequired
import com.anrisoftware.sscontrol.httpd.gitit.RepositoryType

/**
 * @see GititService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdGititTest extends HttpdTestUtil {

    @Test
    void "gitit"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService gititScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        GititService webservice = domain.services[0]
        assert webservice.binding.size() == 1
        assert webservice.name == "gitit"
        assert webservice.id == "gititid"
        assert webservice.ref == null
        assert webservice.alias == ""
        assert webservice.type == RepositoryType.git
        assert webservice.debug.level == 2
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.wikiTitle == "Wiki Foo"
        assert webservice.loginRequired == LoginRequired.modify
        assert webservice.authMethod == AuthMethod.form
        assert webservice.pageType == "Markdown"
        assert webservice.math == "MathML"
        assert webservice.frontPage == "Front Page"
        assert webservice.noDeletePages.containsAll(["Front Page", "Help"])
        assert webservice.noEditPages.containsAll(["Help"])
        assert webservice.defaultSummary == "Default"
        assert webservice.tableOfContents == true
        assert webservice.caching == true
        assert webservice.idleGc == true
        assert webservice.memoryUpload == 100
        assert webservice.memoryPage == 100
        assert webservice.compressResponses == true
        assert webservice.recaptchaEnable == true
        assert webservice.recaptchaPrivateKey == "private.key"
        assert webservice.recaptchaPublicKey == "public.key"
        assert webservice.accessQuestion == "Foo?"
        assert webservice.accessAnswers == "Bar"
        assert webservice.feedsEnabled == true
        assert webservice.feedsDuration == 5
        assert webservice.feedsRefresh == 10

        domain = service.domains[d++]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"

        webservice = domain.services[0]
        assert webservice.name == "gitit"
        assert webservice.id == null
        assert webservice.ref == "gititid"
        assert webservice.refDomain == "testid"
    }
}
