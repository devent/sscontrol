package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	private static final String SERVER_ARG = "server";

	private static final String PASSWORD_ARG = "password";

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

	/**
	 * Sets the user name and default properties.
	 * 
	 * @param logger
	 *            the {@link UserLogger} for logging messages.
	 * 
	 * @param p
	 *            the default user {@link ContextProperties} properties. Expects
	 *            the following properties:
	 *            <dl>
	 *            <dt>{@code user_server}</dt>
	 *            <dd>The default user server host.</dd>
	 *            </dl>
	 * 
	 * @param name
	 *            the user name.
	 */
	@Inject
	User(UserLogger logger,
			@Named("database-defaults-properties") ContextProperties p,
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
	 * Sets additional arguments for the user.
	 * 
	 * @param args
	 *            the arguments {@link Map}.
	 * 
	 * @see #setPassword(String)
	 * @see #setServer(String)
	 */
	public void setArguments(Map<String, String> args) {
		if (args.containsKey(PASSWORD_ARG)) {
			setPassword(args.get(PASSWORD_ARG));
		}
		if (args.containsKey(SERVER_ARG)) {
			setServer(args.get(SERVER_ARG));
		}
	}

	/**
	 * Returns the name of the user.
	 */
	public String getName() {
		return name;
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
	 */
	void setPassword(String password) {
		log.checkPassword(this, password);
		this.password = password;
		log.passwordSet(this, getSavePassword());
	}

	/**
	 * Returns the user password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the user password as stars.
	 */
	public String getSavePassword() {
		return password != null ? password.replaceAll(".", "*") : String
				.valueOf(password);
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
	 */
	void setServer(String server) {
		log.checkServer(this, server);
		this.server = server;
		log.serverSet(this, server);
	}

	/**
	 * Returns on which server host the user can connect.
	 */
	public String getServer() {
		return server;
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
				.append(PASSWORD_ARG, getSavePassword())
				.append(SERVER_ARG, server).append("databases", databases)
				.toString();
	}

}
