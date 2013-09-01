package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * User for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthUser {

	private static final String GROUP = "group";

	private static final String PASSWORD = "password";

	private final String name;

	private String password;

	private String group;

	/**
	 * @see AuthUserFactory#create(Map, String)
	 */
	@Inject
	AuthUser(@Assisted Map<String, Object> map, @Assisted String name) {
		this.name = name;
		if (map.containsKey(PASSWORD)) {
			this.password = (String) map.get(PASSWORD);
		}
		if (map.containsKey(GROUP)) {
			this.group = (String) map.get(GROUP);
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
	 * @return the group or {@code null}.
	 */
	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("group", group).toString();
	}
}
