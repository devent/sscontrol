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

import java.util.Map;

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

	private DatabaseLogger log;

	private String database;

	private String user;

	private String password;

	private Map<String, Object> args;

	private String server;

	private Integer port;

	/**
	 * @see DatabaseFactory#create(Map, String)
	 */
	@Inject
	Database(@Assisted Map<String, Object> args, @Assisted String name) {
		this.args = args;
		this.database = name;
	}

	@Inject
	void setDatabaseLogger(DatabaseLogger logger) {
		this.log = logger;
		setDatabase(database);
		setUser(args.get("user"));
		setPassword(args.get("password"));
		setServer(args.get("server"));
		setPort(args.get("port"));
		args = null;
	}

	private void setPort(Object object) {
		if (object == null) {
			return;
		}
		this.port = (Integer) object;
	}

	public Integer getPort() {
		return port;
	}

	private void setServer(Object object) {
		if (object == null) {
			return;
		}
		log.checkServer(object);
		this.server = object.toString();
	}

	public String getServer() {
		return server;
	}

	private void setDatabase(Object object) {
		log.checkDatabase(object);
		this.database = object.toString();
	}

	public String getDatabase() {
		return database;
	}

	private void setUser(Object object) {
		log.checkUser(object);
		this.user = object.toString();
	}

	public String getUser() {
		return user;
	}

	private void setPassword(Object object) {
		log.checkPassword(object);
		this.password = object.toString();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("database", database)
				.append("user", user).append("password", password)
				.append("server", server).append("port", port).toString();
	}
}
