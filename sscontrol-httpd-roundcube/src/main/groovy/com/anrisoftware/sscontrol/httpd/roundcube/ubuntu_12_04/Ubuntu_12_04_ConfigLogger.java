/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.ubuntu_12_04;

import static com.anrisoftware.sscontrol.httpd.roundcube.ubuntu_12_04.Ubuntu_12_04_ConfigLogger._.installed_packages_info;
import static com.anrisoftware.sscontrol.httpd.roundcube.ubuntu_12_04.Ubuntu_12_04_ConfigLogger._.installed_packages_trace;

import java.util.List;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.service.LinuxScript;

/**
 * Logging messages for {@link Ubuntu_12_04_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Ubuntu_12_04_ConfigLogger extends AbstractLogger {

    enum _ {

        installed_packages_trace("Installed packages '{}' for {}."),

        installed_packages_info("Installed packages '{}' for service '{}'.");

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
     * Creates a logger for {@link Ubuntu_12_04_Config}.
     */
    public Ubuntu_12_04_ConfigLogger() {
        super(Ubuntu_12_04_Config.class);
    }

    void installedPackages(LinuxScript script, List<?> packages) {
        if (isDebugEnabled()) {
            debug(installed_packages_trace, packages, script);
        } else {
            info(installed_packages_info, packages, script.getName());
        }
    }
}
