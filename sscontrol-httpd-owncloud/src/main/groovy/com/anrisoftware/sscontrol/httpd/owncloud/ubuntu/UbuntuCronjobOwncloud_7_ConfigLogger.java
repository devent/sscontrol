/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.ubuntu;

import static com.anrisoftware.sscontrol.httpd.owncloud.ubuntu.UbuntuCronjobOwncloud_7_ConfigLogger._.deploy_cronjob_debug;
import static com.anrisoftware.sscontrol.httpd.owncloud.ubuntu.UbuntuCronjobOwncloud_7_ConfigLogger._.deploy_cronjob_info;

import java.io.File;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link UbuntuCronjobOwncloud_7_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuCronjobOwncloud_7_ConfigLogger extends AbstractLogger {

    enum _ {

        deploy_cronjob_debug("Deployed cronjob file '{}' for {}."),

        deploy_cronjob_info("Deployed cronjob file '{}' for service '{}'.");

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
     * Sets the context of the logger to {@link UbuntuCronjobOwncloud_7_Config}.
     */
    public UbuntuCronjobOwncloud_7_ConfigLogger() {
        super(UbuntuCronjobOwncloud_7_Config.class);
    }

    void deployCronjob(UbuntuCronjobOwncloud_7_Config config, File file) {
        if (isDebugEnabled()) {
            debug(deploy_cronjob_debug, file, config);
        } else {
            info(deploy_cronjob_info, file, config.getServiceName());
        }
    }
}
