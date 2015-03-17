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
package com.anrisoftware.sscontrol.source.gitolite.core;

import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.installed_admin_key_debug;
import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.installed_admin_key_info;
import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.installed_gitolite_debug;
import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.installed_gitolite_info;
import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.upgraded_gitolite_debug;
import static com.anrisoftware.sscontrol.source.gitolite.core.Gitolite_3_ConfigLogger._.upgraded_gitolite_info;

import java.net.URI;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Gitolite_3_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Gitolite_3_ConfigLogger extends AbstractLogger {

    enum _ {

        installed_gitolite_debug("Installed gitolite to '{}' for {}: {}"),

        installed_gitolite_info("Installed gitolite to '{}' for service '{}'."),

        installed_admin_key_debug("Installed admin key '{}' for {}: {}"),

        installed_admin_key_info("Installed admin key '{}' for service '{}'."),

        upgraded_gitolite_debug("Upgraded gitolite to '{}' for {}: {}"),

        upgraded_gitolite_info("Upgraded gitolite to '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Gitolite_3_Config}.
     */
    public Gitolite_3_ConfigLogger() {
        super(Gitolite_3_Config.class);
    }

    void installedGitolite(Gitolite_3_Config config, ProcessTask task,
            String path) {
        if (isDebugEnabled()) {
            debug(installed_gitolite_debug, path, config, task);
        } else {
            info(installed_gitolite_info, path, config.getServiceName());
        }
    }

    void upgradedGitolite(Gitolite_3_Config config, ProcessTask task,
            String path) {
        if (isDebugEnabled()) {
            debug(upgraded_gitolite_debug, path, config, task);
        } else {
            info(upgraded_gitolite_info, path, config.getServiceName());
        }
    }

    void installedAdminKey(Gitolite_3_Config config, ProcessTask task, URI key) {
        if (isDebugEnabled()) {
            debug(installed_admin_key_debug, key, config, task);
        } else {
            info(installed_admin_key_info, key, config.getServiceName());
        }
    }

}
