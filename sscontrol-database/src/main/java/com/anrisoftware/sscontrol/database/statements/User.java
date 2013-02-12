package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.assistedinject.Assisted;

/**
 * Defines a database user identified by the user name and server host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class User implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 771102898596872661L;

	private final UserLogger log;

	private final String name;

	private String password;

	private String server;

	/**
	 * The databases to which the user have access.
	 */
	private final List<String> databases;

	@Inject
	User(UserLogger logger,
			@Named("database-defaults.properties") ContextProperties p,
			@Assisted String name) {
		this.log = logger;
		this.name = name;
		this.databases = new ArrayList<String>();
		setupDefaults(p);
	}

	private void setupDefaults(ContextProperties p) {
		server = p.getProperty("user_server");
	}

	/**
	 * Sets the user password.
	 * 
	 * @param password
	 *            the user password.
	 * 
	 * @throws NullPointerException
	 *             if the specified password is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified password is empty.
	 * 
	 * @return this {@link User}.
	 */
	User password(String password) {
		log.checkPassword(this, password);
		this.password = password;
		log.passwordSet(this, password);
		return this;
	}

	/**
	 * Sets the user server host.
	 * 
	 * @param server
	 *            the user server host.
	 * 
	 * @throws NullPointerException
	 *             if the specified server host is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified server host is empty.
	 * 
	 * @return this {@link User}.
	 */
	User server(String server) {
		log.checkServer(this, server);
		this.server = server;
		log.serverSet(this, server);
		return this;
	}

	/**
	 * Sets the database that the user have read and write access to.
	 * 
	 * @param name
	 *            the database name.
	 * 
	 * @throws NullPointerException
	 *             if the specified database name is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified database name is empty.
	 */
	void use_database(String name) {
		log.checkDatabase(this, name);
		databases.add(name);
		log.databaseAdd(this, name);
	}

	/**
	 * Returns the name of the user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns on which server host the user can connect.
	 */
	public String getServer() {
		return server;
	}

	/**
	 * Returns the user password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the user databases.
	 * 
	 * @return the {@link List} of the database names.
	 */
	public List<String> getDatabases() {
		return databases;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("password", password).append("server", server)
				.append("databases", databases).toString();
	}
}
