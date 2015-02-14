/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.openssh.userkey.linux;

import static com.anrisoftware.sscontrol.remote.openssh.userkey.linux.UserKeyScriptLogger._.sshkey_created_debug;
import static com.anrisoftware.sscontrol.remote.openssh.userkey.linux.UserKeyScriptLogger._.sshkey_created_info;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.remote.user.User;

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

    void sshkeyCreated(LinuxScript script, User user, ProcessTask task) {
        if (isDebugEnabled()) {
            debug(sshkey_created_debug, user, script, task);
        } else {
            info(sshkey_created_info, user.getName(), script.getName());
        }
    }
}
