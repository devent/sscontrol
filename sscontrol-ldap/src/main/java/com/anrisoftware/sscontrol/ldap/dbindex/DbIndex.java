/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.dbindex;

import static java.util.Collections.unmodifiableSet;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Database index with multiple indices and types.
 * 
 * @see IndexType
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DbIndex {

	private final Set<String> names;

	private final Set<IndexType> types;

	/**
	 * @see DbIndexFactory#create(Set, Set)
	 */
	@Inject
	DbIndex(@Assisted Set<String> names, @Assisted Set<IndexType> types) {
		this.names = unmodifiableSet(names);
		this.types = unmodifiableSet(types);
	}

	public Set<String> getNames() {
		return names;
	}

	public Set<IndexType> getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("names", names)
				.append("types", types).toString();
	}
}
