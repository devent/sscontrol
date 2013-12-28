/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall.
 *
 * sscontrol-firewall is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.service

import com.anrisoftware.sscontrol.remote.ubuntu_10_04.UbuntuResources

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
