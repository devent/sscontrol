package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Authentication group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthGroup {

	@Inject
	private AuthGroupLogger log;

	@Inject
	private AuthUserFactory userFactory;

	private final String name;

	private final List<AuthUser> users;

	@Inject
	AuthGroup(@Assisted String name) {
		this.name = name;
		this.users = new ArrayList<AuthUser>();
	}

	public String getName() {
		return name;
	}

	public void user(Map<String, Object> map, String name) {
		AuthUser user = userFactory.create(this, map, name);
		users.add(user);
		log.userAdded(this, user);
	}

	public List<AuthUser> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
	}
}
