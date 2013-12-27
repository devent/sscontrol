package com.anrisoftware.sscontrol.remote.user;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.assistedinject.Assisted;

/**
 * Remove or local user SSH key.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Key {

    private static final String RESOURCE = "resource";

    private final Object service;

    private final URI resource;

    /**
     * @see KeyFactory#create(Object, Map)
     */
    @Inject
    Key(KeyArgs aargs, @Assisted Object service,
            @Assisted Map<String, Object> args) throws ServiceException {
        this.service = service;
        this.resource = aargs.keyResource(service, args);
    }

    public Object getService() {
        return service;
    }

    public URI getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(RESOURCE, resource).toString();
    }
}
