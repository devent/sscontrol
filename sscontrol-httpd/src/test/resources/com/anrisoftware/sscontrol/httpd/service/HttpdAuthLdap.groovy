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
import com.anrisoftware.sscontrol.httpd.auth.RequireValid
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType;

httpd {
    ssl_domain "test1.com", address: "192.168.0.50", {
        setup "auth-ldap", auth: "Private Directory", location: "/private", {
            type AuthType.basic, satisfy: SatisfyType.any, authoritative: no
            host "ldap://127.0.0.1:389", url: "o=deventorg,dc=ubuntutest,dc=com?cn"
            credentials "cn=admin,dc=ubuntutest,dc=com", password: "adminpass"
            require valid: RequireValid.user
            require group: "cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com"
            require attribute: [group: "uniqueMember"]
            require attribute: [dn: no]
        }
    }
}
