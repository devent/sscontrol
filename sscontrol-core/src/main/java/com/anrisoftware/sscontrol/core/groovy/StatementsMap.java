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
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Stored allowed script statements.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class StatementsMap implements Serializable {

    static final String NAME_KEY = "name";

    private static final String SERVICE = "service";

    private final Object service;

    private final String name;

    private final Map<String, Map<String, Object>> args;

    private final Map<String, Set<String>> allowed;

    @Inject
    private StatementsMapLogger log;

    @Inject
    private StringToListFactory toListFactory;

    @Inject
    StatementsMap(@Assisted Object service, @Assisted String name) {
        this.service = service;
        this.name = name;
        this.args = new HashMap<String, Map<String, Object>>();
        this.allowed = new HashMap<String, Set<String>>();
    }

    /**
     * Adds allowed script statements.
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
     * Sets the statement have a value as the first argument.
     * 
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     * 
     * @param names
     *            the statement {@link String} names.
     * 
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowValue(boolean haveName, String... names) {
        for (String name : names) {
            setAllowValue(name, haveName);
        }
    }

    /**
     * Sets the statement have a value as the first argument.
     * 
     * @param name
     *            the statement {@link String} name.
     * 
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     * 
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowValue(String name, boolean haveName) {
        Set<String> set = allowed.get(name);
        if (haveName) {
            set.add(NAME_KEY);
        } else {
            set.remove(NAME_KEY);
        }
    }

    /**
     * Adds allowed statement keys.
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
     * Returns the statement value with the specified name.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @return the {@link Object} value.
     */
    @SuppressWarnings("unchecked")
    public <T> T value(String name) {
        Map<String, Object> map = args.get(name);
        if (map != null) {
            return (T) map.get(NAME_KEY);
        } else {
            return null;
        }
    }

    /**
     * Returns the statement value with the specified name.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @param key
     *            the {@link String} key.
     * 
     * @return the {@link Object} value.
     */
    @SuppressWarnings("unchecked")
    public <T> T mapValue(String name, String key) {
        Map<String, Object> map = args.get(name);
        return map == null ? null : (T) map.get(key);
    }

    /**
     * Returns the statement value with the specified name as a list.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @return the {@link List} list.
     */
    public List<String> valueAsList(String name) {
        Object value = value(name);
        if (value != null) {
            return toListFactory.create(value).getList();
        } else {
            return null;
        }
    }

    /**
     * Puts the statement value with the specified name.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @param key
     *            the {@link String} key.
     * 
     * @param value
     *            the {@link Object} value.
     * 
     * @throws ServiceException
     *             if the statement is now allowed.
     */
    public void putMapValue(String name, String key, Object value)
            throws ServiceException {
        log.checkName(allowed, name);
        Set<String> allowedKeys = allowed.get(name);
        log.checkKey(this, allowedKeys, name, key);
        Map<String, Object> map = getArgsMap(name);
        map.put(key, value);
    }

    /**
     * Puts the statement value with the specified name.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @param value
     *            the {@link Object} value.
     * 
     * @throws ServiceException
     *             if the statement is now allowed.
     */
    public void putValue(String name, Object value) throws ServiceException {
        log.checkName(allowed, name);
        Set<String> allowedKeys = allowed.get(name);
        log.checkNameValueAllowed(this, allowedKeys, name);
        Map<String, Object> map = getArgsMap(name);
        map.put(NAME_KEY, value);
    }

    public Object methodMissing(String name, Object obj)
            throws ServiceException {
        log.checkName(allowed, name);
        @SuppressWarnings("rawtypes")
        List list = InvokerHelper.asList(obj);
        Set<String> allowedKeys = allowed.get(name);
        Map<String, Object> map = getArgsMap(name);
        if (list.size() == 1) {
            if (list.get(0) instanceof Map) {
                putMapValues(name, list, allowedKeys, map);
            } else {
                log.checkNameValueAllowed(this, allowedKeys, name);
                putNameValue(list, map);
            }
        }
        args.put(name, map);
        log.statementValueAdded(this, name, map);
        return null;
    }

    private void putNameValue(List<?> list, Map<String, Object> map) {
        Object value;
        value = list.get(0);
        map.put(NAME_KEY, value);
    }

    private void putMapValues(String name, List<?> list,
            Set<String> allowedKeys, Map<String, Object> map)
            throws ServiceException {
        @SuppressWarnings("unchecked")
        Map<String, Object> vmap = (Map<String, Object>) list.get(0);
        for (Map.Entry<String, Object> entry : vmap.entrySet()) {
            log.checkKey(this, allowedKeys, name, entry.getKey());
            map.put(entry.getKey(), entry.getValue());
        }
    }

    private Map<String, Object> getArgsMap(String name) {
        Map<String, Object> map = args.get(name);
        if (map == null) {
            map = new HashMap<String, Object>();
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
