/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.database;

import java.util.Map;

import javax.inject.Inject;

/**
 * Parses arguments for database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DatabaseArgs {

    public static final String PROVIDER = "provider";
    public static final String HOST = "host";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String DATABASE = "database";

    @Inject
    private DatabaseLogger log;

    boolean haveDatabase(Map<String, Object> args) {
        return args.containsKey(DATABASE);
    }

    String database(Map<String, Object> args) {
        return args.get(DATABASE).toString();
    }

    boolean haveUser(Map<String, Object> args) {
        return args.containsKey(USER);
    }

    String user(Map<String, Object> args) {
        return args.get(USER).toString();
    }

    boolean havePassword(Map<String, Object> args) {
        return args.containsKey(PASSWORD);
    }

    String password(Map<String, Object> args) {
        return args.get(PASSWORD).toString();
    }

    boolean haveHost(Map<String, Object> args) {
        return args.containsKey(HOST);
    }

    String host(Map<String, Object> args) {
        return args.get(HOST).toString();
    }

    boolean haveProvider(Map<String, Object> args) {
        return args.containsKey(PROVIDER);
    }

    String provider(Object service, Map<String, Object> args) {
        Object provider = args.get(PROVIDER);
        log.checkProvider(service, provider);
        return provider.toString();
    }

}
