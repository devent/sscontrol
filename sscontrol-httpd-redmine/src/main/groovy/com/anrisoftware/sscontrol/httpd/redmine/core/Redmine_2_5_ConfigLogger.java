/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.core;

import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_trace;

import java.io.File;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Redmine_2_5_Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Redmine_2_5_ConfigLogger extends AbstractLogger {

    enum _ {

        install_packages_done_trace("Install packages {} done in {}, {}."),

        install_packages_done_debug("Install packages {} done in {}."),

        install_packages_done_info("Install packages {} done."),

        default_config_created_trace(
                "Default configuration '{}' created for {}: \n>>>\n{}<<<"),

        default_config_created_debug(
                "Default configuration '{}' created for {}."),

        default_config_created_info(
                "Default configuration '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link Redmine_2_5_Config}.
     */
    public Redmine_2_5_ConfigLogger() {
        super(Redmine_2_5_Config.class);
    }

    void installCabalPackagesDone(Redmine_2_5_Config config, ProcessTask task,
            Object packages) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, packages, config, task);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, packages, config);
        } else {
            info(install_packages_done_info, packages);
        }
    }

    void defaultConfigCreated(Redmine_2_5_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(default_config_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(default_config_created_debug, file, config);
        } else {
            info(default_config_created_info, file, config.getServiceName());
        }
    }

}
