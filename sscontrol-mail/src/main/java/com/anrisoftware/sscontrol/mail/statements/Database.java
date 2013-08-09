/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
 * Database access.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Database {

	private final DatabaseLogger log;

	private final String database;

	private String user;

	private String password;

	/**
	 * @see DatabaseFactory#create(String)
	 */
	@Inject
	Database(DatabaseLogger logger, @Assisted String database) {
		this.log = logger;
		log.checkDatabase(database);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

	public Database user(String user) {
		this.user = user;
		log.userSet(this, user);
		return this;
	}

	public String getUser() {
		return user;
	}

	public void password(String password) {
		this.password = password;
		log.passwordSet(this, password);
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("database", database)
				.append("user", user).append("password", password).toString();
	}
}
