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
package com.anrisoftware.sscontrol.httpd.authldap;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * File authentication service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthLdapServiceStatement {

    TYPE_KEY,

    SATISFY_KEY,

    AUTHORITATIVE_KEY,

    HOST_KEY,

    URL_KEY,

    CREDENTIALS_KEY,

    PASSWORD_KEY,

    REQUIRE_KEY,

    ATTRIBUTE_KEY,

    GROUPDN_KEY,

    DN_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
