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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Mail user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class User {

	public static final String PASSWORD_KEY = "password";

	private final UserLogger log;

	private final Domain domain;

	private final String name;

	private String password;

	private boolean enabled;

	/**
	 * @see UserFactory#create(Domain, String)
	 */
	@AssistedInject
	User(UserLogger logger, @Assisted Domain domain, @Assisted String name) {
		this.log = logger;
		log.checkName(name);
		this.domain = domain;
		this.name = name;
		this.enabled = true;
	}

	/**
	 * @see UserFactory#create(Domain, String, String)
	 */
	@AssistedInject
	User(UserLogger logger, @Assisted Domain domain,
			@Assisted("name") String name, @Assisted("password") String password) {
		this.log = logger;
		log.checkName(name);
		log.checkPassword(name);
		this.domain = domain;
		this.name = name;
		this.password = password;
		this.enabled = true;
	}

	public Domain getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("domain", domain.getName()).append("enabled", enabled)
				.toString();
	}
}
