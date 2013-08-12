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
package com.anrisoftware.sscontrol.core.registry;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;

/**
 * Stores the services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class ServicesRegistryImpl implements ServicesRegistry {

	private final MultiValueMap services;

	ServicesRegistryImpl() {
		this.services = new MultiValueMap();
	}

	@Override
	public void addService(Service service) {
		services.put(service.getName(), service);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Service> getService(String name) {
		return unmodifiableList(new ArrayList<Service>(
				services.getCollection(name)));
	}

	@Override
	public Collection<Service> getAllServices() {
		List<Service> services = new ArrayList<Service>();
		for (String name : getServiceNames()) {
			services.addAll(getService(name));
		}
		return unmodifiableList(services);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getServiceNames() {
		return unmodifiableSet(services.keySet());
	}

	@Override
	public boolean haveService(String name) {
		return services.containsKey(name);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("services", services.keySet())
				.toString();
	}
}
