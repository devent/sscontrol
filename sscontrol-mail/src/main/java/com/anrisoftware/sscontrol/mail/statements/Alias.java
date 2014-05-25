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

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Alias for an user of the domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Alias {

	public static final String DESTINATION_KEY = "destination";

	private final String name;

	private final String destination;

	private boolean enabled;

	private final Domain domain;

	/**
	 * @see AliasFactory#create(Domain, String, String)
	 */
	@Inject
	Alias(AliasLogger logger, @Assisted Domain domain,
			@Assisted("name") String name,
			@Assisted("destination") String destination) {
		logger.checkName(name);
		logger.checkDestination(destination);
		this.domain = domain;
		this.name = name;
		this.destination = destination;
		this.enabled = true;
	}

	public Domain getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getDestination() {
		return destination;
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isNameDomain() {
		return name.contains("@");
	}

	public boolean isDestinationDomain() {
		return destination.contains("@");
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("alias", name)
				.append("domain", domain.getName())
				.append("destination", destination).append("enabled", enabled)
				.toString();
	}
}
