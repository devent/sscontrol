/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Defines a database user identified by the user name and server host.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class User implements Serializable {

    private static final String SERVER_ARG = "server";

    private static final String PASSWORD_ARG = "password";

    @Inject
    private transient UserAccessFactory accessFactory;

    private final UserLogger log;

    private final String name;

    private String password;

    private String server;

    /**
     * The databases to which the user have access.
     */
    private final List<UserAccess> access;

    /**
     * @see UserFactory#create(String)
     */
    @Inject
    User(UserLogger logger, @Assisted String name) {
        this.log = logger;
        this.name = name;
        this.access = new ArrayList<UserAccess>();
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
     * Sets the access of the user.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code database:} the database name;
     *            </ul>
     *
     * @throws NullPointerException
     *             if the database name is {@code null}.
     *
     * @throws IllegalArgumentException
     *             if the database name is empty.
     */
    void access(Map<String, Object> args) {
        log.checkDatabase(this, name);
        UserAccess access = accessFactory.create(args);
        this.access.add(access);
        log.databaseAdd(this, name);
    }

    /**
     * Returns the user access for databases.
     *
     * @return the {@link List} of the user access.
     */
    public List<UserAccess> getAccess() {
        return access;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append(PASSWORD_ARG, getSavePassword())
                .append(SERVER_ARG, server).append("access", access).toString();
    }

}
