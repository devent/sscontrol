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

import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.auth_name_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.auth_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.auth_set_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.auth_set_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.credentials_set_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.credentials_set_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.domain_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.domain_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.group_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.group_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.host_set_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.host_set_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.location_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.type_null;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.user_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.user_added_info;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.valid_added_debug;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger._.valid_added_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link AuthService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthServiceLogger extends AbstractLogger {

    private static final String SATISFY = "satisfy";
    private static final String PROVIDER = "provider";
    private static final String LOCATION = "location";
    private static final String AUTHORITATIVE = "authoritative";

    enum _ {

        auth_name_null("Authentication cannot be null or blank for %s."),

        location_null("Location cannot be null or blank for %s."),

        type_null("Authentication type cannot be null for %s."),

        group_added_debug("Required group {} added for {}."),

        group_added_info("Required group '{}' added for service '{}'."),

        user_added_debug("Required user {} added for {}."),

        user_added_info("Required user '{}' added for service '{}'."),

        domain_added_debug("Required domain {} added for {}."),

        domain_added_info("Required domain '{}' added for service '{}'."),

        auth_null("Authoritative flag cannot be null for %s."),

        auth_set_debug("Authoritative {} set for {}."),

        auth_set_info("Authoritative {} set for service '{}'."),

        host_set_debug("Authentication host {} set for {}."),

        host_set_info("Authentication host '{}' set for service '{}'."),

        credentials_set_debug("Authentication credentials {} set for {}."),

        credentials_set_info(
                "Authentication credentials '{}' set for service '{}'."),

        valid_added_debug("Required valid {} added for {}."),

        valid_added_info("Required valid {} added for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link AuthService}.
     */
    public AuthServiceLogger() {
        super(AuthService.class);
    }

    void checkAuthName(AuthService service, String name) {
        notBlank(name, auth_name_null.toString(), service);
    }

    boolean haveLocation(Map<String, Object> args) {
        return args.containsKey(LOCATION);
    }

    String location(AuthService service, Map<String, Object> args) {
        Object location = args.get(LOCATION);
        notNull(location, location_null.toString(), service);
        notBlank(location.toString(), location_null.toString(), service);
        return location.toString();
    }

    void checkType(AuthService service, AuthType type) {
        notNull(type, type_null.toString(), service);
    }

    boolean haveProvider(Map<String, Object> args) {
        return args.containsKey(PROVIDER);
    }

    AuthProvider provider(AuthService authService, Map<String, Object> args) {
        AuthProvider provider = (AuthProvider) args.get(PROVIDER);
        return provider;
    }

    boolean haveSatisfy(Map<String, Object> args) {
        return args.containsKey(SATISFY);
    }

    SatisfyType satisfy(AuthService authService, Map<String, Object> args) {
        SatisfyType type = (SatisfyType) args.get(SATISFY);
        return type;
    }

    void groupAdded(AuthService service, RequireGroup group) {
        if (isDebugEnabled()) {
            debug(group_added_debug, group, service);
        } else {
            info(group_added_info, group.getName(), service.getName());
        }
    }

    void userAdded(AuthService service, RequireUser user) {
        if (isDebugEnabled()) {
            debug(user_added_debug, user, service);
        } else {
            info(user_added_info, user.getName(), service.getName());
        }
    }

    void validAdded(AuthService service, RequireValid valid) {
        if (isDebugEnabled()) {
            debug(valid_added_debug, valid, service);
        } else {
            info(valid_added_info, valid.getValidMode(), service.getName());
        }
    }

    void requireDomainSet(AuthService service, RequireDomain domain) {
        if (isDebugEnabled()) {
            debug(domain_added_debug, domain, service);
        } else {
            info(domain_added_info, domain.getDomain(), service.getName());
        }
    }

    boolean haveAuth(Map<String, Object> args) {
        return args.containsKey(AUTHORITATIVE);
    }

    boolean auth(WebService service, Map<String, Object> args) {
        Object auth = args.get(AUTHORITATIVE);
        notNull(auth, auth_null.toString(), service);
        return YesNoFlag.valueOf(auth);
    }

    void authSet(AuthService service, boolean auth) {
        if (isDebugEnabled()) {
            debug(auth_set_debug, auth, service);
        } else {
            info(auth_set_info, auth, service.getName());
        }
    }

    void hostSet(AuthService service, AuthHost host) {
        if (isDebugEnabled()) {
            debug(host_set_debug, host, service);
        } else {
            info(host_set_info, host.getHost(), service.getName());
        }
    }

    void credentialsSet(AuthService service, AuthCredentials cred) {
        if (isDebugEnabled()) {
            debug(credentials_set_debug, cred, service);
        } else {
            info(credentials_set_info, cred.getName(), service.getName());
        }
    }
}
