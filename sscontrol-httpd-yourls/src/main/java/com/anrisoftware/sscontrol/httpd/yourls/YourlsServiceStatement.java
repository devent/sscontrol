/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.yourls;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * <i>Yourls</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum YourlsServiceStatement {

    DATABASE_KEY,

    USER_KEY,

    PASSWORD_KEY,

    HOST_KEY,

    PORT_KEY,

    PREFIX_KEY,

    DRIVER_KEY,

    OVERRIDE_KEY,

    MODE_KEY,

    BACKUP_KEY,

    TARGET_KEY,

    DEBUG_KEY,

    ACCESS_KEY,

    STATS_KEY,

    API_KEY,

    GMT_KEY,

    OFFSET_KEY,

    UNIQUE_KEY,

    URLS_KEY,

    CONVERT_KEY,

    RESERVED_KEY,

    SITE_KEY,

    LANGUAGE_KEY;

    @Override
    public String toString() {
        return split(lowerCase(name()), "_")[0];
    }

}
