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
package com.anrisoftware.sscontrol.httpd.authdb;

import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.ALLOW_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.AUTHORITATIVE_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.CHARSET_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.DATABASE_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.DRIVER_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.EMPTY_PASSWORDS_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.ENCRYPTION_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.FIELD_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.PORT_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.SOCKET_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.TABLE_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.TYPE_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.USERS_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.USER_KEY;
import static com.anrisoftware.sscontrol.httpd.authdb.AuthDbServiceStatement.USER_NAME_KEY;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.google.inject.assistedinject.Assisted;

/**
 * Database authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthDbService extends AbstractAuthService {

    /**
     * Database authentication service name.
     */
    public static final String AUTH_DB_NAME = "auth-db";

    private StatementsMap statementsMap;

    /**
     * @see AuthDbServiceFactory#create(Map, Domain)
     */
    @Inject
    AuthDbService(@Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(AUTH_DB_NAME, args, domain);
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, AUTH_DB_NAME);
        map.addAllowed(TYPE_KEY, DATABASE_KEY, USERS_KEY, FIELD_KEY, ALLOW_KEY);
        map.setAllowValue(true, TYPE_KEY, DATABASE_KEY);
        map.addAllowedKeys(TYPE_KEY, AUTHORITATIVE_KEY);
        map.addAllowedKeys(DATABASE_KEY, USER_KEY, PASSWORD_KEY, HOST_KEY,
                PORT_KEY, SOCKET_KEY, CHARSET_KEY, DRIVER_KEY, ENCRYPTION_KEY);
        map.addAllowedKeys(USERS_KEY, TABLE_KEY);
        map.addAllowedKeys(FIELD_KEY, USER_NAME_KEY, PASSWORD_KEY);
        map.addAllowedKeys(ALLOW_KEY, EMPTY_PASSWORDS_KEY);
        this.statementsMap = map;
    }

    /**
     * Returns whether authorization and authentication are passed to lower
     * level modules.
     *
     * @return {@code true} if enabled or {@code null}.
     */
    public Boolean getAuthoritative() {
        Object value = statementsMap.mapValue(TYPE_KEY, AUTHORITATIVE_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    /**
     * Returns the database properties.
     *
     * <ul>
     * <li>{@code database} the database name;</li>
     * <li>{@code user} the database user name;</li>
     * <li>{@code password} the user password;</li>
     * <li>{@code host} the database host;</li>
     * <li>{@code port} the database port;</li>
     * <li>{@code socket} the database socket;</li>
     * <li>{@code charset} the database character set;</li>
     * <li>{@code driver} the database driver;</li>
     * <li>{@code encryption} the password encryption;</li>
     * </ul>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-db", auth: "Private Directory", location: "/private", {
     *             database "authdb", user: "userdb", password: "userpassdb", host: "localhost", driver: "mysql"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link Map} of the database properties or {@code null}.
     */
    public Map<String, Object> getDatabase() {
        @SuppressWarnings("serial")
        Map<String, Object> map = new HashMap<String, Object>() {
            @Override
            public Object put(String key, Object value) {
                if (value != null) {
                    return super.put(key, value);
                } else {
                    return null;
                }
            }
        };
        StatementsMap m = statementsMap;
        map.put(DATABASE_KEY.toString(), m.value(DATABASE_KEY));
        map.put(USER_KEY.toString(), m.mapValue(DATABASE_KEY, USER_KEY));
        map.put(PASSWORD_KEY.toString(), m.mapValue(DATABASE_KEY, PASSWORD_KEY));
        map.put(HOST_KEY.toString(), m.mapValue(DATABASE_KEY, HOST_KEY));
        map.put(PORT_KEY.toString(), m.mapValue(DATABASE_KEY, PORT_KEY));
        map.put(SOCKET_KEY.toString(), m.mapValue(DATABASE_KEY, SOCKET_KEY));
        map.put(CHARSET_KEY.toString(), m.mapValue(DATABASE_KEY, CHARSET_KEY));
        map.put(DRIVER_KEY.toString(), m.mapValue(DATABASE_KEY, DRIVER_KEY));
        map.put(ENCRYPTION_KEY.toString(),
                m.mapValueAsStringList(DATABASE_KEY, ENCRYPTION_KEY));
        return map.size() == 0 ? null : map;
    }

    /**
     * Returns the name of the users table.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-db", auth: "Private Directory", location: "/private", {
     *             users table: "users"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} name or {@code null}.
     */
    public String getUsersTable() {
        return statementsMap.mapValue(USERS_KEY, TABLE_KEY);
    }

    /**
     * Returns the name of the user name field.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-db", auth: "Private Directory", location: "/private", {
     *             field userName: "username"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} name or {@code null}.
     */
    public String getUserNameField() {
        return statementsMap.mapValue(FIELD_KEY, USER_NAME_KEY);
    }

    /**
     * Returns the name of the password field.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-db", auth: "Private Directory", location: "/private", {
     *             field password: "passwd"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} name or {@code null}.
     */
    public String getPasswordField() {
        return statementsMap.mapValue(FIELD_KEY, PASSWORD_KEY);
    }

    /**
     * Returns allows empty passwords.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-db", auth: "Private Directory", location: "/private", {
     *             allow emptyPasswords: no
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} name or {@code null}.
     */
    public Boolean getAllowEmptyPasswords() {
        Object value = statementsMap.mapValue(ALLOW_KEY, EMPTY_PASSWORDS_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    @Override
    public Object methodMissing(String name, Object args) {
        try {
            return super.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsMap.methodMissing(name, args);
        }
    }

}
