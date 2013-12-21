package com.anrisoftware.sscontrol.httpd.statements.proxy;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Factory to create domain proxy.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ProxyFactory {

    /**
     * Creates the domain proxy.
     * 
     * @param domain
     *            the {@link Domain} of the proxy.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Proxy}.
     */
    Proxy create(Domain domain, Map<String, Object> args);
}
