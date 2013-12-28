package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Factory to create local user group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface GroupFactory {

    /**
     * Creates local user group.
     * 
     * @param service
     *            the remote access {@link Service}.
     * 
     * @param name
     *            the group {@link String} name.
     * 
     * @return the {@link Group}.
     */
    Group create(Service service, String name);

    /**
     * Creates local user group.
     * 
     * @param service
     *            the remote access {@link Service}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Group}.
     */
    Group create(Service service, Map<String, Object> args);

}
