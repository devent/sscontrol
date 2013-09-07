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
