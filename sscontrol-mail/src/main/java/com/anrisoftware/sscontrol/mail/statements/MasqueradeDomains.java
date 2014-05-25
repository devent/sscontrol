/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.statements;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MasqueradeDomains {

	private final MasqueradeDomainsLogger log;

	private final List<String> domains;

	private final List<String> userExceptions;

	@Inject
	MasqueradeDomains(MasqueradeDomainsLogger logger) {
		this.log = logger;
		this.domains = new ArrayList<String>();
		this.userExceptions = new ArrayList<String>();
	}

	/**
	 * Adds the list of domains to masquerade.
	 * 
	 * @param list
	 *            a list of domains separated by {@code "[ ,;]"}.
	 */
	public void domains(String list) {
		log.checkDomains(this, list);
		List<String> domains = asList(split(list, " ,;"));
		this.domains.addAll(domains);
		log.domainsAdded(this, domains);
	}

	/**
	 * Adds the list of users to not to masquerade.
	 * 
	 * @param list
	 *            a list of users separated by {@code "[ ,;]"}.
	 */
	public void users(String list) {
		log.checkUsers(this, list);
		List<String> users = asList(split(list, " ,;"));
		this.userExceptions.addAll(users);
		log.usersAdded(this, users);
	}

	/**
	 * Returns the list of domains to masquerade.
	 * 
	 * @return an unmodifiable {@link List} of the domains.
	 */
	public List<String> getDomains() {
		return unmodifiableList(domains);
	}

	/**
	 * Returns the list of users to not to masquerade.
	 * 
	 * @return an unmodifiable {@link List} of the users.
	 */
	public List<String> getUserExceptions() {
		return unmodifiableList(userExceptions);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("domains", domains)
				.append("user exceptions", userExceptions).toString();
	}
}
