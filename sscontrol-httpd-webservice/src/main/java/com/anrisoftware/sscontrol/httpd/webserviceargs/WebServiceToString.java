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
package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.ALIAS;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.PREFIX;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REF;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REFDOMAIN;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Returns a string representation of a web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebServiceToString {

    private static final String NAME = "name";
    private static final String DOMAIN = "domain";

    /**
     * Returns a string representation of a web service.
     * 
     * @param service
     *            the {@link WebService}.
     * 
     * @return the {@link String} representation.
     */
    public String toString(WebService service) {
        ToStringBuilder builder;
        builder = new ToStringBuilder(this);
        builder.append(NAME, service.getName());
        builder.append(DOMAIN, service.getDomain());
        if (service.getAlias() != null) {
            builder.append(ALIAS, service.getAlias());
        }
        if (service.getPrefix() != null) {
            builder.append(PREFIX, service.getPrefix());
        }
        if (service.getRef() != null) {
            builder.append(REF, service.getRef());
        }
        if (service.getRefDomain() != null) {
            builder.append(REFDOMAIN, service.getRefDomain());
        }
        return builder.toString();
    }
}
