/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>Roundcube</i> service statement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RoundcubeServiceStatement {

    PORT_KEY,

    DOMAIN_KEY,

    SERVER_KEY,

    MAIL_KEY,

    DRIVER_KEY,

    DEBUG_KEY,

    MODE_KEY,

    OVERRIDE_KEY,

    HOST_KEY,

    PASSWORD_KEY,

    USER_KEY,

    DATABASE_KEY,

    TARGET_KEY,

    BACKUP_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }

}
