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

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Required valid for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireValid {

    private static final String VALID = "valid";

    private final AuthService service;

    private final RequireValidMode validMode;

    /**
     * @see RequireValidFactory#create(AuthService, Map)
     */
    @Inject
    RequireValid(RequireValidLogger log, @Assisted AuthService service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.validMode = log.valid(service, args);
    }

    public AuthService getService() {
        return service;
    }

    public RequireValidMode getValidMode() {
        return validMode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(VALID, validMode).toString();
    }
}
