/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav.linux;

import static com.anrisoftware.sscontrol.security.clamav.linux.Clamav_0_98_ConfigLogger._.deploy_spamassassin_config_debug;
import static com.anrisoftware.sscontrol.security.clamav.linux.Clamav_0_98_ConfigLogger._.deploy_spamassassin_config_info;
import static com.anrisoftware.sscontrol.security.clamav.linux.Clamav_0_98_ConfigLogger._.deploy_spamassassin_config_trace;
import static org.apache.commons.lang3.StringUtils.join;

import java.io.File;
import java.util.List;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Clamav_0_98_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Clamav_0_98_ConfigLogger extends AbstractLogger {

    enum _ {

        message("message"),

        deploy_spamassassin_config_trace(
                "Configuration file '{}' created for {}: \n>>>\n{}\n<<<"),

        deploy_spamassassin_config_debug(
                "Configuration file '{}' created for {}."),

        deploy_spamassassin_config_info(
                "Configuration file '{}' created for service '{}'.");

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
     * Sets the context of the logger to {@link Clamav_0_98_Config}.
     */
    public Clamav_0_98_ConfigLogger() {
        super(Clamav_0_98_Config.class);
    }

    void deploySpamassassinConfig(Clamav_0_98_Config config, File file,
            List<String> configs) {
        if (isTraceEnabled()) {
            trace(deploy_spamassassin_config_trace, file, config,
                    join(configs, '\n'));
        } else if (isDebugEnabled()) {
            debug(deploy_spamassassin_config_debug, file, config);
        } else {
            info(deploy_spamassassin_config_info, file, config.getServiceName());
        }
    }
}
