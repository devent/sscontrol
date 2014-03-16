/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * User access to database.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * 
 * @since 1.0
 */
@SuppressWarnings("serial")
public class UserAccess implements Serializable {

	private static final String DATABASE = "database";

	@Inject
	private UserAccessLogger log;

	private Map<String, Object> args;

	private String database;

	/**
	 * @see UserAccessFactory#create(Map)
	 */
	@Inject
	UserAccess(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setUserAccessLogger(UserAccessLogger logger) {
		this.log = logger;
		setDatabase(args.get(DATABASE));
		args = null;
	}

	private void setDatabase(Object object) {
		log.checkDatabase(object);
		this.database = object.toString();
	}

	public String getDatabase() {
		return database;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(DATABASE, database).toString();
	}

}
