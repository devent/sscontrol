/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.openssh.fail2ban.ufw_ubuntu_10_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.remote.openssh.fail2ban.linux.Fail2BanScript;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Installs the UFW firewall fail2ban script for Ubuntu 10.04.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UfwFail2BanUbuntu_10_04_Module extends AbstractModule {

    @Override
    protected void configure() {
        bindScripts();
    }

    private void bindScripts() {
        MapBinder<String, Fail2BanScript> binder;
        binder = newMapBinder(binder(), String.class, Fail2BanScript.class);
        binder.addBinding("fail2ban.ufw").to(Ubuntu_10_04_Script.class);
    }
}
