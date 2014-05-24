/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.linux;

import static com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.linux.AuthorizedKeysScriptLogger._.authorized_key_debug;
import static com.anrisoftware.sscontrol.remote.openssh.authorizedkeys.linux.AuthorizedKeysScriptLogger._.authorized_key_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.remote.user.Key;
import com.anrisoftware.sscontrol.remote.user.User;

/**
 * Logging for {@link AuthorizedKeysScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthorizedKeysScriptLogger extends AbstractLogger {

    enum _ {

        authorized_key_debug("Deployed authorized key {} for {} in {}."),

        authorized_key_info("Deployed authorized key '{}' for user '{}' in {}.");

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
     * Sets the context of the logger to {@link AuthorizedKeysScript}.
     */
    public AuthorizedKeysScriptLogger() {
        super(AuthorizedKeysScript.class);
    }

    void deployAuthorizedKey(LinuxScript script, Key key, User user) {
        if (isDebugEnabled()) {
            debug(authorized_key_debug, key, user, script);
        } else {
            info(authorized_key_info, key.getResource(), user.getName(),
                    script.getName());
        }
    }
}
