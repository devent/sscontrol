/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.service

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.remote.service.RemoteResources.*
import groovy.util.logging.Slf4j

import org.junit.Test

import com.anrisoftware.sscontrol.remote.resources.RemoteTestUtil
import com.anrisoftware.sscontrol.remote.user.Key
import com.anrisoftware.sscontrol.remote.user.User

/**
 * Test the remote access service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class RemoteServiceTest extends RemoteTestUtil {

    @Test
    void "remote access script"() {
        loader.loadService profile.resource, null
        def profile = registry.getService("profile")[0]
        loader.loadService remoteScript.resource, profile

        RemoteService service = registry.getService("remote")[0]
        assert service.users.size() == 4

        User user = service.users[0]
        assert user.name == "bar"
        assert user.password == "barpass"
        assert user.uid == 2001
        assert user.group.name == "bar"
        assert user.group.gid == null

        user = service.users[1]
        assert user.name == "baz"
        assert user.password == "bazpass"
        assert user.uid == null
        assert user.keys.size() == 0
        assert user.passphrase == "somepass"
        assert user.home == "/var/home/baz"
        assert user.group.name == "baz"
        assert user.group.gid == null

        user = service.users[2]
        assert user.name == "foo"
        assert user.password == "foopass"
        assert user.passphrase == "somepass"
        assert user.keys.size() == 2
        assert user.group.name == "foo"
        assert user.group.gid == null

        Key key = user.keys[0]
        assert key.resource.toString().endsWith("fooremote_pub.txt");

        user = service.users[3]
        assert user.name == "foobar"
        assert user.password == "foopass"
        assert user.passphrase == "somepass"
        assert user.keys.size() == 2
        assert user.group.name == "foob"
        assert user.group.gid == 2001
    }
}
