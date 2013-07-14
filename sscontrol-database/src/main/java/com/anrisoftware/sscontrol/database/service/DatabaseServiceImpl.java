/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import static com.anrisoftware.sscontrol.database.service.DatabaseServiceFactory.NAME;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.database.statements.Database;
import com.anrisoftware.sscontrol.database.statements.DatabaseFactory;
import com.anrisoftware.sscontrol.database.statements.User;
import com.anrisoftware.sscontrol.database.statements.UserFactory;

/**
 * Database service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class DatabaseServiceImpl extends AbstractService {

	private final DatabaseServiceImplLogger log;

	private final DatabaseFactory databaseFactory;

	private final UserFactory userFactory;

	private final List<Database> databases;

	private final List<User> users;

	private boolean debugging;

	private String bindAddress;

	private String adminPassword;

	@Inject
	DatabaseServiceImpl(DatabaseServiceImplLogger logger,
			DatabaseFactory databaseFactory, UserFactory userFactory,
			DatabasePropertiesProvider p) {
		this.log = logger;
		this.databases = new ArrayList<Database>();
		this.databaseFactory = databaseFactory;
		this.userFactory = userFactory;
		this.users = new ArrayList<User>();
		setupDefaults(p.get());
	}

	private void setupDefaults(ContextProperties p) {
		debugging = p.getBooleanProperty("debugging");
		bindAddress = p.getProperty("bind_address");
	}

	@Override
	protected Script getScript(String profileName) throws ServiceException {
		ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
		return (Script) scriptFactory.getScript();
	}

	/**
	 * Because we load the script from a script service the dependencies are
	 * already injected.
	 */
	@Override
	protected void injectScript(Script script) {
	}

	/**
	 * Returns the database service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Entry point for the database service script.
	 * 
	 * @param statements
	 *            the database service statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Service database(Object statements) {
		return this;
	}

	/**
	 * Enables or disables the general logging for the database server. Defaults
	 * to {@code false}.
	 * 
	 * @param debugging
	 *            set to {@code true} to enable debug logging, {@code false} to
	 *            disable.
	 */
	public void debugging(boolean debugging) {
		this.debugging = debugging;
		log.debuggingSet(this, debugging);
	}

	/**
	 * The IP address or the host name on which the database server should
	 * listen to connections. Defaults to {@code "127.0.0.1"}.
	 * 
	 * @param address
	 *            the IP address or host name.
	 * 
	 * @throws NullPointerException
	 *             if the specified address is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified address is empty.
	 */
	public void bind_address(String address) {
		log.checkBindAddress(this, address);
		this.bindAddress = address;
		log.bindAddressSet(this, address);
	}

	/**
	 * The administrator password for the database server.
	 * 
	 * @param password
	 *            the administrator password.
	 * 
	 * @throws NullPointerException
	 *             if the specified password is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified password is empty.
	 */
	public void admin_password(String password) {
		log.checkAdminPassword(this, password);
		this.adminPassword = password;
		log.adminPasswordSet(this, getSaveAdminPassword());
	}

	public void database(String name) {
		database(Collections.<String, String> emptyMap(), name);
	}

	public void database(Map<String, String> args, String name) {
		database(args, name, null);
	}

	public Database database(String name, Object statements) {
		return database(Collections.<String, String> emptyMap(), name,
				statements);
	}

	/**
	 * Creates a new database with the specified name.
	 * 
	 * @param args
	 *            additional parameter {@link Map}.
	 * 
	 * @param name
	 *            the database name.
	 * 
	 * @param statements
	 *            database statements.
	 * 
	 * @return the new {@link Database}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public Database database(Map<String, String> args, String name,
			Object statements) {
		Database database = databaseFactory.create(name);
		database.setArguments(args);
		databases.add(database);
		log.databaseAdd(this, database);
		return database;
	}

	public void user(String name) {
		user(Collections.<String, String> emptyMap(), name, null);
	}

	public void user(Map<String, String> args, String name) {
		user(args, name, null);
	}

	public User user(String name, Object statements) {
		return user(Collections.<String, String> emptyMap(), name, statements);
	}

	/**
	 * Creates a new database user with the specified name.
	 * 
	 * @param args
	 *            additional parameter {@link Map}.
	 * 
	 * @param name
	 *            the user name.
	 * 
	 * @param statements
	 *            user statements.
	 * 
	 * @return the {@link User}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public User user(Map<String, String> args, String name, Object statements) {
		User user = userFactory.create(name);
		users.add(user);
		user.setArguments(args);
		log.userAdd(this, user);
		return user;
	}

	/**
	 * Returns whether general logging for the database server is enabled or
	 * disabled.
	 * 
	 * @return {@code true} if debugging is enabled, {@code false} if disabled.
	 */
	public boolean isDebugging() {
		return debugging;
	}

	/**
	 * Returns the administrator password for the database server.
	 * 
	 * @return the administrator password.
	 */
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * Returns the administrator password as stars.
	 */
	public String getSaveAdminPassword() {
		return adminPassword != null ? adminPassword.replaceAll(".", "*")
				: String.valueOf(adminPassword);
	}

	/**
	 * Returns the IP address or the host name on which the database server
	 * should listen to connections.
	 * 
	 * @return the IP address or host name.
	 */
	public String getBindAddress() {
		return bindAddress;
	}

	/**
	 * Returns the databases of the server.
	 * 
	 * @return an unmodifiable {@link List}.
	 */
	public List<Database> getDatabases() {
		return unmodifiableList(databases);
	}

	/**
	 * Sets the default character set for databases.
	 * 
	 * @param set
	 *            the character set.
	 * 
	 * @throws NullPointerException
	 *             if the specified character set is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified character set is empty.
	 */
	public void setDefaultCharacterSet(String set) {
		for (Database database : databases) {
			if (database.getCharacterSet() == null) {
				database.setCharacterSet(set);
			}
		}
	}

	/**
	 * Sets the default collate for databases.
	 * 
	 * @param collate
	 *            the collate.
	 * 
	 * @throws NullPointerException
	 *             if the specified collate is {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified collate is empty.
	 */
	public void setDefaultCollate(String collate) {
		for (Database database : databases) {
			if (database.getCollate() == null) {
				database.setCollate(collate);
			}
		}
	}

	/**
	 * Returns the users of the server.
	 * 
	 * @return an unmodifiable {@link List}.
	 */
	public List<User> getUsers() {
		return unmodifiableList(users);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("debugging", debugging)
				.append("bind address", bindAddress)
				.append("administrator passowrd", getSaveAdminPassword())
				.toString();
	}
}
