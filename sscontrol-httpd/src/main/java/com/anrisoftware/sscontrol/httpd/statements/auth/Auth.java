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
package com.anrisoftware.sscontrol.httpd.statements.auth;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Auth {

	private static final String SATISFY = "satisfy";

	private static final String LOCATION = "location";

	private static final String APPENDING = "appending";

	private static final String TYPE = "type";

	private static final String GROUP = "group";

	private static final String PROVIDER = "provider";

	private final AuthLogger log;

	@Inject
	private AuthRequireFactory requireFactory;

	@Inject
	private AuthUserFactory userFactory;

	@Inject
	private AuthGroupFactory groupFactory;

	private final List<AuthRequire> requires;

	private final List<AuthUser> users;

	private final List<AuthGroup> groups;

	private AuthProvider provider;

	private AuthType type;

	private final String name;

	private boolean appending;

	private String location;

	private SatisfyType satisfy;

	/**
	 * @see AuthFactory#create(Map, String)
	 */
	@Inject
	Auth(AuthLogger log, @Assisted Map<String, Object> args,
			@Assisted String name) {
		this.log = log;
		this.name = name;
		setLocation(args.get(LOCATION));
		setType((AuthType) args.get(TYPE));
		setProvider((AuthProvider) args.get(PROVIDER));
		setAppending((Boolean) args.get(APPENDING));
		setSatisfy((SatisfyType) args.get(SATISFY));
		this.requires = new ArrayList<AuthRequire>();
		this.users = new ArrayList<AuthUser>();
		this.groups = new ArrayList<AuthGroup>();
	}

	public String getName() {
		return name;
	}

	public void setLocation(Object location) {
		log.checkLocation(location);
		this.location = location.toString();
	}

	public String getLocation() {
		return location;
	}

	public void setType(AuthType type) {
		log.checkType(type);
		this.type = type;
	}

	public AuthType getType() {
		return type;
	}

	public void setProvider(AuthProvider provider) {
		log.checkProvider(provider);
		this.provider = provider;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void setAppending(Boolean appending) {
		this.appending = appending;
	}

	public boolean isAppending() {
		return appending;
	}

	public void setSatisfy(SatisfyType type) {
		log.checkSatisfy(type);
		this.satisfy = type;
	}

	public SatisfyType getSatisfy() {
		return satisfy;
	}

	public void valid_user() {
		AuthRequireValidUser require = requireFactory.validUser();
		requires.add(require);
		log.requireValidUserAdded(this, require);
	}

	public void require(Object s) {
	}

	public void require(Map<String, Object> map) {
		if (map.containsKey(GROUP)) {
			AuthRequireGroup require = requireFactory.group(map);
			requires.add(require);
			log.requireGroupAdded(this, require);
		}
	}

	public List<AuthRequire> getRequires() {
		return requires;
	}

	public AuthGroup group(String name, Object s) {
		AuthGroup group = groupFactory.create(name);
		groups.add(group);
		log.groupAdded(this, group);
		return group;
	}

	public List<AuthGroup> getGroups() {
		return groups;
	}

	public void user(Map<String, Object> map, String name) {
		AuthUser user = userFactory.create(map, name);
		users.add(user);
		log.userAdded(this, user);
	}

	public List<AuthUser> getUsers() {
		return users;
	}

	public String getPasswordFileName() {
		log.checkType(type);
		switch (type) {
		case basic:
			return format("%s.passwd", getName());
		case digest:
			return format("%s-digest.passwd", getName());
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
