package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube;

/**
 * Roundcube database configuration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RoundcubeDatabaseConfig {

    /**
     * Returns the database back-end name.
     * 
     * @return the database back-end {@link String} name.
     */
    String getDatabase();
}
