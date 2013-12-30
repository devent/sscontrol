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

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Parses arguments for the local user group.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class GroupArgs {

    private static final String GID = "gid";

    private static final String NAME = "name";

    @Inject
    private GroupArgsLogger log;

    String name(Object service, Map<String, Object> args) {
        Object name = args.get(NAME);
        log.checkName(name, service);
        return name.toString();
    }

    boolean haveGid(Map<String, Object> args) {
        return args.containsKey(GID);
    }

    int gid(Service service, Map<String, Object> args) {
        Object gid = args.get(GID);
        log.checkGid(gid, service);
        return ((Number) gid).intValue();
    }

}
