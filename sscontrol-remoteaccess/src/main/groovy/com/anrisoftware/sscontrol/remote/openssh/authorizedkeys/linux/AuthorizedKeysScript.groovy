/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import org.apache.commons.io.IOUtils

import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.remote.api.RemoteScript
import com.anrisoftware.sscontrol.remote.service.RemoteService
import com.anrisoftware.sscontrol.remote.user.Require
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
            removeOldAuthorizedKeys user, file
            copyAuthorizedKeys user, file
        }
    }

    void removeOldAuthorizedKeys(User user, File file) {
        if (!file.isFile()) {
            return
        }
        if (user.requires?.contains(Require.access)) {
            file.delete()
        }
    }

    void copyAuthorizedKeys(User user, File file) {
        if (!user.accessKeys) {
            return
        }
        def log = log
        def oldkeys = file.isFile() ? lineIterator(file, charset.name()) : []
        user.accessKeys.each { URI key ->
            def res = key.toURL()
            def str = IOUtils.toString res
            def found = oldkeys.find { it == str.trim() }
            if (!found) {
                writeStringToFile file, str, charset, true
                log.deployAuthorizedKey script, key, user
            }
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
