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
package com.anrisoftware.sscontrol.httpd.statements.authfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.statements.auth.SatisfyType;
import com.google.inject.assistedinject.Assisted;

/**
 * File/authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthFile extends AbstractAuth {

	private static final String SATISFY = "satisfy";

	private static final String LOCATION = "location";

	private static final String APPENDING = "appending";

	private static final String TYPE = "type";

	private static final String PROVIDER = "provider";

	private final List<FileUser> users;

	private final List<FileGroup> groups;

	private Map<String, Object> args;

	private AuthFileLogger log;

	@Inject
	private FileUserFactory userFactory;

	@Inject
	private FileGroupFactory groupFactory;

	private boolean appending;

	/**
	 * @see AuthFileFactory#create(Map, String)
	 */
	@Inject
	AuthFile(@Assisted Map<String, Object> args, @Assisted String name) {
		super(name);
		this.args = args;
		this.users = new ArrayList<FileUser>();
		this.groups = new ArrayList<FileGroup>();
	}

	@Inject
	void setAuthLogger(AuthFileLogger logger) {
		this.log = logger;
		setLocation(args.get(LOCATION));
		if (args.containsKey(TYPE)) {
			setType((AuthType) args.get(TYPE));
		}
		if (args.containsKey(PROVIDER)) {
			setProvider((AuthProvider) args.get(PROVIDER));
		}
		if (args.containsKey(APPENDING)) {
			setAppending((Boolean) args.get(APPENDING));
		}
		if (args.containsKey(SATISFY)) {
			setSatisfy((SatisfyType) args.get(SATISFY));
		}
		this.args = null;
	}

	private void setLocation(Object location) {
		log.checkLocation(location);
		super.setLocation(location.toString());
	}

	public String getLocationFilename() {
		return new File(getLocation()).getName();
	}

	public void setAppending(boolean appending) {
		this.appending = appending;
	}

	public boolean isAppending() {
		return appending;
	}

	public FileGroup group(String name, Object s) {
		FileGroup group = groupFactory.create(name);
		groups.add(group);
		log.groupAdded(this, group);
		return group;
	}

	public List<FileGroup> getGroups() {
		return groups;
	}

	public void user(Map<String, Object> map, String name) {
		FileUser user = userFactory.create(map, name);
		users.add(user);
		log.userAdded(this, user);
	}

	public List<FileUser> getUsers() {
		return users;
	}

	public String getPasswordFileName() {
		log.checkType(getType());
		String location = getLocationFilename();
		switch (getType()) {
		case basic:
			return String.format("%s.passwd", location);
		case digest:
			return String.format("%s-digest.passwd", location);
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.toString();
	}
}
