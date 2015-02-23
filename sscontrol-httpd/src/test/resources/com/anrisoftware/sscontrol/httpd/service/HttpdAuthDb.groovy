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

import com.anrisoftware.sscontrol.httpd.auth.AuthType
import com.anrisoftware.sscontrol.httpd.auth.RequireUpdate
import com.anrisoftware.sscontrol.httpd.auth.RequireValid
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType

httpd {
	ssl_domain "test1.com", address: "192.168.0.50", {
		setup "auth-db", auth: "Private Directory", location: "/private", {
            database "authdb", user: "userdb", password: "userpassdb", host: "localhost", driver: "mysql", encryption: "PHP_MD5, Crypt"
            users table: "users"
            field userName: "username", password: "passwd"
            allow emptyPasswords: no
            require valid: RequireValid.user
            require user: "foo", password: "foopassword"
			require user: "bar", password: "barpassword", update: RequireUpdate.password
            require group: "foogroup"
            require group: "admin1", {
                user "adminfoo1", password: "adminfoopassword"
                user "adminbar1", password: "adminbarpassword"
            }
            require group: "admin2", update: RequireUpdate.rewrite, {
                user "adminfoo2", password: "adminfoopassword"
                user "adminbar2", password: "adminbarpassword"
            }
			require group: "admin3", update: RequireUpdate.append, {
				user "adminfoo3", password: "adminfoopassword"
				user "adminbar3", password: "adminbarpassword"
			}
		}
	}
}
