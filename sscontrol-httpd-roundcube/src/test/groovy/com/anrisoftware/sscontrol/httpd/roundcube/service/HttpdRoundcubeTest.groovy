/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.roundcube.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.domain.SslDomain
import com.anrisoftware.sscontrol.httpd.roundcube.RoundcubeService
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode
import com.anrisoftware.sscontrol.testutils.resources.WebServiceTestEnvironment

/**
 * @see RoundcubeService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdRoundcubeTest extends WebServiceTestEnvironment {

    @Test
    void "roundcube"() {
        loader.loadService profile.resource, null, null
        def profile = registry.getService("profile")[0]
        loader.loadService roundcubeScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0

        Domain domain = service.domains[d++]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"

        RoundcubeService webservice = domain.services[0]
        assert webservice.name == "roundcube"
        assert webservice.id == "idroundcube"
        assert webservice.ref == null
        assert webservice.alias == "roundcube"
        assert webservice.prefix == null
        assert webservice.database.database == "roundcubedb"
        assert webservice.database.user == "userdb"
        assert webservice.database.password == "userpassdb"
        assert webservice.database.host == "localhost"
        assert webservice.database.driver == "mysql"
        assert webservice.debugLogging("level") == null
        assert webservice.mailServer.mail == "tls://%h"
        assert webservice.mailServer.user == "usersmtp"
        assert webservice.mailServer.password == "passwordsmtp"
        assert webservice.backupTarget.toString() == "file:///var/backups"
        assert webservice.imapServers["Default Server"] == "mail.example.com"
        assert webservice.imapServers["Webmail Server"] == "webmail.example.com"
        assert webservice.imapServer == null
        assert webservice.imapPort == null
        assert webservice.imapDomains["example.com"] == "mail.example.com"
        assert webservice.imapDomains["otherdomain.com"] == "othermail.example.com"

        domain = service.domains[d++]
        assert domain.name == "www.test1.com"
        assert domain.address == "192.168.0.51"
        assert (domain instanceof SslDomain)
        assert domain.domainUser.name == "www-data"
        assert domain.domainUser.group == "www-data"
        webservice = domain.services[0]
        assert (webservice instanceof RoundcubeService)
        assert webservice.name == "roundcube"
        assert webservice.id == null
        assert webservice.ref == "idroundcube"
        assert webservice.database == null
        assert webservice.mailServer == null
        assert webservice.backupTarget == null
        assert webservice.imapServers == null
        assert webservice.imapServer == null
        assert webservice.imapDomains == null

        domain = service.domains[d++]
        assert domain.name == "www.testone.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof RoundcubeService)
        assert webservice.name == "roundcube"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.alias == "roundcube"
        assert webservice.prefix == "roundcubeone"
        assert webservice.imapServer == "localhost"
        assert webservice.imapPort == 99
        assert webservice.imapDomain == "example.com"

        domain = service.domains[d++]
        assert domain.name == "www.testdebug.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof RoundcubeService)
        assert webservice.name == "roundcube"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.alias == "roundcube"
        assert webservice.prefix == "roundcubedebug"
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.debugLogging("level")["roundcube"] == 1

        domain = service.domains[d++]
        assert domain.name == "www.testold.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof RoundcubeService)
        assert webservice.name == "roundcube"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.alias == "roundcube"
        assert webservice.prefix == "roundcubeold"
        assert webservice.overrideMode == OverrideMode.no

        domain = service.domains[d++]
        assert domain.name == "www.testupdate.com"
        assert domain.address == "192.168.0.51"
        webservice = domain.services[0]
        assert (webservice instanceof RoundcubeService)
        assert webservice.name == "roundcube"
        assert webservice.id == null
        assert webservice.ref == null
        assert webservice.alias == "roundcube"
        assert webservice.prefix == "roundcubeold"
        assert webservice.overrideMode == OverrideMode.update
    }
}
