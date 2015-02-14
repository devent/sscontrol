/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.database.service.DatabaseServiceFactory.SERVICE_NAME;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
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
class DatabaseServiceImpl extends AbstractService implements DatabaseService {

    private static final String USER_KEY = "name";

    private static final String PASSWORD_KEY = "password";

    private static final String PORT_KEY = "port";

    private static final String ADMIN_KEY = "admin";

    private static final String BIND_KEY = "bind";

    private static final String FILE_KEY = "file";

    private static final String LEVEL_KEY = "level";

    private static final String DEBUG_KEY = "debug";

    private static final String DATABASE_KEY = "name";

    private final List<Database> databases;

    private final List<User> users;

    @Inject
    private DatabaseServiceImplLogger log;

    @Inject
    private DatabaseFactory databaseFactory;

    @Inject
    private UserFactory userFactory;

    private StatementsTable statementsTable;

    private StatementsMap statementsMap;

    @Inject
    DatabaseServiceImpl() {
        this.databases = new ArrayList<Database>();
        this.users = new ArrayList<User>();
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(SERVICE_NAME);
        return (Script) scriptFactory.getScript();
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(factory, SERVICE_NAME);
        map.addAllowed(BIND_KEY, ADMIN_KEY);
        map.setAllowValue(true, BIND_KEY);
        map.addAllowedKeys(BIND_KEY, PORT_KEY);
        map.addAllowedKeys(ADMIN_KEY, PASSWORD_KEY);
        this.statementsMap = map;
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(factory, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.addAllowedKeys(DEBUG_KEY, LEVEL_KEY, FILE_KEY);
        this.statementsTable = table;
    }

    /**
     * Returns the database service name.
     */
    @Override
    public String getName() {
        return SERVICE_NAME;
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

    @Override
    public Map<String, Object> getDebugLevels() {
        return statementsTable.tableKeys(DEBUG_KEY, LEVEL_KEY);
    }

    @Override
    public Map<String, Object> getDebugFiles() {
        return statementsTable.tableKeys(DEBUG_KEY, FILE_KEY);
    }

    @Override
    public String getBindingAddress() {
        Object value = statementsMap.value(BIND_KEY);
        if (value instanceof BindingAddress) {
            return ((BindingAddress) value).getAddress();
        } else if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public Integer getBindingPort() {
        return statementsMap.mapValue(BIND_KEY, PORT_KEY);
    }

    @Override
    public String getAdminPassword() {
        return statementsMap.mapValue(ADMIN_KEY, PASSWORD_KEY);
    }

    public void database(String name) {
        database(new HashMap<String, Object>(), name);
    }

    public void database(Map<String, Object> args, String name) {
        database(args, name, null);
    }

    public Database database(String name, Object statements) {
        return database(new HashMap<String, Object>(), name, statements);
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
    public Database database(Map<String, Object> args, String name,
            Object statements) {
        args.put(DATABASE_KEY, name);
        Database database = databaseFactory.create(args);
        databases.add(database);
        log.databaseAdd(this, database);
        return database;
    }

    @Override
    public List<Database> getDatabases() {
        return unmodifiableList(databases);
    }

    public void user(String name) {
        user(new HashMap<String, Object>(), name, null);
    }

    public void user(Map<String, Object> args, String name) {
        user(args, name, null);
    }

    public User user(String name, Object statements) {
        return user(new HashMap<String, Object>(), name, statements);
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
    public User user(Map<String, Object> args, String name, Object statements) {
        args.put(USER_KEY, name);
        User user = userFactory.create(args);
        users.add(user);
        log.userAdd(this, user);
        return user;
    }

    @Override
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

    public Object methodMissing(String name, Object args) {
        try {
            return statementsMap.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsTable.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }
}
