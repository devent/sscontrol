/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.fail2ban.linux;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.security.services.Service;

/**
 * Fail2ban firewall.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface Fail2BanFirewall {

    /**
     * Called before the Firewall configuration.
     */
    void beforeConfiguration();

    /**
     * Deploys the security script.
     * 
     * @param service
     *            the {@link Service}.
     */
    void deployFirewallScript(Service service);

    /**
     * Sets the parent script with the properties.
     * 
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(LinuxScript script);

    /**
     * Returns the parent script with the properties.
     * 
     * @return the {@link LinuxScript}.
     */
    LinuxScript getScript();

}
