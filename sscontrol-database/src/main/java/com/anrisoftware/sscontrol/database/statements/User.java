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
package com.anrisoftware.sscontrol.database.statements;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Defines a database user identified by the user name and server host.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class User implements Serializable {

    private static final String NAME = "name";
    private static final String DATABASE_KEY = "database";
    private static final String ACCESS_KEY = "access";
    private static final String SERVER_KEY = "server";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_KEY = "user";

    private final StatementsMap statementsMap;

    private final UserLogger log;

    /**
     * @see UserFactory#create(Map)
     */
    @Inject
    User(UserLogger log, StatementsMapFactory statementsMapFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.statementsMap = createStatementsMap(statementsMapFactory);
        setupArgs(statementsMap, args);
    }

    private StatementsMap createStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, USER_KEY);
        map.addAllowed(USER_KEY, ACCESS_KEY);
        map.setAllowValue(true, USER_KEY);
        map.addAllowedKeys(USER_KEY, PASSWORD_KEY, SERVER_KEY);
        map.addAllowedKeys(ACCESS_KEY, DATABASE_KEY);
        map.addAllowedMultiKeys(ACCESS_KEY, DATABASE_KEY);
        return map;
    }

    private void setupArgs(StatementsMap map, Map<String, Object> args) {
        map.putValue(USER_KEY, log.name(this, args));
        if (args.containsKey(PASSWORD_KEY)) {
            map.putMapValue(USER_KEY, PASSWORD_KEY, args.get(PASSWORD_KEY));
        }
        if (args.containsKey(SERVER_KEY)) {
            map.putMapValue(USER_KEY, SERVER_KEY, args.get(SERVER_KEY));
        }
    }

    /**
     * Returns the name of the user.
     *
     * <pre>
     *     user "test1", password: "test1password", server: "srv1"
     * </pre>
     *
     * @return the user {@link String} name.
     */
    public String getName() {
        return statementsMap.value(USER_KEY);
    }

    /**
     * Returns the user password.
     *
     * <pre>
     *     user "test1", password: "test1password", server: "srv1"
     * </pre>
     *
     * @return the user {@link String} password or {@code null}.
     */
    public String getPassword() {
        return statementsMap.mapValue(USER_KEY, PASSWORD_KEY);
    }

    /**
     * Returns on which server host the user can connect.
     *
     * <pre>
     *     user "test1", password: "test1password", server: "srv1"
     * </pre>
     *
     * @return the server {@link String} name or {@code null}.
     */
    public String getServer() {
        return statementsMap.mapValue(USER_KEY, SERVER_KEY);
    }

    /**
     * Returns the user access for databases.
     *
     * @return the {@link List} of the user access or {@code null}.
     */
    public List<String> getAccess() {
        return statementsMap.mapMultiValue(ACCESS_KEY, DATABASE_KEY);
    }

    public Object methodMissing(String name, Object args) {
        return statementsMap.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, getName()).toString();
    }

}
