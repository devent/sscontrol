/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.statements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * List of aliases.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Aliases {

	@Inject
	private AliasesLogger log;

	private final List<Alias> aliases;

	Aliases() {
		this.aliases = new ArrayList<Alias>();
	}

	/**
	 * Adds the alias.
	 * 
	 * @param alias
	 *            the {@link Alias}.
	 * 
	 * @throws NullPointerException
	 *             if the specified alias is {@code null}.
	 */
	public void addAlias(Alias alias) {
		log.checkAlias(this, alias);
		aliases.add(alias);
		log.aliasAdded(this, alias);
	}

	/**
	 * Returns the list of aliases.
	 * 
	 * @return the {@link List} of aliases {@link Alias}.
	 */
	public List<Alias> getAliases() {
		return aliases;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(aliases).toString();
	}
}
