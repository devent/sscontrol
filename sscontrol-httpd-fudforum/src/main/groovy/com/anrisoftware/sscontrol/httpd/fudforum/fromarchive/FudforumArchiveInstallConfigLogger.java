/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.fromarchive;

import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_installed_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_installed_info;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_installed_trace;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_upgraded_debug;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_upgraded_info;
import static com.anrisoftware.sscontrol.httpd.fudforum.fromarchive.FudforumArchiveInstallConfigLogger._.service_upgraded_trace;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging messages for {@link FudforumArchiveInstallConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class FudforumArchiveInstallConfigLogger extends AbstractLogger {

    enum _ {

        service_installed_trace("Service installed for {}: {}"),

        service_installed_debug("Service installed for domain '{}' for {}."),

        service_installed_info(
                "Service installed for domain '{}' for service '{}'."),

        service_upgraded_trace("Service upgraded for {}: {}"),

        service_upgraded_debug("Service upgraded for domain '{}' for {}."),

        service_upgraded_info(
                "Service upgraded for domain '{}' for service '{}'.");

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
     * Creates a logger for {@link FudforumArchiveInstallConfig}.
     */
    public FudforumArchiveInstallConfigLogger() {
        super(FudforumArchiveInstallConfig.class);
    }

    void serviceInstalled(FudforumArchiveInstallConfig config, Domain domain,
            ProcessTask task) {
        if (isTraceEnabled()) {
            trace(service_installed_trace, config, task);
        } else if (isDebugEnabled()) {
            debug(service_installed_debug, domain.getName(), config);
        } else {
            info(service_installed_info, domain.getName(),
                    config.getServiceName());
        }
    }

    void serviceUpgraded(FudforumArchiveInstallConfig config, Domain domain,
            ProcessTask task) {
        if (isTraceEnabled()) {
            trace(service_upgraded_trace, config, task);
        } else if (isDebugEnabled()) {
            debug(service_upgraded_debug, domain.getName(), config);
        } else {
            info(service_upgraded_info, domain.getName(),
                    config.getServiceName());
        }
    }

}
