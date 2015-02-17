/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>ownCloud</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum OwncloudServiceStatement {

    DATABASE_KEY,

    USER_KEY,

    PASSWORD_KEY,

    HOST_KEY,

    PORT_KEY,

    PREFIX_KEY,

    ADAPTER_KEY,

    OVERRIDE_KEY,

    MODE_KEY,

    BACKUP_KEY,

    TARGET_KEY,

    DEBUG_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }

}
