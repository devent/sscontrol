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
package com.anrisoftware.sscontrol.httpd.staticservice;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * Static cache files service statements.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum StaticCacheStatement {

    EXPIRES_KEY,

    ACCESS_KEY,

    LOG_KEY,

    HEADERS_KEY,

    VALUE_KEY,

    INCLUDE_KEY,

    REFS_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
