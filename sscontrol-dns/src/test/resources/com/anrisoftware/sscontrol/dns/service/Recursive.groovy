/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service

dns {
    // adds upstream servers
    servers upstream: "8.8.8.8"

    // adds icann root servers
    servers root: "icann"

    // adds named root server
    server "example1.com", address: "127.0.0.2"

    // adds named root server
    server "example2.com", address: "127.0.0.3"

    // sets addresses who is allowed to perform DNS recursion
    acls "127.0.0.1"

    // sets addresses who is allowed to perform DNS recursion
    acls "192.168.0.1, 192.168.0.2"

}
