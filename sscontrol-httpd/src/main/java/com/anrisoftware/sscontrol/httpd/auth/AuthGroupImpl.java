/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.EXCEPT_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.NAME_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.REQUIRE_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.UPDATE_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.USER_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthGroupStatement.VALID_KEY;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Authentication group.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthGroupImpl implements AuthGroup {

    private static final String NAME = "group";

    private final AuthService service;

    private StatementsMap statementsMap;

    private StatementsTable statementsTable;

    /**
     * @see AuthGroupFactory#create(AuthService, Map)
     */
    @Inject
    AuthGroupImpl(@Assisted AuthService service) {
        this.service = service;
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory,
            @Assisted Map<String, Object> args) {
        StatementsMap map = factory.create(this, NAME);
        map.addAllowed(NAME_KEY, UPDATE_KEY, REQUIRE_KEY);
        map.setAllowValue(true, NAME_KEY, UPDATE_KEY);
        map.addAllowedKeys(REQUIRE_KEY, VALID_KEY, EXCEPT_KEY);
        map.putValue(NAME_KEY.toString(), args.get(NAME_KEY.toString()));
        map.putValue(UPDATE_KEY.toString(), args.get(UPDATE_KEY.toString()));
        this.statementsMap = map;
    }

    @Inject
    public final void setStatementsTable(StatementsTableFactory factory,
            @Assisted Map<String, Object> args) {
        StatementsTable table = factory.create(this, NAME);
        table.addAllowed(USER_KEY);
        table.addAllowedKeys(USER_KEY, PASSWORD_KEY, UPDATE_KEY);
        this.statementsTable = table;
    }

    public AuthService getService() {
        return service;
    }

    @Override
    public String getName() {
        return statementsMap.value(NAME_KEY);
    }

    @Override
    public UpdateMode getUpdate() {
        return statementsMap.value(UPDATE_KEY);
    }

    @Override
    public RequireValid getRequireValid() {
        return statementsMap.mapValue(REQUIRE_KEY, VALID_KEY);
    }

    @Override
    public List<String> getRequireExcept() {
        return statementsMap.mapValueAsStringList(REQUIRE_KEY, EXCEPT_KEY);
    }

    @Override
    public Map<String, Object> getUserPasswords() {
        return statementsTable.tableKeys(USER_KEY, PASSWORD_KEY);
    }

    @Override
    public Map<String, Object> getUserUpdates() {
        return statementsTable.tableKeys(USER_KEY, UPDATE_KEY);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

    /**
     * Redirects to the statements map and table.
     *
     * @see StatementsMap#methodMissing(String, Object)
     * @see StatementsTable#methodMissing(String, Object)
     */
    public Object methodMissing(String name, Object args)
            throws StatementsException {
        try {
            statementsMap.methodMissing(name, args);
        } catch (StatementsException e) {
            statementsTable.methodMissing(name, args);
        }
        return null;
    }

}
