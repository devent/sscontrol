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

import java.util.Map;

/**
 * Factory to create a binding for service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface BindingFactory {

	/**
	 * Creates a new binding for service.
	 * 
	 * @param args
	 *            the {@link Map} arguments for the binding.
	 * 
	 * @param array
	 *            additional arguments.
	 * 
	 * @return the {@link Binding}.
	 */
	Binding create(Map<String, Object> args, String... array);

	/**
	 * Creates a new binding for service.
	 * 
	 * @param address
	 *            the {@link BindingAddress} address.
	 * 
	 * @return the {@link Binding}.
	 */
	Binding create(BindingAddress address);
}
