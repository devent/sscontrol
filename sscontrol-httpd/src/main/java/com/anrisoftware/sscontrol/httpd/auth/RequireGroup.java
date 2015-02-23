/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Required group for authentication.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RequireGroup {

    private static final String NAME_ARG = "name";

    private static final String USER = "user";

    private static final String NAME = NAME_ARG;

    private final List<RequireUser> users;

    private final String name;

    private final AuthService service;

    private final RequireGroupLogger log;

    @Inject
    private RequireUserFactory userFactory;

    private RequireUpdate updateMode;

    /**
     * @see RequireGroupFactory#create(AuthService, Map)
     */
    @Inject
    RequireGroup(RequireGroupLogger log, @Assisted AuthService service,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.service = service;
        this.name = log.name(service, args);
        this.users = new ArrayList<RequireUser>();
        if (log.haveUpdate(args)) {
            this.updateMode = log.update(service, args);
        }
    }

    public String getName() {
        return name;
    }

    public void user(Map<String, Object> args, String name) {
        args.put(USER, name);
        RequireUser user = userFactory.create(service, args);
        log.userAdded(this, service, user);
        users.add(user);
    }

    public List<RequireUser> getUsers() {
        return users;
    }

    public void setUpdateMode(RequireUpdate mode) {
        this.updateMode = mode;
    }

    public RequireUpdate getUpdateMode() {
        return updateMode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, name).toString();
    }
}
