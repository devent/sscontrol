package com.anrisoftware.sscontrol.mail.statements;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Database access.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Database {

	private final DatabaseLogger log;

	private final String database;

	private String user;

	private String password;

	/**
	 * @see DatabaseFactory#create(String)
	 */
	@Inject
	Database(DatabaseLogger logger, @Assisted String database) {
		this.log = logger;
		log.checkDatabase(database);
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

	public Database user(String user) {
		this.user = user;
		log.userSet(this, user);
		return this;
	}

	public String getUser() {
		return user;
	}

	public void password(String password) {
		this.password = password;
		log.passwordSet(this, password);
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("database", database)
				.append("user", user).append("password", password).toString();
	}
}
