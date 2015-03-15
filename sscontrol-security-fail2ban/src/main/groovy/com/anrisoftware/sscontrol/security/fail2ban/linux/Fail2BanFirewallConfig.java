/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
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

import com.anrisoftware.sscontrol.security.fail2ban.Fail2banService;
import com.anrisoftware.sscontrol.security.fail2ban.Jail;

/**
 * <i>Fail2ban</i> firewall configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Fail2BanFirewallConfig {

    /**
     * Deploys the configuration before the jails.
     *
     * @param service
     *            the {@link Fail2banService} service.
     */
    void beforeConfiguration(Fail2banService service);

    /**
     * Deploys the security configuration for the jail.
     *
     * @param service
     *            the {@link Fail2banService} service.
     *
     * @param jail
     *            the {@link Jail} jail.
     */
    void deployFirewallScript(Fail2banService service, Jail jail);

    /**
     * Sets the parent script with the properties.
     *
     * @param script
     *            the parent {@link Object} script.
     */
    void setScript(Object script);

    /**
     * Returns the parent script with the properties.
     *
     * @return the parent {@link Object} script.
     */
    Object getScript();

}
