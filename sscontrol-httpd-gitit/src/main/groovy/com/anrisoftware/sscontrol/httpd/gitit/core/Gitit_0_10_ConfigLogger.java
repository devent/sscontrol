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
package com.anrisoftware.sscontrol.httpd.gitit.core;

import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.default_config_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.install_packages_done_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_defaults_file_created_trace;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_info;
import static com.anrisoftware.sscontrol.httpd.gitit.core.Gitit_0_10_ConfigLogger._.service_file_created_trace;

import java.io.File;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Gitit_0_10_Config}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Gitit_0_10_ConfigLogger extends AbstractLogger {

    enum _ {

        install_packages_done_trace("Install packages {} done in {}, {}."),

        install_packages_done_debug("Install packages {} done in {}."),

        install_packages_done_info("Install packages {} done."),

        default_config_created_trace(
                "Default configuration '{}' created for {}: \n>>>\n{}<<<"),

        default_config_created_debug(
                "Default configuration '{}' created for {}."),

        default_config_created_info(
                "Default configuration '{}' created for service '{}'."),

        service_defaults_file_created_trace(
                "Service defaults configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_defaults_file_created_debug(
                "Service defaults configuration '{}' created for {}."),

        service_defaults_file_created_info(
                "Service defaults configuration '{}' created for service '{}'."),

        service_file_created_trace(
                "Service configuration '{}' created for {}: \n>>>\n{}<<<"),

        service_file_created_debug("Service configuration '{}' created for {}."),

        service_file_created_info(
                "Service configuration '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link Gitit_0_10_Config}.
     */
    public Gitit_0_10_ConfigLogger() {
        super(Gitit_0_10_Config.class);
    }

    void installCabalPackagesDone(Gitit_0_10_Config config, ProcessTask task,
            Object packages) {
        if (isTraceEnabled()) {
            trace(install_packages_done_trace, packages, config, task);
        } else if (isDebugEnabled()) {
            debug(install_packages_done_debug, packages, config);
        } else {
            info(install_packages_done_info, packages);
        }
    }

    void defaultConfigCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(default_config_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(default_config_created_debug, file, config);
        } else {
            info(default_config_created_info, file, config.getServiceName());
        }
    }

    void serviceDefaultsFileCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(service_defaults_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_defaults_file_created_debug, file, config);
        } else {
            info(service_defaults_file_created_info, file,
                    config.getServiceName());
        }
    }

    void serviceFileCreated(Gitit_0_10_Config config, File file,
            String configstr) {
        if (isTraceEnabled()) {
            trace(service_file_created_trace, file, config, configstr);
        } else if (isDebugEnabled()) {
            debug(service_file_created_debug, file, config);
        } else {
            info(service_file_created_info, file, config.getServiceName());
        }
    }
}
