package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Factory to create user SSH/key.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface KeyFactory {

    /**
     * Creates user SSH/key.
     * 
     * @param service
     *            the remote access service.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Key}.
     */
    Key create(Object service, Map<String, Object> args)
            throws ServiceException;
}
