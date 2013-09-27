/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
 * Database administrator.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Admin implements Serializable {

	private static final String PASSWORD = "password";

	@Inject
	private AdminLogger log;

	private Map<String, Object> args;

	private String password;

	/**
	 * @see AdminFactory#create(Map)
	 */
	@Inject
	Admin(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setAdminLogger(AdminLogger logger) {
		this.log = logger;
		setPassword(args.get(PASSWORD));
		args = null;
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
		return new ToStringBuilder(this).append(PASSWORD, password).toString();
	}

}
