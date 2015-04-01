/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

httpd {
	ssl_domain "test1.com", address: "192.168.0.50", {
		setup "auth-file", auth: "Private Directory", location: "/private", {

            type AuthType.digest, satisfy: SatisfyType.any

            group {
                user "foo", password: "foopass", update: UpdateMode.password
                user "bar", password: "barpass"
            }

            group "foogroup", {
                user "foo", password: "foopass", update: UpdateMode.password
                user "bar", password: "barpass"
            }

            group "foogroupappend", update: UpdateMode.append, {
                user "foo", password: "foopass"
                user "bar", password: "barpass"
            }

            group "foogrouprewrite", update: UpdateMode.rewrite, {
                user "foo", password: "foopass"
                user "bar", password: "barpass"
            }

            group {
                require valid: RequireValid.user
                user "foo", password: "foopass"
                user "bar", password: "barpass"
            }

            group "foolimit", {
                require except: "GET, OPTIONS"
                user "foo", password: "foopass"
                user "bar", password: "barpass"
            }
		}
	}
}
