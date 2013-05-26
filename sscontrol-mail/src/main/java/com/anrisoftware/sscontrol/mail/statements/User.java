package com.anrisoftware.sscontrol.mail.statements;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Mail user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class User {

	public static final String PASSWORD_KEY = "password";

	private final UserLogger log;

	private final Domain domain;

	private final String name;

	private final String password;

	private boolean enabled;

	/**
	 * @see UserFactory#create(Domain, String, String)
	 */
	@Inject
	User(UserLogger logger, @Assisted Domain domain,
			@Assisted("name") String name, @Assisted("password") String password) {
		this.log = logger;
		log.checkName(name);
		log.checkPassword(name);
		this.domain = domain;
		this.name = name;
		this.password = password;
		this.enabled = true;
	}

	public Domain getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("enabled", enabled).toString();
	}
}
