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
package com.anrisoftware.sscontrol.security.fail2ban.ufw_ubuntu_12_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.security.fail2ban.linux.Fail2BanFirewallConfig;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Ufw Fail2ban Ubuntu 12.04</i> module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ufw_Ubuntu_12_04_Module extends AbstractModule {

    @Override
    protected void configure() {
        bindScripts();
    }

    private void bindScripts() {
        MapBinder<String, Fail2BanFirewallConfig> binder;
        binder = newMapBinder(binder(), String.class, Fail2BanFirewallConfig.class);
        binder.addBinding("ufw").to(Ufw_Ubuntu_Script.class);
    }
}
