/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.fudforum.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.fudforum.DatabaseType
import com.anrisoftware.sscontrol.httpd.fudforum.FudforumService
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see FudforumService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdFudforumTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "fudforum service"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService fudforumScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        FudforumService webservice = domain.services[0]
        assert webservice.name == "fudforum"
        assert webservice.id == "fudforumid"
        assert webservice.ref == "fudforumid"
        assert webservice.alias == "fudforum"
        assert webservice.prefix == "test1comfudforum"
        assert webservice.debugLogging("level").size() == 1
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "fudforumdb"
        assert webservice.database.user == "fudforumuser"
        assert webservice.database.password == "fudforumpass"
        assert webservice.database.host == "localhost"
        assert webservice.database.port == 3306
        assert webservice.database.prefix == "fudforum_"
        assert webservice.database.type == DatabaseType.mysql
        assert webservice.rootLogin == "admin"
        assert webservice.rootPassword == "admin"
        assert webservice.rootEmail == "admin@server.com"
        assert webservice.site == "http://127.0.0.1:8080/forum/"
        assert webservice.language == "de"
        assert webservice.template == "default"
    }
}
