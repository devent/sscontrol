/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.bindings;

import static com.anrisoftware.sscontrol.core.bindings.BindingLogger._.address_added;
import static com.anrisoftware.sscontrol.core.bindings.BindingLogger._.address_blank;
import static com.anrisoftware.sscontrol.core.bindings.BindingLogger._.address_null;
import static com.anrisoftware.sscontrol.core.bindings.BindingLogger._.port_null;
import static com.anrisoftware.sscontrol.core.bindings.BindingLogger._.port_number;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Binding}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BindingLogger extends AbstractLogger {

	enum _ {

        address_null("Bind address cannot be null for %s."),

        address_blank("Bind address cannot be blank for %s."),

        port_null("Port cannot be null for %s."),

        port_number("Port must be a number for %s."),

        address_added("Binding address {} added.");

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
	 * Create logger for {@link Binding}.
	 */
	BindingLogger() {
		super(Binding.class);
	}

    void checkAddress(Object service, Object object) {
        notNull(object, address_null.toString(), service);
        notBlank(object.toString(), address_blank.toString(), service);
	}

    void checkPort(Object service, Object object) {
        notNull(object, port_null.toString(), service);
        isInstanceOf(Integer.class, object, port_number.toString(), service);
    }

    void addressAdded(Address address) {
        debug(address_added, address);
    }
}
