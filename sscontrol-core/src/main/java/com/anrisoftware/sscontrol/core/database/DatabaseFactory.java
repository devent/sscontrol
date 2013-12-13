package com.anrisoftware.sscontrol.core.database;

import java.util.Map;

/**
 * Factory to create database credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DatabaseFactory {

    /**
     * Creates Roundcube database credentials from the specified arguments.
     * 
     * @param service
     *            the Roundcube service.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Database}.
     */
    Database create(Object service, Map<String, Object> args);
}
