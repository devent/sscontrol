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
package com.anrisoftware.sscontrol.httpd.gitit;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Force directives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Force {

    private static final String ADMIN = "admin";

    private static final String LOGIN = "login";

    private boolean login;

    private boolean admin;

    /**
     * @see ForceFactory#create(Map)
     */
    @Inject
    Force(ForceArgs aargs, @Assisted Map<String, Object> args) {
        if (aargs.haveLogin(args)) {
            this.login = aargs.login(args);
        }
        if (aargs.haveAdmin(args)) {
            this.admin = aargs.admin(args);
        }
    }

    public boolean isLogin() {
        return login;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(LOGIN, login)
                .append(ADMIN, admin).toString();
    }
}
