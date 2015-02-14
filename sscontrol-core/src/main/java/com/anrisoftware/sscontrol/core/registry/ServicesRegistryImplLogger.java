/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.registry;

import static com.anrisoftware.sscontrol.core.registry.ServicesRegistryImplLogger._.service_added_debug;
import static com.anrisoftware.sscontrol.core.registry.ServicesRegistryImplLogger._.service_added_info;
import static com.anrisoftware.sscontrol.core.registry.ServicesRegistryImplLogger._.service_not_found;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Collection;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Logging messages for {@link ServicesRegistryImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ServicesRegistryImplLogger extends AbstractLogger {

	enum _ {

		service_added_debug("Service {} added."),

		service_added_info("Service '{}' added."),

		service_not_found("Service '{}' not found.");

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
	 * Creates a logger for {@link ServicesRegistryImpl}.
	 */
	public ServicesRegistryImplLogger() {
		super(ServicesRegistryImpl.class);
	}

	void serviceAdded(String name, Service service) {
		if (isDebugEnabled()) {
			debug(service_added_debug, service);
		} else {
			info(service_added_info, name);
		}
	}

	void checkService(String name, Collection<?> list) {
		notNull(list, service_not_found.toString(), name);
		isTrue(list.size() > 0, service_not_found.toString(), name);
	}

}
