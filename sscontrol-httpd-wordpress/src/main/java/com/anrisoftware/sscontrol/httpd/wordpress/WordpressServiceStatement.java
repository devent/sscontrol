/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress;

import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsEnumToString;

/**
 * <i>Wordpress</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum WordpressServiceStatement {

    PLUGIN_KEY,

    ENABLED_KEY,

    CACHE_KEY,

    SETUP_KEY,

    MULTISITE_KEY,

    ADMIN_KEY,

    LOGIN_KEY,

    HOST_KEY,

    PORT_KEY,

    PREFIX_KEY,

    CHARSET_KEY,

    COLLATE_KEY,

    SCHEMA_KEY,

    PASSWORD_KEY,

    USER_KEY,

    THEMES_KEY,

    PLUGINS_KEY,

    FORCE_KEY,

    DATABASE_KEY,

    TARGET_KEY,

    BACKUP_KEY,

    MODE_KEY,

    OVERRIDE_KEY,

    NAME,

    SERVICE_NAME,

    ALIAS_KEY,

    DEBUG_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }

}
