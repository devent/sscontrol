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
