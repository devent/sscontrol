/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
 * Required user for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireUser {

    private static final String PASSWORD = "password";

    private static final String NAME = "name";

    private final String name;

    private final String password;

    private RequireUpdate updateMode;

    /**
     * @see RequireUserFactory#create(AbstractAuthService, Map)
     */
    @Inject
    RequireUser(RequireUserLogger log, @Assisted AbstractAuthService service,
            @Assisted Map<String, Object> args) {
        this.name = log.name(service, args);
        this.password = log.password(service, args);
        if (log.haveUpdate(args)) {
            this.updateMode = log.update(service, args);
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUpdateMode(RequireUpdate mode) {
        this.updateMode = mode;
    }

    public RequireUpdate getUpdateMode() {
        return updateMode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, name)
                .append(PASSWORD, password).toString();
    }
}
