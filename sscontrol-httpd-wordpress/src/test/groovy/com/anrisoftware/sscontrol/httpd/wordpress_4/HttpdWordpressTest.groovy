/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress_4

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.wordpress_4.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.wordpress.MultiSite
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see WordpressService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdWordpressTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "wordpress service"() {
        loader.loadService profile.resource, null, null
        def profile = registry.getService("profile")[0]
        loader.loadService wordpressScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0

        Domain domain = service.domains[d++]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        WordpressService webservice = domain.services[0]
        assert webservice.name == "wordpress_4"
        assert webservice.id == "wordpress3"
        assert webservice.ref == null
        assert webservice.alias == "wordpress3"
        assert webservice.database.database == "wordpress3"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.plugins.size() == 3
        assert webservice.plugins.contains("wp-typography")
        assert webservice.plugins.contains("link-indication")
        assert webservice.plugins.contains("broken-link-checker")
        assert webservice.themes.size() == 2
        assert webservice.themes.contains("picochic")
        assert webservice.themes.contains("tagebuch")
        assert webservice.forceSslLogin == true
        assert webservice.forceSslAdmin == true
        assert webservice.backupTarget.toString() == "file:///var/backups"
        assert webservice.cacheEnabled == true
        assert webservice.cachePlugin == "hyper-cache"

        domain = service.domains[d++]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof SslDomain)
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.id == null
        assert webservice.ref == "wordpress3"
        assert webservice.database == null
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.test2.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.multiSite == MultiSite.subdir
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.test3.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.multiSite == MultiSite.subdomain
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.testid.com"
        assert domain.address == "192.168.0.51"
        assert domain.id == "testid"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.id == "wordpress3"
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.testref.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.ref == "wordpress3"
        assert webservice.refDomain == "testid"
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.testold.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.prefix == "wordpressold"
        assert webservice.overrideMode == OverrideMode.no
        assert webservice.cacheEnabled == null

        domain = service.domains[d++]
        assert domain.name == "www.testupdate.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof WordpressService)
        assert webservice.name == "wordpress_4"
        assert webservice.prefix == "wordpressold"
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.cacheEnabled == null
    }
}
