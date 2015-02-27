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
package com.anrisoftware.sscontrol.dns.service;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>DNS</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum DnsServiceStatement {

    ALIAS_KEY,

    ADDRESS_KEY,

    ADDRESSES_KEY,

    ROOT_KEY,

    UPSTREAM_KEY,

    ACLS_KEY,

    SERVER_KEY,

    SERVERS_KEY,

    GENERATE_KEY,

    SERIAL_KEY,

    DURATION_KEY,

    TTL_KEY,

    NAME_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
