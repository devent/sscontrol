package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

/**
 * Factory to create Roundcube mail host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface HostFactory {

    /**
     * Creates Roundcube mail host for the specified service.
     * 
     * @param service
     *            the {@link Object} service.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Host}.
     */
    Host create(Object service, Map<String, Object> args);
}
