package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Factory to create local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UserFactory {

    /**
     * Creates local user.
     * 
     * @param service
     *            the remote access {@link Service}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link User}.
     */
    User create(Service service, Map<String, Object> args);
}
