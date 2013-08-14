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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * List of root servers for recursive.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Roots {

	@Inject
	private RootsLogger log;

	private final List<String> servers;

	Roots() {
		this.servers = new ArrayList<String>();
	}

	/**
	 * Adds a new root servers group.
	 * 
	 * @param name
	 *            the name the servers group.
	 */
	void servers(String name) {
		servers.add(name);
		log.serversGroupAdded(this, name);
	}

	/**
	 * Returns the root servers groups.
	 * 
	 * @return the {@link List}.
	 */
	public List<String> getServers() {
		return unmodifiableList(servers);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(servers).toString();
	}
}
