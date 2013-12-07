package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import java.util.Map;

/**
 * Factory to create Roundcube database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface DatabaseFactory {

    /**
     * Creates Roundcube database credentials from the specified arguments.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Database}.
     */
    Database create(Map<String, Object> args);
}
