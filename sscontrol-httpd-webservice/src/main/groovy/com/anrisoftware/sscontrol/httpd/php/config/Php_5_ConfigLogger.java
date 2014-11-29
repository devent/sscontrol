/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.php.config;

import static com.anrisoftware.sscontrol.httpd.php.config.Php_5_ConfigLogger._.phpinit_deployed_debug;
import static com.anrisoftware.sscontrol.httpd.php.config.Php_5_ConfigLogger._.phpinit_deployed_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link Php_5_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Php_5_ConfigLogger extends AbstractLogger {

    enum _ {

        phpinit_deployed_debug("Deployed php.ini '{}' for {}"),

        phpinit_deployed_info("Deployed php.ini '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link Php_5_Config}.
     */
    public Php_5_ConfigLogger() {
        super(Php_5_Config.class);
    }

    void phpinitDeployed(Domain domain, File file) {
        if (isDebugEnabled()) {
            debug(phpinit_deployed_debug, file, domain);
        } else {
            info(phpinit_deployed_info, file, domain.getName());
        }
    }
}
