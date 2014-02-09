/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Require group attribute.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class GroupAttribute {

    private static final String ATTRIBUTES = "attributes";

    private static final String NAME = "name";

    private final AuthService service;

    private final String name;

    private final Map<String, Object> attributes;

    /**
     * @see RequireCredentialsFactory#create(AuthService, Map)
     */
    @Inject
    GroupAttribute(GroupAttributeLogger log, @Assisted AuthService service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.name = log.name(service, args);
        this.attributes = new HashMap<String, Object>(args);
    }

    public AuthService getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(NAME, name);
        builder.append(ATTRIBUTES, attributes);
        return builder.toString();
    }

}
