/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.Key
import com.anrisoftware.sscontrol.remote.user.User
import com.anrisoftware.sscontrol.remote.user.UserFactory

/**
 * Deploys authorized keys to local users.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AuthorizedKeysScript implements RemoteScript {

    @Inject
    private AuthorizedKeysScriptLogger log

    @Inject
    UserFactory userFactory

    LinuxScript script

    @Override
    void deployRemoteScript(RemoteService service) {
        deployAuthorizedKeys service.users
    }

    void deployAuthorizedKeys(List users) {
        users.each { User user ->
            def file = authorizedKeysFile user
            copyAuthorizedKeys user, file
        }
    }

    void copyAuthorizedKeys(User user, File file) {
        if (user.keys.size() == 0) {
            return
        }
        def log = log
        user.keys.each { Key key ->
            def res = key.resource.toURL()
            def str = IOUtils.toString res
            FileUtils.writeStringToFile file, str, charset, true
            log.deployAuthorizedKey script, key, user
        }
    }

    @Override
    void setScript(LinuxScript script) {
        this.script = script
    }

    @Override
    LinuxScript getScript() {
        script
    }

    def propertyMissing(String name) {
        script.getProperty name
    }

    def methodMissing(String name, def args) {
        script.invokeMethod name, args
    }
}
