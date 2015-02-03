/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.groovy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Stored allowed script statements as table.
 * <p>
 *
 * <pre>
 * public Object methodMissing(String name, Object args)
 *         throws StatementsException {
 *     statementsMap.methodMissing(name, args);
 *     return null;
 * }
 *
 * </pre>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class StatementsTable implements Serializable {

    private static final String ARBITRARY_KEY = "_arbitrary";

    static final String NAME_KEY = "name";

    private static final String SERVICE = "service";

    private final Object service;

    private final String name;

    private final Map<String, Map<String, Map<String, Object>>> args;

    private final Map<String, Set<String>> allowed;

    @Inject
    private StatementsTableLogger log;

    @Inject
    private StringToListFactory toListFactory;

    /**
     * @see StatementsTableFactory#create(Object, String)
     */
    @Inject
    StatementsTable(@Assisted Object service, @Assisted String name) {
        this.service = service;
        this.name = name;
        this.args = new HashMap<String, Map<String, Map<String, Object>>>();
        this.allowed = new HashMap<String, Set<String>>();
    }

    /**
     * Adds allowed script statements.
     *
     * <pre>
     * statement "value", key: "value"
     * </pre>
     *
     * @param names
     *            the array with script statement {@link String} names.
     */
    public void addAllowed(String... names) {
        for (String name : names) {
            allowed.put(name, new HashSet<String>());
        }
    }

    /**
     * Adds allowed statement keys.
     *
     * <pre>
     * statement "name", key: "value"
     * </pre>
     *
     * @param name
     *            the statement {@link String} name.
     *
     * @param keys
     *            the array with allowed {@link String} keys.
     *
     * @see #mapValue(String, String)
     */
    public void addAllowedKeys(String name, String... keys) {
        Set<String> set = allowed.get(name);
        set.addAll(Arrays.asList(keys));
    }

    /**
     * Set allow arbitrary keys for the statements.
     *
     * <pre>
     * statement "name", foo: "value"
     * statement "name", bar: "value"
     * </pre>
     *
     * @param allow
     *            set to {@code true} to allow arbitrary keys.
     *
     * @param names
     *            the array with statement {@link String} names.
     *
     * @see #mapValue(String, String)
     */
    public void setAllowArbitraryKeys(boolean allow, String... names) {
        if (allow) {
            for (String name : names) {
                addAllowedKeys(name, ARBITRARY_KEY);
            }
        }
    }

    /**
     * Returns the statement value with the specified name.
     * <p>
     *
     * The following statement returns ["foo", "bar"]:
     *
     * <pre>
     * statement "foo"
     * statement "bar"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @return the {@link Set} of values or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> tableValues(String name) {
        Map<String, Map<String, Object>> map = args.get(name);
        if (map != null) {
            return (Set<T>) map.keySet();
        } else {
            return null;
        }
    }

    /**
     * Returns the statement table values with the specified name.
     * <p>
     *
     * The following statements returns the table for the key "keyFoo"
     * {@code ["foo": "value1", "bar": "value2"]}
     *
     * <pre>
     * statement "foo", keyFoo: "value1"
     * statement "bar", keyFoo: "value2", keyBar: "value3"
     * </pre>
     *
     * @param name
     *            the {@link String} statement name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link Object} value or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> tableKeys(String name, String key) {
        Map<String, Map<String, Object>> map = args.get(name);
        if (map == null) {
            return null;
        }
        Map<String, T> res = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> t : map.entrySet()) {
            Object keyValue = t.getValue().get(key);
            if (keyValue != null) {
                res.put(t.getKey(), (T) keyValue);
            }
        }
        return res.size() == 0 ? null : res;
    }

    /**
     * Returns the statement table values with the specified name.
     * <p>
     *
     * The following statements returns the table for the key "keyFoo"
     * {@code ["foo": ["value1"], "bar": ["value2", "value3"]]}
     *
     * <pre>
     * statement "foo", keyFoo: "value1"
     * statement "bar", keyFoo: "value2, value3", keyBar: "value4"
     * </pre>
     *
     * @param name
     *            the {@link String} statement name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link Object} value or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, List<T>> tableKeysAsList(String name, String key) {
        Map<String, Map<String, Object>> map = args.get(name);
        if (map == null) {
            return null;
        }
        List<T> list;
        Map<String, List<T>> res = new HashMap<String, List<T>>();
        for (Entry<String, Map<String, Object>> t : map.entrySet()) {
            Object keyValue = t.getValue().get(key);
            if (keyValue != null) {
                list = (List<T>) toListFactory.create(keyValue).getList();
                res.put(t.getKey(), list);
            }
        }
        return res.size() == 0 ? null : res;
    }

    /**
     * Puts the statement value with the specified name.
     *
     * @param name
     *            the {@link String} statement name.
     *
     * @param table
     *            the {@link String} table name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @param value
     *            the {@link Object} value.
     *
     * @throws StatementsException
     *             if the statement is now allowed.
     */
    public void putMapValue(String name, String table, String key, Object value)
            throws StatementsException {
        log.checkName(allowed, name);
        Set<String> allowedKeys = allowed.get(name);
        if (!allowedKeys.contains(ARBITRARY_KEY)) {
            log.checkKey(this, allowedKeys, name, key);
        }
        Map<String, Map<String, Object>> map = getArgsMap(name);
        map.get(table).put(key, value);
        log.statementValueAdded(this, name, table, key, value);
    }

    /**
     * Puts the statement value with the specified name.
     *
     * @param name
     *            the {@link String} name.
     *
     * @param table
     *            the {@link String} table name.
     *
     * @throws StatementsException
     *             if the statement is now allowed.
     */
    public void putTable(String name, String table) throws StatementsException {
        log.checkName(allowed, name);
        Map<String, Map<String, Object>> map = getArgsMap(name);
        if (!map.containsKey(table)) {
            map.put(table, new HashMap<String, Object>());
        }
    }

    public Object methodMissing(String name, Object obj)
            throws StatementsException {
        @SuppressWarnings("rawtypes")
        List list = InvokerHelper.asList(obj);
        if (list.size() == 2) {
            String table = list.get(1).toString();
            putTable(name, table);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) list.get(0);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                putMapValue(name, table, entry.getKey(), entry.getValue());
            }
        }
        if (list.size() == 1) {
            String table = list.get(0).toString();
            putTable(name, table);
        }
        return null;
    }

    private Map<String, Map<String, Object>> getArgsMap(String name) {
        Map<String, Map<String, Object>> map = args.get(name);
        if (map == null) {
            map = new HashMap<String, Map<String, Object>>();
            args.put(name, map);
        }
        return map;
    }

    public String getName() {
        return name;
    }

    public Object getService() {
        return service;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service.toString())
                .toString();
    }

}
