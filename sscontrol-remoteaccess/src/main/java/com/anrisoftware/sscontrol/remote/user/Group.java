/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Group {

    private final Service service;

    private final String name;

    private Integer gid;

    /**
     * @see GroupFactory#create(Service, Map)
     */
    @AssistedInject
    Group(@Assisted Service service, @Assisted String name) {
        this.service = service;
        this.name = name;
    }

    /**
     * @see GroupFactory#create(Service, Map)
     */
    @AssistedInject
    Group(GroupArgs aargs, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.name = aargs.name(service, args);
        if (aargs.haveGid(args)) {
            this.gid = aargs.gid(service, args);
        }
    }

    public Service getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public Integer getGid() {
        return gid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }
}
