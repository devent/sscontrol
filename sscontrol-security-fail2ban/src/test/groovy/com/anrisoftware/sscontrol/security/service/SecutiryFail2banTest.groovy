/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.security.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.security.fail2ban.Backend
import com.anrisoftware.sscontrol.security.fail2ban.Fail2banService
import com.anrisoftware.sscontrol.security.fail2ban.Jail
import com.anrisoftware.sscontrol.security.fail2ban.Type

/**
 * @see Fail2banService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SecutiryFail2banTest extends SecurityTestUtil {

    @Test
    void "fail2ban script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService securityScript.resource, profile
        SecurityService service = registry.getService("security")[0]

        assert service.services.size() == 1
        int d = 0
        Fail2banService fail2ban = service.services[d++]
        assert fail2ban.name == "fail2ban"
        assert fail2ban.debugLogging("level")["service"] == 1
        assert fail2ban.jails.size() == 4

        Jail jail = fail2ban.jails[0]
        assert jail.service == "apache"
        assert jail.notify == null

        jail = fail2ban.jails[1]
        assert jail.service == "ssh"
        assert jail.notify == "root@localhost"
        assert jail.ignoreAddresses.containsAll(["192.0.0.1"])
        assert jail.banningRetries == 3
        assert jail.banningTime.getStandardMinutes() == 10
        assert jail.banningBackend == Backend.polling
        assert jail.banningType == Type.deny
        assert jail.banningApp == "OpenSSH"

        jail = fail2ban.jails[2]
        assert jail.service == "ssh-ddos"
        assert jail.notify == "root@localhost"

        jail = fail2ban.jails[3]
        assert jail.service == "postfix"
        assert jail.notify == "root@localhost"
        assert jail.ignoreAddresses.containsAll(["192.0.0.1", "192.0.0.2"])
    }
}
