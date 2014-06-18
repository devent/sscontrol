/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webservice;

import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface WebService {

    /**
     * Returns the name of the service.
     * 
     * @return the service {@link String} name.
     */
    String getName();

    /**
     * Returns the identifier of the service.
     * 
     * @return the service {@link String} identifier.
     */
    String getId();

    /**
     * Returns the alias of the service.
     * 
     * @return the service {@link String} alias.
     */
    String getAlias();

    /**
     * Returns the prefix of the service.
     * 
     * @return the service {@link String} prefix.
     */
    String getPrefix();

    /**
     * Returns the reference of the service.
     * 
     * @return the service {@link String} reference.
     */
    String getRef();

    /**
     * Returns the domain for which the service is configured.
     * 
     * @return the {@link Domain} domain.
     */
    Domain getDomain();

    /**
     * Returns the domain reference of the service.
     * 
     * @return the domain {@link String} reference.
     */
    String getRefDomain();
}
