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

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyService;

/**
 * Parses arguments for {@link ProxyService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainUserArgs {

    public static final String UID = "uid";

    public static final String GROUP = "group";

    public static final String USER = "user";

    public static final String GID = "gid";

    public static final String REF_DOMAIN = "refdomain";

    @Inject
    private DomainUserLogger log;

    public boolean haveUser(Map<String, Object> args) {
        return args.containsKey(USER);
    }

    public String user(Domain domain, Map<String, Object> args) {
        Object user = args.get(USER);
        log.checkUser(domain, user);
        return user.toString();
    }

    public boolean haveGroup(Map<String, Object> args) {
        return args.containsKey(USER);
    }

    public String group(Domain domain, Map<String, Object> args) {
        Object group = args.get(GROUP);
        log.checkGroup(domain, group);
        return group.toString();
    }

    public boolean haveUid(Map<String, Object> args) {
        return args.containsKey(UID);
    }

    public int uid(Domain domain, Map<String, Object> args) {
        Object uid = args.get(UID);
        log.checkUid(domain, uid);
        return (Integer) uid;
    }

    public boolean haveGid(Map<String, Object> args) {
        return args.containsKey(GID);
    }

    public int gid(Domain domain, Map<String, Object> args) {
        Object gid = args.get(GID);
        log.checkGid(domain, gid);
        return (Integer) gid;
    }

    public boolean haveRefDomain(Map<String, Object> args) {
        return args.containsKey(REF_DOMAIN);
    }

    public String refDomain(Domain domain, Map<String, Object> args) {
        Object ref = args.get(REF_DOMAIN);
        log.checkRefDomain(domain, ref);
        return ref.toString();
    }
}
