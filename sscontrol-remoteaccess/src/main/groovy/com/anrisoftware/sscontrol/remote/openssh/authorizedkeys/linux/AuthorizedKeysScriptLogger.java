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
