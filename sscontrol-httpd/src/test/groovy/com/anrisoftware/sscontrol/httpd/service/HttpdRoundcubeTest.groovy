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
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService

/**
 * @see RoundcubeService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdRoundcubeTest extends HttpdTestUtil {

    @Test
    void "roundcube"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService roundcubeScript.resource, profile
        HttpdServiceImpl service = registry.getService("httpd")[0]

        Domain domain = service.domains[2]
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        WebService webservice = domain.services[0]
        assert webservice.getClass() == RoundcubeService
        assert webservice.name == "roundcube"
        assert webservice.alias == "roundcube"
        assert webservice.database.database == "roundcube"
        assert webservice.database.provider == "mysql"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.hosts.size() == 5
        assert webservice.smtp.host == "localhost"
        assert webservice.smtp.user == "smtpuser"
        assert webservice.smtp.password == "smtppass"
    }
}
