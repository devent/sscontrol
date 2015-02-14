/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.service;

import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.hosts.host.HostModule;
import com.anrisoftware.sscontrol.hosts.utils.HostsUtilsModule;
import com.google.inject.AbstractModule;

/**
 * Hosts service module.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostsModule extends AbstractModule {

	@Override
	protected void configure() {
        install(new HostModule());
        install(new HostsUtilsModule());
        install(new ListModule());
	}
}
