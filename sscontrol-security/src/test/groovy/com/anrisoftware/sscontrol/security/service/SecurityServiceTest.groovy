/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.security.service.SecurityResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging
import com.anrisoftware.sscontrol.security.banning.Backend
import com.anrisoftware.sscontrol.security.banning.Type
import com.anrisoftware.sscontrol.security.resources.SecurityTestUtil
import com.anrisoftware.sscontrol.security.services.Service

/**
 * Test the security service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SecurityServiceTest extends SecurityTestUtil {

    @Test
    void "security script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService securityScript.resource, profile

        SecurityService service = registry.getService("security")[0]

        DebugLogging user = service.debug
        assert user.level == 1

        def services = service.services
        assert services.size() == 3

        Service s = services[1]
        assert s.name == "ssh"
        assert s.notifyAddress == "root@localhost"
        assert s.ignoring.addresses.contains("192.0.0.1")
        assert s.banning.maxRetries == 3
        assert s.banning.banningTime.standardMinutes == 10
        assert s.banning.backend == Backend.polling
        assert s.banning.type == Type.deny

        s = services[2]
        assert s.name == "postfix"
        assert s.notifyAddress == "root@localhost"
        assert s.ignoring.addresses.contains("192.0.0.1")
        assert s.ignoring.addresses.contains("192.0.0.2")
        assert s.banning.maxRetries == 3
        assert s.banning.banningTime.standardMinutes == 10
        assert s.banning.backend == Backend.auto
        assert s.banning.type == Type.reject
    }
}
