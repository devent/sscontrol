package com.anrisoftware.sscontrol.remote.openssh.userkey.linux;

import static com.anrisoftware.sscontrol.remote.openssh.userkey.linux.UserKeyScriptLogger._.sshkey_created_debug;
import static com.anrisoftware.sscontrol.remote.openssh.userkey.linux.UserKeyScriptLogger._.sshkey_created_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.remote.user.User;
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker;

/**
 * Logging for {@link UserKeyScript}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UserKeyScriptLogger extends AbstractLogger {

    enum _ {

        sshkey_created_debug("SSH key created for {} in {}: {}."),

        sshkey_created_info("SSH key created for user '{}' in script '{}'.");

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
     * Sets the context of the logger to {@link UserKeyScript}.
     */
    public UserKeyScriptLogger() {
        super(UserKeyScript.class);
    }

    void sshkeyCreated(LinuxScript script, User user,
            ScriptCommandWorker worker) {
        if (isDebugEnabled()) {
            debug(sshkey_created_debug, user, script, worker);
        } else {
            info(sshkey_created_info, user.getName(), script.getName());
        }
    }
}
