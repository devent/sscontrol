package com.anrisoftware.sscontrol.httpd.statements.authldap;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/authentication credentials.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Credentials {

	private static final String PASSWORD = "password";

	private CredentialsLogger log;

	private Map<String, Object> args;

	private final String name;

	private String password;

	@Inject
	Credentials(@Assisted Map<String, Object> args, @Assisted String name) {
		this.name = name;
		this.args = args;
	}

	@Inject
	void setCredentialsLogger(CredentialsLogger logger) {
		this.log = logger;
		if (args.containsKey(PASSWORD)) {
			setPassword(args.get(PASSWORD));
		}
		args = null;
	}

	public String getName() {
		return name;
	}

	public void setPassword(Object password) {
		log.checkPassword(password);
		this.password = password.toString();
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).toString();
	}
}
