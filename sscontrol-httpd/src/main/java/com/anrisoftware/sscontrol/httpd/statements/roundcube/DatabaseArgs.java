package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

/**
 * Parses arguments for Roundcube database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DatabaseArgs {

    public static final String HOST = "host";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String DATABASE = "database";

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

}
