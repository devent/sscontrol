package com.anrisoftware.sscontrol.httpd.statements.proxy;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Parses arguments for {@link Proxy}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProxyArgs {

    public static final String ALIAS = "alias";

    public static final String SERVICE = "service";

    public static final String ADDRESS = "address";

    @Inject
    private ProxyLogger log;

    public boolean haveAddress(Map<String, Object> args) {
        return args.containsKey(ADDRESS);
    }

    public String address(Domain domain, Map<String, Object> args) {
        Object address = args.get(ADDRESS);
        log.checkAddress(domain, address);
        return address.toString();
    }

    public String service(Domain domain, Map<String, Object> args) {
        Object service = args.get(SERVICE);
        log.checkService(domain, service);
        return service.toString();
    }

    public boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    public String alias(Domain domain, Map<String, Object> args) {
        Object alias = args.get(ALIAS);
        log.checkAlias(domain, alias);
        return alias.toString();
    }
}
