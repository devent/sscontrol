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
import java.util.List;
import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
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
class DatabaseServiceImpl extends AbstractService {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 8391052070668552719L;

	private final DatabaseServiceImplLogger log;

	private final ServiceLoader<ServiceScriptFactory> serviceScripts;

	private final DatabaseFactory databaseFactory;

	private final UserFactory userFactory;

	private final List<Database> databases;

	private final List<User> users;

	private boolean debugging;

	private String bindAddress;

	private String adminPassword;

	/**
	 * Sets the default database service properties.
	 * 
	 * @param logger
	 *            the {@link DatabaseServiceImplLogger} for logging messages.
	 * 
	 * @param serviceScripts
	 *            the {@link ServiceLoader} to load the database service
	 *            {@link Script} scripts.
	 * 
	 * @param databaseFactory
	 *            the {@link DatabaseFactory} to create a new database.
	 * 
	 * @param userFactory
	 *            the {@link UserFactory} to create a new database user.
	 * 
	 * @param p
	 *            the default database service {@link ContextProperties}
	 *            properties. Expects the following properties:
	 *            <dl>
	 *            <dt>
	 *            {@code debugging}</dt>
	 *            <dd>Set to {@code true} to enable debugging logging.</dd>
	 *            </dl>
	 */
	@Inject
	DatabaseServiceImpl(DatabaseServiceImplLogger logger,
			ServiceLoader<ServiceScriptFactory> serviceScripts,
			DatabaseFactory databaseFactory, UserFactory userFactory,
			@Named("database-defaults.properties") ContextProperties p) {
		this.log = logger;
		this.serviceScripts = serviceScripts;
		this.databases = new ArrayList<Database>();
		this.databaseFactory = databaseFactory;
		this.userFactory = userFactory;
		this.users = new ArrayList<User>();
	}

	@Override
	protected Script getScript(String profileName) throws ServiceException {
		ServiceScriptFactory scriptFactory = findScriptFactory();
		return (Script) scriptFactory.getScript();
	}

	private ServiceScriptFactory findScriptFactory() throws ServiceException {
		String name = getProfile().getProfileName();
		String service = getProfile().getEntry(NAME).get("service").toString();
		for (ServiceScriptFactory scriptFactory : serviceScripts) {
			ServiceScriptInfo info = scriptFactory.getInfo();
			if (info.getProfileName().equals(name)
					&& info.getServiceName().equals(service)) {
				return scriptFactory;
			}
		}
		throw log.errorFindServiceScript(this, name, service);
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
		log.adminPasswordSet(this, password);
	}

	/**
	 * Creates a new database with the specified name.
	 * 
	 * @param name
	 *            the database name.
	 * 
	 * @return the {@link Database}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public Database database(String name) {
		Database database = databaseFactory.create(name);
		databases.add(database);
		log.databaseAdd(this, database);
		return database;
	}

	/**
	 * Creates a new database user with the specified name.
	 * 
	 * @param name
	 *            the user name.
	 * 
	 * @return the {@link User}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified name is empty.
	 */
	public User user(String name) {
		User user = userFactory.create(name);
		users.add(user);
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
				.append("bind address", bindAddress).toString();
	}
}
