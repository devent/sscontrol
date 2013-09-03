package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdminUser {

	@Inject
	private AdminUserLogger log;

	private String user;

	private String password;

	public void setUser(String user) {
		log.checkUser(user);
		this.user = user;
	}

	public String getUser() {
		return user;
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
		return new ToStringBuilder(this).append("user", user)
				.append("password", password).toString();
	}
}
