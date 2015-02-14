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
package com.anrisoftware.sscontrol.remote.service

remote {
    debug "openssh", facility: "AUTH", level: 4
    bind all, port: 22
    user "bar", password: "barpass", uid: 2001
    user "baz", password: "bazpass", {
        group "bazgroup", gid: 2001
        home "/var/home/baz"
        login "/bin/sh"
        comment "Baz User"
        passphrase "somepass"
        require password, passphrase
    }
    user "foo", password: "foopass", {
        passphrase "somepass"
        access key: UbuntuResources.fooRemotePub.resource
        access key: UbuntuResources.barRemotePub.resource
    }
}
