/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine;

import com.anrisoftware.sscontrol.core.groovy.StatementsEnumToString;

/**
 * <i>Redmine</i> service statement key.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
enum RedmineServiceStatement {

    ENCODING_KEY,

    PROVIDER_KEY,

    DEBUG_KEY,

    DATABASE_KEY,

    BACKEND_KEY,

    MODE_KEY,

    OVERRIDE_KEY,

    INSTALL_KEY,

    SCM_KEY,

    NAME_KEY,

    LANGUAGE_KEY,

    PASSWORD_KEY,

    SSL_KEY,

    START_TLS_AUTO_KEY,

    OPENSSL_VERIFY_MODE_KEY,

    USER_KEY,

    AUTH_KEY,

    DOMAIN_KEY,

    METHOD_KEY,

    PORT_KEY,

    HOST_KEY,

    MAIL_KEY,

    BACKUP_KEY,

    TARGET_KEY,

    TRACKING_KEY,

    SCRIPT_KEY;

    @Override
    public String toString() {
        return StatementsEnumToString.toString(this);
    }
}
