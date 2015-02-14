/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.nginx_1_4;

import com.anrisoftware.sscontrol.httpd.service.HttpdService;

/**
 * Debug logging renderer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DebugLoggingRenderer {

    public String toString(HttpdService service) {
        Object storage = service.debugLogging("storage").get("error");
        int level = (Integer) service.debugLogging("level").get("error");
        StringBuilder builder = new StringBuilder();
        builder.append(storage);
        builder.append(" ").append(loggingLevel(level));
        return builder.toString();
    }

    private Object loggingLevel(int level) {
        switch (level) {
        case 0:
            return "emerg";
        case 1:
            return "alert";
        case 2:
            return "crit";
        case 3:
            return "error";
        case 4:
            return "warn";
        case 5:
            return "notice";
        case 6:
            return "info";
        case 7:
            return "debug";
        default:
            return "debug";
        }
    }
}
