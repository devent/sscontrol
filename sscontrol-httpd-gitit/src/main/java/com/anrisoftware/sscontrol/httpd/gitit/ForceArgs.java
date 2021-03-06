/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

import java.util.Map;

/**
 * Parses the arguments of force directives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ForceArgs {

    private static final String ADMIN = "admin";

    private static final String LOGIN = "login";

    boolean haveLogin(Map<String, Object> args) {
        return args.containsKey(LOGIN);
    }

    boolean login(Map<String, Object> args) {
        return (Boolean) args.get(LOGIN);
    }

    boolean haveAdmin(Map<String, Object> args) {
        return args.containsKey(ADMIN);
    }

    boolean admin(Map<String, Object> args) {
        return (Boolean) args.get(ADMIN);
    }

}
