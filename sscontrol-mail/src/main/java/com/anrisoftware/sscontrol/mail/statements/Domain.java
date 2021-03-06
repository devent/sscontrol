/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.mail.statements.Alias.DESTINATION_KEY;
import static com.anrisoftware.sscontrol.mail.statements.User.PASSWORD_KEY;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * The domain where mails are received to and distributed to.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Domain {

	private final DomainLogger log;

	private final String name;

	private final List<Alias> aliases;

	private final List<User> users;

	private final AliasFactory aliasFactory;

	private final CatchallFactory catchallFactory;

	private boolean enabled;

	private final UserFactory userFactory;

	@Inject
	Domain(DomainLogger logger, AliasFactory aliasFactory,
			UserFactory userFactory, CatchallFactory catchallFactory,
			@Assisted String name) {
		this.log = logger;
		this.aliasFactory = aliasFactory;
		this.catchallFactory = catchallFactory;
		this.name = name;
		this.enabled = true;
		this.aliases = new ArrayList<Alias>();
		this.users = new ArrayList<User>();
		this.userFactory = userFactory;
	}

	public String getName() {
		return name;
	}

	public void catchall(Map<String, Object> args) {
		String destination = args.get("destination").toString();
		Catchall alias = catchallFactory.create(this, destination);
		log.catchallAdded(this, alias);
		aliases.add(alias);
	}

	public void alias(Map<String, Object> args, String name) {
		alias(args, name, null);
	}

	public Alias alias(Map<String, Object> args, String name, Object statements) {
		String destination = log.checkDestination(args.get(DESTINATION_KEY));
		Alias alias = aliasFactory.create(this, name, destination);
		aliases.add(alias);
		log.aliasAdded(this, alias);
		return alias;
	}

	public List<Alias> getAliases() {
		return unmodifiableList(aliases);
	}

	public void user(String name) {
		user(name, null);
	}

	public User user(String name, Object statements) {
		User user = userFactory.create(this, name);
		users.add(user);
		log.userAdded(this, user);
		return user;
	}

	public void user(Map<String, Object> args, String name) {
		user(args, name, null);
	}

	public User user(Map<String, Object> args, String name, Object statements) {
		String password = log.checkPassword(args.get(PASSWORD_KEY));
		User user = userFactory.create(this, name, password);
		users.add(user);
		log.userAdded(this, user);
		return user;
	}

	public List<User> getUsers() {
		return unmodifiableList(users);
	}

	public Domain enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("enabled", enabled).toString();
	}
}
