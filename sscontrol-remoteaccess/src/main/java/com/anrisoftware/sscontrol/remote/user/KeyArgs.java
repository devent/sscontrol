/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Parses arguments for the user SSH/key.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class KeyArgs {

    private static final String RESOURCE = "key";

    @Inject
    private KeyArgsLogger log;

    URI keyResource(Object service, Map<String, Object> args)
            throws ServiceException {
        Object resource = args.get(RESOURCE);
        log.checkResource(resource, service);
        URI uri;
        if (resource instanceof URI) {
            uri = fromURI(resource);
        } else if (resource instanceof URL) {
            uri = fromURL(service, (URL) resource);
        } else {
            uri = fromFile(resource);
        }
        return uri;
    }

    private URI fromURL(Object service, URL resource) throws ServiceException {
        try {
            return resource.toURI();
        } catch (URISyntaxException e) {
            throw log.errorURISyntax(service, e, resource);
        }
    }

    private URI fromURI(Object resource) {
        URI uri = (URI) resource;
        if (!uri.isAbsolute()) {
            uri = fromFile(resource);
        }
        return uri;
    }

    private URI fromFile(Object resource) {
        return new File(resource.toString()).toURI();
    }
}
