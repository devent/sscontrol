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
package com.anrisoftware.sscontrol.httpd.authfile;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * File/user for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class FileUser {

	private static final String PASSWORD = "password";

	private final String name;

	private String password;

	private FileGroup group;

	/**
	 * @see FileUserFactory#create(Map, String)
	 */
	@AssistedInject
	FileUser(@Assisted Map<String, Object> map, @Assisted String name) {
		this.name = name;
		if (map.containsKey(PASSWORD)) {
			this.password = (String) map.get(PASSWORD);
		}
	}

	/**
	 * @see FileUserFactory#create(FileGroup, Map, String)
	 */
	@AssistedInject
	FileUser(@Assisted FileGroup group, @Assisted Map<String, Object> map,
			@Assisted String name) {
		this.name = name;
		this.group = group;
		if (map.containsKey(PASSWORD)) {
			this.password = (String) map.get(PASSWORD);
		}
	}

	/**
	 * Returns the name of the user.
	 * 
	 * @return the user name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the password for the user.
	 * 
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the group that the user belongs to.
	 * 
	 * @return the {@link FileGroup} or {@code null}.
	 */
	public FileGroup getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("group", group).toString();
	}
}
