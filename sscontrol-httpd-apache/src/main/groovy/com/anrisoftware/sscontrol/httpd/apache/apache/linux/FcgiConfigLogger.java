/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.link_phpconfig_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.link_phpconfig_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.scripts_directory_created_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.scripts_directory_created_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.starter_script_deployed_debug;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.starter_script_deployed_info;
import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.FcgiConfigLogger._.starter_script_deployed_trace;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging for {@link FcgiConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FcgiConfigLogger extends AbstractLogger {

    enum _ {

        scripts_directory_created_debug(
                "Scripts directory '{}' created for {}."),

        scripts_directory_created_info(
                "Scripts directory '{}' created for domain '{}'."),

        starter_script_deployed_trace(
                "Starter script '{}' deployed for {}: \n>>>{}\n<<<"),

        starter_script_deployed_debug("Starter script '{}' deployed for {}."),

        starter_script_deployed_info(
                "Starter script '{}' deployed for domain '{}'."),

        link_phpconfig_debug("PHP configuration linked '{}'->'{}' for {}."),

        link_phpconfig_info(
                "PHP configuration linked '{}'->'{}' for domain '{}'.");

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
     * Sets the context of the logger to {@link FcgiConfig}.
     */
    public FcgiConfigLogger() {
        super(FcgiConfig.class);
    }

    void scriptsDirectoryCreated(Domain domain, File dir) {
        if (isDebugEnabled()) {
            debug(scripts_directory_created_debug, dir, domain);
        } else {
            info(scripts_directory_created_info, dir, domain.getName());
        }
    }

    void starterScriptDeployed(Domain domain, File file, String string) {
        if (isTraceEnabled()) {
            trace(starter_script_deployed_trace, file, domain, string);
        } else if (isDebugEnabled()) {
            debug(starter_script_deployed_debug, file, domain);
        } else {
            info(starter_script_deployed_info, file, domain.getName());
        }
    }

    void linkPhpconf(Domain domain, File file, File target) {
        if (isDebugEnabled()) {
            debug(link_phpconfig_debug, file, target, domain);
        } else {
            info(link_phpconfig_info, file, target, domain.getName());
        }
    }
}
