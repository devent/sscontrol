/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.user;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Local user.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class User {

    private static final String SERVICE = "service";
    private static final String LOGIN_KEY = "login";
    private static final String COMMENT_KEY = "comment";
    private static final String GID_KEY = "gid";
    private static final String GROUP_KEY = "group";
    private static final String KEY_KEY = "key";
    private static final String ACCESS_KEY = "access";
    private static final String REQUIRE_KEY = "require";
    private static final String HOME_KEY = "home";
    private static final String PASSPHRASE_KEY = "passphrase";
    private static final String UID_KEY = "uid";
    private static final String PASSWORD_KEY = "password";
    private static final String USER_KEY = "user";

    private final Service service;

    private final StatementsMap statementsMap;

    /**
     * @see UserFactory#create(Service, Map)
     */
    @Inject
    User(StatementsMapFactory statementsMapFactory, @Assisted Service service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.statementsMap = setupStatemetsMap(args, statementsMapFactory);
    }

    private StatementsMap setupStatemetsMap(Map<String, Object> args,
            StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, USER_KEY);
        map.addAllowed(USER_KEY, PASSWORD_KEY, UID_KEY, PASSPHRASE_KEY,
                HOME_KEY, REQUIRE_KEY, ACCESS_KEY, GROUP_KEY, COMMENT_KEY,
                LOGIN_KEY);
        map.setAllowValue(true, USER_KEY, PASSPHRASE_KEY, HOME_KEY,
                REQUIRE_KEY, GROUP_KEY, COMMENT_KEY, LOGIN_KEY);
        map.setAllowMultiValue(true, REQUIRE_KEY);
        map.addAllowedKeys(USER_KEY, PASSWORD_KEY, UID_KEY);
        map.addAllowedKeys(ACCESS_KEY, KEY_KEY);
        map.addAllowedKeys(GROUP_KEY, GID_KEY);
        map.addAllowedMultiKeys(ACCESS_KEY, KEY_KEY);
        map.putValue(USER_KEY, args.get(USER_KEY));
        if (args.containsKey(PASSWORD_KEY)) {
            map.putMapValue(USER_KEY, PASSWORD_KEY, args.get(PASSWORD_KEY));
        }
        if (args.containsKey(UID_KEY)) {
            map.putMapValue(USER_KEY, UID_KEY, args.get(UID_KEY));
        }
        return map;
    }

    /**
     * Returns the user name.
     *
     * <pre>
     * user "foo", password: "foopass", {
     * }
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
     * user "foo", password: "foopass", {
     * }
     * </pre>
     *
     * @return the user {@link String} password.
     */
    public String getPassword() {
        return statementsMap.mapValue(USER_KEY, PASSWORD_KEY);
    }

    /**
     * Returns the user ID.
     *
     * <pre>
     * user "foo", password: "foopass", uid: 99, {
     * }
     * </pre>
     *
     * @return the user {@link Integer} ID or {@code null}.
     */
    public Integer getUid() {
        return statementsMap.mapValue(USER_KEY, UID_KEY);
    }

    /**
     * Returns the user group name.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      group "foogroup"
     * }
     * </pre>
     *
     * @return the user group {@link String} name or {@code null}.
     */
    public String getGroup() {
        return statementsMap.value(GROUP_KEY);
    }

    /**
     * Returns the user group ID.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      group "foogroup", gid: 99
     * }
     * </pre>
     *
     * @return the user group {@link Integer} ID or {@code null}.
     */
    public Integer getGroupId() {
        return statementsMap.mapValue(GROUP_KEY, GID_KEY);
    }

    /**
     * Returns the user comment.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      comment "Foo User"
     * }
     * </pre>
     *
     * @return the user {@link String} comment or {@code null}.
     */
    public String getComment() {
        return statementsMap.value(COMMENT_KEY);
    }

    /**
     * Returns the user home directory.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      home "/var/foo"
     * }
     * </pre>
     *
     * @return the user home {@link String} directory or {@code null}.
     */
    public String getHome() {
        Object value = statementsMap.value(HOME_KEY);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * Returns the user log-in.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      login "/bin/sh"
     * }
     * </pre>
     *
     * @return the user {@link String} log-in or {@code null}.
     */
    public String getLogin() {
        return statementsMap.value(LOGIN_KEY);
    }

    /**
     * Returns the user passphrase.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      passphrase "passphrase"
     * }
     * </pre>
     *
     * @return the user {@link String} passphrase or {@code null}.
     */
    public String getPassphrase() {
        return statementsMap.value(PASSPHRASE_KEY);
    }

    /**
     * Returns the user requires statements.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      require password, passphrase
     * }
     * </pre>
     *
     * @return the user {@link List} {@link Require} requires or {@code null}.
     */
    public List<Require> getRequires() {
        List<Require> list = new ArrayList<Require>();
        List<Object> values = statementsMap.valueAsList(REQUIRE_KEY);
        if (values == null) {
            return null;
        }
        for (Object obj : values) {
            if (obj instanceof Require) {
                list.add((Require) obj);
            } else {
                list.add(Require.valueOf(obj.toString()));
            }
        }
        return list.size() == 0 ? null : list;
    }

    /**
     * Returns the user access keys.
     *
     * <pre>
     * user "foo", password: "foopass", {
     *      access key: "file:///foo.pub"
     * }
     * </pre>
     *
     * @return the user {@link List} {@link URI} keys or {@code null}.
     */
    public List<URI> getAccessKeys() {
        return statementsMap.mapMultiValueAsURI(ACCESS_KEY, KEY_KEY);
    }

    public void require(Object... s) {
    }

    public void password() {
        statementsMap.putValue(REQUIRE_KEY, Require.password);
    }

    public void passphrase() {
        statementsMap.putValue(REQUIRE_KEY, Require.passphrase);
    }

    public void access() {
        statementsMap.putValue(REQUIRE_KEY, Require.access);
    }

    public void home() {
        statementsMap.putValue(REQUIRE_KEY, Require.home);
    }

    public void login() {
        statementsMap.putValue(REQUIRE_KEY, Require.login);
    }

    public void comment() {
        statementsMap.putValue(REQUIRE_KEY, Require.comment);
    }

    public void uid() {
        statementsMap.putValue(REQUIRE_KEY, Require.uid);
    }

    public void group() {
        statementsMap.putValue(REQUIRE_KEY, Require.group);
    }

    public Object methodMissing(String name, Object args) {
        return statementsMap.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service).toString();
    }
}
