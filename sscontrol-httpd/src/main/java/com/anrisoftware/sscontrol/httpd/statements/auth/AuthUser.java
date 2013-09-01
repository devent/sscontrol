package com.anrisoftware.sscontrol.httpd.statements.auth;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * User for authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthUser {

	private static final String PASSWORD = "password";

	private final String name;

	private String password;

	private AuthGroup group;

	/**
	 * @see AuthUserFactory#create(Map, String)
	 */
	@AssistedInject
	AuthUser(@Assisted Map<String, Object> map, @Assisted String name) {
		this.name = name;
		if (map.containsKey(PASSWORD)) {
			this.password = (String) map.get(PASSWORD);
		}
	}

	/**
	 * @see AuthUserFactory#create(AuthGroup, Map, String)
	 */
	@AssistedInject
	AuthUser(@Assisted AuthGroup group, @Assisted Map<String, Object> map,
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
	 * @return the {@link AuthGroup} or {@code null}.
	 */
	public AuthGroup getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("group", group).toString();
	}
}
