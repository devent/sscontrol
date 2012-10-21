/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;

/**
 * Stores the services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ServicesRegistryImpl implements ServicesRegistry {

	/**
	 * 
	 * @version 0.1
	 */
	private static final long serialVersionUID = -5209241548874285959L;

	private final MultiValueMap services;

	ServicesRegistryImpl() {
		this.services = new MultiValueMap();
	}

	@Override
	public void addService(Service service) {
		services.put(service.getName(), service);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Service> getService(String name) {
		return UnmodifiableList.decorate(new ArrayList<Object>(services
				.getCollection(name)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getServiceNames() {
		return UnmodifiableSet.decorate(services.keySet());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("services", services.keySet())
				.toString();
	}
}
