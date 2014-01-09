/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
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
