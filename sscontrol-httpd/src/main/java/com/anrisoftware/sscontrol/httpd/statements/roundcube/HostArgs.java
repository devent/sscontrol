package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

import javax.inject.Inject;

/**
 * Parses arguments for Roundcube mail host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostArgs {

    public static final String DOMAIN = "domain";
    public static final String ALIAS = "alias";
    public static final String HOST = "host";

    @Inject
    private HostLogger log;

    String host(Object service, Map<String, Object> args) {
        Object hostname = args.get(HOST);
        log.checkHost(service, hostname);
        return hostname.toString();
    }

    boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    String alias(Map<String, Object> args) {
        return args.get(ALIAS).toString();
    }

    boolean haveDomain(Map<String, Object> args) {
        return args.containsKey(DOMAIN);
    }

    String domain(Map<String, Object> args) {
        return args.get(DOMAIN).toString();
    }

}
