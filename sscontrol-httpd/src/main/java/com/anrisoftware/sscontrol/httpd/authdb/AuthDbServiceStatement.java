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
package com.anrisoftware.sscontrol.httpd.authdb;

import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsEnumToString;

/**
 * Database authentication service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum AuthDbServiceStatement {

    DATABASE_KEY,

    USER_KEY,

    PASSWORD_KEY,

    HOST_KEY,

    PORT_KEY,

    SOCKET_KEY,

    CHARSET_KEY,

    DRIVER_KEY,

    ENCRYPTION_KEY,

    USERS_KEY,

    TABLE_KEY,

    FIELD_KEY,

    USER_NAME_KEY,

    ALLOW_KEY,

    EMPTY_PASSWORDS_KEY,

    TYPE_KEY,

    AUTHORITATIVE_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
