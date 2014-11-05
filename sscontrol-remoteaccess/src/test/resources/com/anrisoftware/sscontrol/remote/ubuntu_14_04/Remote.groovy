/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.ubuntu_12_04

remote {
    user "devent", password: "foopass", {
        passphrase "somepass"
        access key: Ubuntu_12_04_Resources.fooRemotePub.resource
        require password, passphrase, accesskeys
    }
    user "bar", password: "barpass", uid: 2001
    user "baz", password: "bazpass", {
        passphrase "somepass"
        home "/var/home/baz"
    }
    user "foo", password: "foopass", {
        passphrase "somepass"
        access key: Ubuntu_12_04_Resources.fooRemotePub.resource
        access key: Ubuntu_12_04_Resources.barRemotePub.resource
    }
    user "foobar", password: "foopass", {
        group "foob", gid: 2001
        passphrase "somepass"
        access key: Ubuntu_12_04_Resources.fooRemotePub.resource
        access key: Ubuntu_12_04_Resources.barRemotePub.resource
    }
    user "foobaz", password: "foopass", {
        passphrase "somepass"
    }
}
