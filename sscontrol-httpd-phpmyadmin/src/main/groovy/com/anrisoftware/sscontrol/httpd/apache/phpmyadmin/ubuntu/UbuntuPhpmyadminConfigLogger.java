/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu;

import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.domain_config_created_debug;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.domain_config_created_info;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.domain_config_created_trace;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.import_tables_debug;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.import_tables_info;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.import_tables_trace;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.reconfigure_service_debug;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.reconfigure_service_info;
import static com.anrisoftware.sscontrol.httpd.apache.phpmyadmin.ubuntu.UbuntuPhpmyadminConfigLogger._.reconfigure_service_trace;

import java.io.File;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging messages for {@link UbuntuPhpmyadminConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class UbuntuPhpmyadminConfigLogger extends AbstractLogger {

    enum _ {

        reconfigure_service_trace("Reconfigure phpmyadmin for {}, {}."),

        reconfigure_service_debug("Reconfigure phpmyadmin for {}."),

        reconfigure_service_info("Reconfigure phpmyadmin for service '{}'."),

        import_tables_trace("Import tables from '{}' for {}: {}."),

        import_tables_debug("Import tables from '{}' for {}."),

        import_tables_info("Import tables from '{}' for service '{}'."),

        domain_config_created_trace(
                "Configuration created for {} for {}: \n>>>\n{}<<<"),

        domain_config_created_debug("Configuration created for {} for {}."),

        domain_config_created_info(
                "Configuration created for domain '{}' for service '{}'.");

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
     * Creates a logger for {@link UbuntuPhpmyadminConfig}.
     */
    public UbuntuPhpmyadminConfigLogger() {
        super(UbuntuPhpmyadminConfig.class);
    }

    void domainConfigCreated(LinuxScript script, Domain domain, String config) {
        if (isTraceEnabled()) {
            trace(domain_config_created_trace, domain, script, config);
        } else if (isDebugEnabled()) {
            debug(domain_config_created_debug, domain, script);
        } else {
            info(domain_config_created_info, domain.getName(), script.getName());
        }
    }

    void reconfigureService(LinuxScript script, ProcessTask task) {
        if (isTraceEnabled()) {
            trace(reconfigure_service_trace, script, task);
        } else if (isDebugEnabled()) {
            debug(reconfigure_service_debug, script);
        } else {
            info(reconfigure_service_info, script.getName());
        }
    }

    void importTables(LinuxScript script, ProcessTask task, File file) {
        if (isTraceEnabled()) {
            trace(import_tables_trace, file, script, task);
        } else if (isDebugEnabled()) {
            debug(import_tables_debug, file, script);
        } else {
            info(import_tables_info, file, script.getName());
        }
    }
}
