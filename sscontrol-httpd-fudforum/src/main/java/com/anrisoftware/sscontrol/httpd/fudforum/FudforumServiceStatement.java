/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>FUDForum</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum FudforumServiceStatement {

    DATABASE_KEY,

    USER_KEY,

    PASSWORD_KEY,

    HOST_KEY,

    PORT_KEY,

    PREFIX_KEY,

    TYPE_KEY,

    DRIVER_KEY,

    OVERRIDE_KEY,

    MODE_KEY,

    BACKUP_KEY,

    TARGET_KEY,

    DEBUG_KEY,

    TEMPLATE_KEY,

    LANGUAGE_KEY,

    ROOT_KEY,

    EMAIL_KEY,

    SITE_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }

}
