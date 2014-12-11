/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import com.anrisoftware.sscontrol.dhclient.statements.StatementsModule;
import com.anrisoftware.sscontrol.dhclient.ubuntu_12_04.Ubuntu_12_04_Module;
import com.anrisoftware.sscontrol.dhclient.ubuntu_14_04.Ubuntu_14_04_Module;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;

/**
 * Binds the <i>Dhclient</i> service scripts.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DhclientModule extends AbstractModule {

	@Override
	protected void configure() {
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.ExecCommandModule());
		install(new StatementsModule());
        install(new Ubuntu_12_04_Module());
        install(new Ubuntu_14_04_Module());
	}
}
