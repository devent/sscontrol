/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ControlUser {

	@Inject
	private ControlUserLogger log;

	private String user;

	private String password;

	private String database;

	public void setUser(String user) {
		log.checkUser(user);
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(Object password) {
		log.checkPassword(password);
		this.password = password.toString();
	}

	public String getPassword() {
		return password;
	}

	public void setDatabase(Object database) {
		log.checkDatabase(database);
		this.database = database.toString();
	}

	public String getDatabase() {
		return database;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("user", user)
				.append("password", password).append("database", database)
				.toString();
	}
}
