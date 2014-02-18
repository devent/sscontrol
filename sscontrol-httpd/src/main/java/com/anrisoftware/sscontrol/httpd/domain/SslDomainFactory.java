package com.anrisoftware.sscontrol.httpd.domain;

import java.util.Map;

/**
 * Factory to create a new SSL/TLS domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SslDomainFactory {

    /**
     * Creates the SSL/TLS domain with the specified name.
     * 
     * @param args
     *            the {@link Map} arguments of the domain.
     * 
     * @param name
     *            the {@link String} name of the domain.
     * 
     * @return the {@link DomainImpl}.
     */
    SslDomain create(Map<String, Object> args, String name);
}
