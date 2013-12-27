package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Parses arguments for the local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserArgs {

    private static final String UID = "uid";

    private static final String GID = "gid";

    private static final String PASSWORD = "password";

    private static final String NAME = "name";

    @Inject
    private UserArgsLogger log;

    String name(Object service, Map<String, Object> args) {
        Object name = args.get(NAME);
        log.checkName(name, service);
        return name.toString();
    }

    String password(Object service, Map<String, Object> args) {
        Object password = args.get(PASSWORD);
        log.checkPassword(password, service);
        return password.toString();
    }

    boolean haveUid(Map<String, Object> args) {
        return args.containsKey(UID);
    }

    int uid(Service service, Map<String, Object> args) {
        Object uid = args.get(UID);
        log.checkUid(uid, service);
        return ((Number) uid).intValue();
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
