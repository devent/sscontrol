/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.util.Map;

import javax.inject.Inject;

/**
 * Parses arguments for the SMTP server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SmtpServerArgs {

    public static final String USER = "user";

    public static final String HOST = "host";

    public static final String PASSWORD = "password";

    @Inject
    private SmtpServerLogger log;

    String host(Object service, Map<String, Object> args) {
        Object host = args.get(HOST);
        log.checkHost(service, host);
        return host.toString();
    }

    boolean haveUser(Map<String, Object> args) {
        return args.containsKey(USER);
    }

    String user(Object service, Map<String, Object> args) {
        Object user = args.get(USER);
        log.checkUser(service, user);
        return user.toString();
    }

    boolean havePassword(Map<String, Object> args) {
        return args.containsKey(PASSWORD);
    }

    String password(Object service, Map<String, Object> args) {
        Object password = args.get(PASSWORD);
        log.checkPassword(service, password);
        return password.toString();
    }

}
