/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-fail2ban.
 *
 * sscontrol-security-fail2ban is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-fail2ban is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-fail2ban. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.linux;

import static com.anrisoftware.sscontrol.security.fail2ban.linux.Fail2ban_0_8_ConfigLogger._.firewall_script_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Fail2ban_0_8_Config}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Fail2ban_0_8_ConfigLogger extends AbstractLogger {

    enum _ {

        firewall_script_null("Fail2ban firewall script '{}' not found for {}.");

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
     * Sets the context of the logger to {@link Fail2ban_0_8_Config}.
     */
    public Fail2ban_0_8_ConfigLogger() {
        super(Fail2ban_0_8_Config.class);
    }

    void checkFirewallScript(Fail2ban_0_8_Config config, Object firewall,
            String name) {
        notNull(firewall, firewall_script_null.toString(), name, config);
    }
}
