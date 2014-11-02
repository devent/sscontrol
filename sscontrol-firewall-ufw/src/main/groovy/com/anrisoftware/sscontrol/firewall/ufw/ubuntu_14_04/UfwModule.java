/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-firewall-ufw.
 *
 * sscontrol-firewall-ufw is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-firewall-ufw is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-firewall-ufw. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.firewall.ufw.ubuntu_14_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import groovy.lang.Script;

import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the UFW service for Ubuntu 12.04.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UfwModule extends AbstractModule {

	@Override
	protected void configure() {
		bindScripts();
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
	}

	private void bindScripts() {
		MapBinder<String, Script> binder;
		binder = newMapBinder(binder(), String.class, Script.class);
        binder.addBinding("ufw.ubuntu_12_04").to(UbuntuScript.class);
	}
}
