package com.anrisoftware.sscontrol.security.fail2ban;

import java.util.Map;

/**
 * Factory to create the jail.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface JailFactory {

    /**
     * Creates the jail.
     *
     * @param args
     *            the {@link Map} arguments.
     *
     * @return the {@link Jail}.
     */
    Jail create(Map<String, Object> args);
}
