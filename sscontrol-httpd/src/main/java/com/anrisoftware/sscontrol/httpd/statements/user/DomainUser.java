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
package com.anrisoftware.sscontrol.httpd.statements.user;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Domain user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainUser {

    private static final String GROUP = "group";

    private static final String GID = "gid";

    private static final String UID = "uid";

    private static final String USER = "user";

    private String name;

    private String group;

    private Integer uid;

    private Integer gid;

    @AssistedInject
    DomainUser() {
    }

    /**
     * @see DomainUserFactory#create(Domain, Map)
     */
    @AssistedInject
    DomainUser(DomainUserLogger logger, DomainUserArgs aargs,
            @Assisted Domain domain, @Assisted Map<String, Object> args) {
        setUser(aargs.user(domain, args));
        if (aargs.haveGroup(args)) {
            setGroup(aargs.group(domain, args));
        }
        if (aargs.haveUid(args)) {
            setUid(aargs.uid(domain, args));
        }
        if (aargs.haveGid(args)) {
            setGid(aargs.gid(domain, args));
        }
    }

    public void setUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public Integer getGid() {
        return gid;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(USER, name);
        builder = uid != null ? builder.append(UID, uid) : builder;
        builder = group != null ? builder.append(GROUP, group) : builder;
        builder = gid != null ? builder.append(GID, gid) : builder;
        return builder.toString();
    }
}
