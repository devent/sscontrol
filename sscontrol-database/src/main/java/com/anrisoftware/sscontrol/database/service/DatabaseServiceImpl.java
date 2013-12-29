/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
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

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingArgs;
import com.anrisoftware.sscontrol.core.bindings.BindingFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.database.statements.Admin;
import com.anrisoftware.sscontrol.database.statements.AdminFactory;
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

    private final List<Database> databases;

    private final List<User> users;

    @Inject
    private DatabaseServiceImplLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private UserFactory userFactory;

    @Inject
    private DebugLoggingFactory debugLoggingFactory;

    @Inject
    private AdminFactory adminFactory;

    @Inject
    private Binding binding;

    @Inject
    private BindingArgs bindingArgs;

    private DebugLogging debug;

    private Admin admin;

    @Inject
    DatabaseServiceImpl() {
        this.databases = new ArrayList<Database>();
        this.users = new ArrayList<User>();
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
     * Sets the debug logging for the mail server.
     * 
     * @see DebugLoggingFactory#create(Map)
     */
    public void debug(Map<String, Object> args) {
        debug = debugLoggingFactory.create(args);
        log.debugLoggingSet(this, debug);
    }

    public void setDebug(DebugLogging debug) {
        this.debug = debug;
    }

    public DebugLogging getDebug() {
        return debug;
    }

    /**
     * Sets the IP addresses or host names to where to bind the database
     * service.
     * 
     * @see BindingFactory#create(Map, String...)
     */
    public void bind(Map<String, Object> args) throws ServiceException {
        List<Address> addresses = bindingArgs.createAddress(this, args);
        binding.addAddress(addresses);
        log.bindingSet(this, binding);
    }

    /**
     * Sets the IP addresses or host names to where to bind the database
     * service.
     * 
     * @see BindingFactory#create(BindingAddress)
     */
    public void bind(BindingAddress address) throws ServiceException {
        binding.addAddress(address);
        log.bindingSet(this, binding);
    }

    /**
     * Returns a list of the IP addresses where to bind the database service.
     * 
     * @return the {@link Binding}.
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * The administrator password for the database server.
     * 
     * @see AdminFactory#create(Map)
     */
    public void admin(Map<String, Object> args) {
        this.admin = adminFactory.create(args);
        log.adminSet(this, admin);
    }

    public Admin getAdmin() {
        return admin;
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

    /**
     * @see BindingAddress#local
     */
    public BindingAddress getLocal() {
        return BindingAddress.local;
    }

    /**
     * @see BindingAddress#all
     */
    public BindingAddress getAll() {
        return BindingAddress.all;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }
}
