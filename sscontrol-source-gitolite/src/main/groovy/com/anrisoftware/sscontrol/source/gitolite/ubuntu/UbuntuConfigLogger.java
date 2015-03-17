/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite.ubuntu;

import static com.anrisoftware.sscontrol.source.gitolite.ubuntu.UbuntuConfigLogger._.created_gitolite_user_debug;
import static com.anrisoftware.sscontrol.source.gitolite.ubuntu.UbuntuConfigLogger._.created_gitolite_user_info;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.source.gitolite.GitoliteService;

/**
 * Logging for {@link UbuntuConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfigLogger extends AbstractLogger {

    enum _ {

        created_gitolite_user_debug("Gitolite local user {} created for {}."),

        created_gitolite_user_info(
                "Gitolite local user '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link UbuntuConfig}.
     */
    public UbuntuConfigLogger() {
        super(UbuntuConfig.class);
    }

    void createdGitoliteUser(UbuntuConfig config, GitoliteService service) {
        if (isDebugEnabled()) {
            debug(created_gitolite_user_debug, service.getUser(), config);
        } else {
            info(created_gitolite_user_info, service.getUser().get("user"),
                    config.getServiceName());
        }
    }
}
