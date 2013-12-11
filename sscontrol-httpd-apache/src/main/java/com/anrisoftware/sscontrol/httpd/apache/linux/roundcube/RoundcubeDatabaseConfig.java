package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube;

import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript;
import com.anrisoftware.sscontrol.httpd.statements.roundcube.RoundcubeService;

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

    /**
     * Sets the parent script with the properties.
     * 
     * @param script
     *            the {@link ApacheScript}.
     */
    void setScript(ApacheScript script);

    /**
     * Returns the parent script with the properties.
     * 
     * @return the {@link ApacheScript}.
     */
    ApacheScript getScript();

    /**
     * Setups the database configuration.
     * 
     * @param service
     *            the {@link RoundcubeService}.
     */
    void setupDatabase(RoundcubeService service);
}
