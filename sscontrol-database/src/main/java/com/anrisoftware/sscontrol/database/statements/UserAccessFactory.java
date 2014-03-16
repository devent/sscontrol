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

import java.util.Map;

/**
 * Factory to create user access to database.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UserAccessFactory {

	/**
	 * Creates user access to database.
	 * 
	 * @param args
	 *            the {@link Map} arguments for the user access.
	 * 
	 * @return the {@link UserAccess}.
	 * 
	 * @throws NullPointerException
	 *             if the database name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the database name is blank.
	 */
	UserAccess create(Map<String, Object> args);
}
