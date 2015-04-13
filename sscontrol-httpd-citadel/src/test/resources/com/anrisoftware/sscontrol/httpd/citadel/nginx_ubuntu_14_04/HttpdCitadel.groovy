/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.nginx_ubuntu_14_04

import com.anrisoftware.sscontrol.httpd.citadel.AuthMethod
import com.anrisoftware.sscontrol.httpd.citadel.ubuntu_14_04.UbuntuResources

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "citadel", alias: "/", {
            bind "0.0.0.0", port: 504
            auth method: AuthMethod.selfContained
            admin "admin", password: "adminpass"
            certificate ca: UbuntuResources.certCa.resource, file: UbuntuResources.certCrt.resource, key: UbuntuResources.certKey.resource
        }
    }
}
