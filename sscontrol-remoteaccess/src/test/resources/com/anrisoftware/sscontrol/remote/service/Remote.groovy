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

remote {
    user "bar", password: "barpass", uid: 2001
    user "baz", password: "bazpass", {
        passphrase "somepass"
        home "/var/home/baz"
        require password
    }
    user "foo", password: "foopass", {
        passphrase "somepass"
        access key: UbuntuResources.fooRemotePub.resource
        access key: UbuntuResources.barRemotePub.resource
    }
    user "foobar", password: "foopass", {
        group "foob", gid: 2001
        passphrase "somepass"
        access key: UbuntuResources.fooRemotePub.resource
        access key: UbuntuResources.barRemotePub.resource
    }
}
