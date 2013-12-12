package com.anrisoftware.sscontrol.httpd.statements.roundcube;

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
