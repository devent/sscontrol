/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.piwik.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.piwik.PiwikService
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see PiwikService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdPiwikTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "piwik service"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService piwikScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        PiwikService webservice = domain.services[0]
        assert webservice.name == "piwik"
        assert webservice.id == "piwikid"
        assert webservice.ref == "piwikid"
        assert webservice.alias == "piwik"
        assert webservice.prefix == "test2piwik"
        assert webservice.debugLogging("level").size() == 2
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.debugLogging("level")["piwik"] == 4
        assert webservice.debugLogging("file").size() == 1
        assert webservice.debugLogging("file")["piwik"] == "tmp/logs/piwik.log"
        assert webservice.debugLogging("writer").size() == 1
        assert webservice.debugLogging("writer")["piwik"] == "file"
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "piwik"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.database.port == 3306
        assert webservice.database.prefix == "piwik_"
        assert webservice.database.adapter == "PDO\\MYSQL"
        assert webservice.database.type == "InnoDB"
        assert webservice.database.schema == "Mysql"
    }
}
