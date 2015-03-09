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
package com.anrisoftware.sscontrol.httpd.roundcube.core;

import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.error_sample_config_file;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.error_sample_config_file_message;
import static com.anrisoftware.sscontrol.httpd.roundcube.core.Roundcube_1_ConfigLogger._.the_file;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link Roundcube_1_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Roundcube_1_ConfigLogger extends AbstractLogger {

    enum _ {

        message("message"),

        error_sample_config_file("Roundcube sample config file does not exist"),

        error_sample_config_file_message(
                "Roundcube sample config file '{}' does not exist for service '{}'."),

        the_file("file");

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
     * Sets the context of the logger to {@link Roundcube_1_Config}.
     */
    public Roundcube_1_ConfigLogger() {
        super(Roundcube_1_Config.class);
    }

    void checkRoundcubeSampleConfigFile(Roundcube_1_Config config, File file)
            throws ServiceException {
        if (!file.exists()) {
            throw logException(
                    new ServiceException(error_sample_config_file).add(
                            the_file, file), error_sample_config_file_message,
                    file, config.getServiceName());
        }
    }
}
