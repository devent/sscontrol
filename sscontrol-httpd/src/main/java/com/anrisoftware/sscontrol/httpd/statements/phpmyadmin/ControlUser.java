package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ControlUser {

	@Inject
	private ControlUserLogger log;

	private String user;

	private String password;

	private String database;

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

	public void setDatabase(Object database) {
		log.checkDatabase(database);
		this.database = database.toString();
	}

	public String getDatabase() {
		return database;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("user", user)
				.append("password", password).append("database", database)
				.toString();
	}
}
