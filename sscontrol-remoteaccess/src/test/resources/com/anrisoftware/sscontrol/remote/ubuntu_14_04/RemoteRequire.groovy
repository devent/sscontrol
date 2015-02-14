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
package com.anrisoftware.sscontrol.remote.ubuntu_14_04

remote {
    user "foo", password: "foopass", {
        home "$tmp/home/foo"
        passphrase "somepass"
        access key: Ubuntu_14_04_Resources.fooPub.resource
        require home, password, passphrase
    }
    user "bar", password: "barpass", uid: 99, {
        home "$tmp/home/bar"
        login "/bin/sh"
        comment "User Bar"
        group "bargroup", gid: 99
        passphrase "somepass"
        access key: Ubuntu_14_04_Resources.barPub.resource
        require home, login, comment, uid, group
    }
    user "baz", password: "bazpass", {
        passphrase "somepass"
        access key: Ubuntu_14_04_Resources.bazPub.resource
        require access
    }
}
