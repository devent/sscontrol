/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.httpd.owncloud.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.owncloud.OwncloudService
import com.anrisoftware.sscontrol.httpd.service.HttpdService
import com.anrisoftware.sscontrol.testutils.resources.HttpdPreScriptTestEnvironment

/**
 * @see PiwikService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class HttpdOwncloudTest extends HttpdPreScriptTestEnvironment {

    @Test
    void "owncloud service"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService owncloudScript.resource, profile, preScript
        HttpdService service = registry.getService("httpd")[0]

        int d = 0
        Domain domain = service.domains[d++]
        assert domain.name == "test1.com"
        assert domain.address == "192.168.0.51"

        OwncloudService webservice = domain.services[0]
        assert webservice.name == "owncloud_7"
        assert webservice.id == "owncloudid"
        assert webservice.ref == "owncloudid"
        assert webservice.alias == "owncloud"
        assert webservice.prefix == "test1owncloud"
        assert webservice.debugLogging("level").size() == 2
        assert webservice.debugLogging("level")["php"] == 1
        assert webservice.debugLogging("level")["owncloud"] == 1
        assert webservice.overrideMode == OverrideMode.update
        assert webservice.backupTarget.toString() =~ /.*\/var\/backups/
        assert webservice.database.database == "owncloud"
        assert webservice.database.user == "user"
        assert webservice.database.password == "userpass"
        assert webservice.database.host == "localhost"
        assert webservice.database.port == 3306
        assert webservice.database.prefix == "owncloud_"
        assert webservice.database.adapter == "mysql"
    }
}
