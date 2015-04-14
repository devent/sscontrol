/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.security.service.ServicesResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.core.api.ServiceLoader as SscontrolServiceLoader
import com.anrisoftware.sscontrol.core.api.ServicesRegistry
import com.anrisoftware.sscontrol.security.spamassassin.ClearType
import com.anrisoftware.sscontrol.security.spamassassin.MessageType
import com.anrisoftware.sscontrol.security.spamassassin.SpamassassinService

/**
 * @see SpamassassinService
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class SpamassassinTest extends SecurityTestUtil {

    @Test
    void "spamassassin script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService securityScript.resource, profile
        SecurityService service = registry.getService("security")[0]

        assert service.services.size() == 1
        int d = 0
        SpamassassinService sa = service.services[d++]
        assert sa.name == "spamassassin"
        assert sa.debugLogging("level")["log"] == 1
        assert sa.clearHeaders == ClearType.headers
        assert sa.rewriteHeaders.size() == 1
        assert sa.rewriteHeaders["subject"] == "*SPAM*"
        assert sa.addHeaders.size() == 3
        assert sa.addHeaders[0].type == MessageType.spam
        assert sa.addHeaders[0].name == "Flag"
        assert sa.addHeaders[0].text == "_YESNOCAPS_"
        assert sa.addHeaders[0].enabled == true
        assert sa.addHeaders[1].type == MessageType.all
        assert sa.addHeaders[2].type == MessageType.all
        assert sa.addHeaders[2].enabled == false
        assert sa.trustedNetworks.size() == 1
        assert sa.trustedNetworks.containsAll(["192.168.0.40"])
        assert sa.spamScore == 5.0
    }
}
