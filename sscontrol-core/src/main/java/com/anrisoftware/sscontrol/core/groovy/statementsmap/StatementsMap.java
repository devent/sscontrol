/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy.statementsmap;

import groovy.lang.GString;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.anrisoftware.globalpom.resources.ConvertException;
import com.anrisoftware.globalpom.resources.ToURI;
import com.anrisoftware.globalpom.resources.ToURIFactory;
import com.anrisoftware.sscontrol.core.listproperty.PropertyToListFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Stored allowed script statements.
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
public class StatementsMap implements Serializable {

    static final String NAME_KEY = "name";

    private static final String SERVICE = "service";

    private final Object service;

    private final String name;

    private final Map<String, Map<String, Object>> args;

    private final Map<String, Set<String>> allowed;

    private final Map<String, Map<String, Boolean>> multiValued;

    @Inject
    private StatementsMapLogger log;

    @Inject
    private PropertyToListFactory toListFactory;

    @Inject
    private ToURIFactory toURIFactory;

    /**
     * @see StatementsMapFactory#create(Object, String)
     */
    @Inject
    StatementsMap(@Assisted Object service, @Assisted String name) {
        this.service = service;
        this.name = name;
        this.args = new HashMap<String, Map<String, Object>>();
        this.allowed = new HashMap<String, Set<String>>();
        this.multiValued = new HashMap<String, Map<String, Boolean>>();
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
            multiValued.put(name, new HashMap<String, Boolean>());
        }
    }

    /**
     * Adds allowed script statements.
     *
     * <pre>
     * statement "value", key: "value"
     * </pre>
     *
     * @param names
     *            the array with script statement {@link Enum} names.
     */
    public void addAllowed(Enum<?>... names) {
        addAllowed(convert(names));
    }

    /**
     * Sets the statement have a value as the first argument.
     *
     * <pre>
     * statement "value"
     * </pre>
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
     * <pre>
     * statement "value"
     * </pre>
     *
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     *
     * @param names
     *            the statement {@link Enum} names.
     *
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowValue(boolean haveName, Enum<?>... names) {
        setAllowValue(haveName, convert(names));
    }

    /**
     * Sets the statement have a multi-value as the first argument.
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
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
    public void setAllowMultiValue(boolean haveName, String... names) {
        for (String name : names) {
            setAllowMultiValue(name, haveName);
        }
    }

    /**
     * Sets the statement have a multi-value as the first argument.
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
     *
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     *
     * @param names
     *            the statement {@link Enum} names.
     *
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowMultiValue(boolean haveName, Enum<?>... names) {
        setAllowMultiValue(haveName, convert(names));
    }

    /**
     * Sets the statement have a value as the first argument.
     *
     * <pre>
     * statement "value"
     * </pre>
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
        Map<String, Boolean> multi = multiValued.get(name);
        if (haveName) {
            set.add(NAME_KEY);
            multi.put(NAME_KEY, false);
        } else {
            set.remove(NAME_KEY);
            multi.remove(NAME_KEY);
        }
    }

    /**
     * Sets the statement have a value as the first argument.
     *
     * <pre>
     * statement "value"
     * </pre>
     *
     * @param name
     *            the statement {@link Enum} name.
     *
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     *
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowValue(Enum<?> name, boolean haveName) {
        setAllowValue(name.toString(), haveName);
    }

    /**
     * Sets the statement have a value as the first argument.
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
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
    public void setAllowMultiValue(String name, boolean haveName) {
        Set<String> set = allowed.get(name);
        Map<String, Boolean> multi = multiValued.get(name);
        if (haveName) {
            set.add(NAME_KEY);
            multi.put(NAME_KEY, true);
        } else {
            set.remove(NAME_KEY);
            multi.remove(NAME_KEY);
        }
    }

    /**
     * Sets the statement have a value as the first argument.
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
     *
     * @param name
     *            the statement {@link Enum} name.
     *
     * @param haveName
     *            set to {@code true} if the statement have a value as the first
     *            argument.
     *
     * @see #value(String)
     * @see #valueAsList(String)
     */
    public void setAllowMultiValue(Enum<?> name, boolean haveName) {
        setAllowMultiValue(name.toString(), haveName);
    }

    /**
     * Adds allowed statement keys.
     *
     * <pre>
     * statement key: "value"
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
        addAllowedKeys(name, false, keys);
    }

    /**
     * Adds allowed statement keys.
     *
     * <pre>
     * statement key: "value"
     * </pre>
     *
     * @param name
     *            the statement {@link Enum} name.
     *
     * @param keys
     *            the array with allowed {@link Enum} keys.
     *
     * @see #mapValue(String, String)
     */
    public void addAllowedKeys(Enum<?> name, Enum<?>... keys) {
        addAllowedKeys(name.toString(), convert(keys));
    }

    /**
     * Adds allowed statement keys with multi-values.
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
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
    public void addAllowedMultiKeys(String name, String... keys) {
        addAllowedKeys(name, true, keys);
    }

    /**
     * Adds allowed statement keys with multi-values.
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
     * </pre>
     *
     * @param name
     *            the statement {@link Enum} name.
     *
     * @param keys
     *            the array with allowed {@link Enum} keys.
     *
     * @see #mapValue(String, String)
     */
    public void addAllowedMultiKeys(Enum<?> name, Enum<?>... keys) {
        addAllowedMultiKeys(name.toString(), convert(keys));
    }

    /**
     * Adds allowed statement keys.
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
     * </pre>
     *
     * @param name
     *            the statement {@link String} name.
     *
     * @param isMulti
     *            set to {@code true} for multi-values.
     *
     * @param keys
     *            the array with allowed {@link String} keys.
     *
     * @see #mapValue(String, String)
     */
    public void addAllowedKeys(String name, boolean isMulti, String... keys) {
        Set<String> set = allowed.get(name);
        Map<String, Boolean> multi = multiValued.get(name);
        set.addAll(Arrays.asList(keys));
        for (String key : keys) {
            multi.put(key, isMulti);
        }
    }

    /**
     * Adds allowed statement keys.
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
     * </pre>
     *
     * @param name
     *            the statement {@link Enum} name.
     *
     * @param isMulti
     *            set to {@code true} for multi-values.
     *
     * @param keys
     *            the array with allowed {@link Enum} keys.
     *
     * @see #mapValue(String, String)
     */
    public void addAllowedKeys(Enum<?> name, boolean isMulti, Enum<?>... keys) {
        addAllowedKeys(name.toString(), isMulti, convert(keys));
    }

    /**
     * Checks if the key is allowed statement.
     *
     * @param name
     *            the {@link String} name of the key.
     *
     * @return {@code true} if the key is allowed.
     */
    public boolean isAllowedKey(String name) {
        return allowed.containsKey(name);
    }

    /**
     * Checks if the key is allowed statement.
     *
     * @param name
     *            the {@link Enum} name of the key.
     *
     * @return {@code true} if the key is allowed.
     */
    public boolean isAllowedKey(Enum<?> name) {
        return isAllowedKey(name.toString());
    }

    /**
     * Returns the statement value with the specified name.
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement "value"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @return the {@link Object} value or {@code null}.
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
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement "value"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @return the {@link Object} value or {@code null}.
     */
    public <T> T value(Enum<?> name) {
        return value(name.toString());
    }

    /**
     * Returns the statement value with the specified name as a list.
     * <p>
     *
     * The following statement returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @return the {@link List} list or {@code null}.
     */
    public List<Object> valueAsList(String name) {
        Object value = value(name);
        if (value != null) {
            return toListFactory.create(value).getList();
        } else {
            return null;
        }
    }

    /**
     * Returns the statement value with the specified name as a list.
     * <p>
     *
     * The following statement returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @return the {@link List} list or {@code null}.
     */
    public List<Object> valueAsList(Enum<?> name) {
        return valueAsList(name.toString());
    }

    /**
     * Returns the statement value with the specified name as a list.
     * <p>
     *
     * The following statement returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @return the {@link List} list or {@code null}.
     */
    public List<String> valueAsStringList(String name) {
        Object value = value(name);
        if (value != null) {
            List<Object> list = toListFactory.create(value).getList();
            List<String> res = new ArrayList<String>(list.size());
            for (Object object : list) {
                res.add(object.toString());
            }
            return res;
        } else {
            return null;
        }
    }

    /**
     * Returns the statement value with the specified name as a list.
     * <p>
     *
     * The following statement returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @return the {@link List} list or {@code null}.
     */
    public List<String> valueAsStringList(Enum<?> name) {
        return valueAsStringList(name.toString());
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @return the {@link List} values.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> multiValue(String name) {
        Map<String, Object> map = args.get(name);
        if (map != null) {
            return (List<T>) map.get(NAME_KEY);
        } else {
            return null;
        }
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns ["value1", "value2"]:
     *
     * <pre>
     * statement "value1"
     * statement "value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @return the {@link List} values.
     */
    public <T> List<T> multiValue(Enum<?> name) {
        return multiValue(name.toString());
    }

    /**
     * Returns the statement value with the specified name.
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement key: "value"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link Object} value or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> T mapValue(String name, String key) {
        Map<String, Object> map = args.get(name);
        return map == null ? null : (T) map.get(key);
    }

    /**
     * Returns the statement value with the specified name.
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement key: "value"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link Object} value or {@code null}.
     */
    public <T> T mapValue(Enum<?> name, Enum<?> key) {
        return mapValue(name.toString(), key.toString());
    }

    /**
     * Returns the statement value with the specified name as URI.
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement key: "http://address.com/file"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link URI} or {@code null}.
     *
     * @throws ConvertException
     *             if there were errors converting the path to the URI.
     */
    public URI mapValueAsURI(String name, String key) {
        Map<String, Object> map = args.get(name);
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        return value == null ? null : toURIFactory.create().convert(
                value.toString());
    }

    /**
     * Returns the statement value with the specified name as URI.
     * <p>
     *
     * The following statement returns "value":
     *
     * <pre>
     * statement key: "http://address.com/file"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link URI} or {@code null}.
     *
     * @throws ConvertException
     *             if there were errors converting the path to the URI.
     */
    public URI mapValueAsURI(Enum<?> name, Enum<?> key) {
        return mapValueAsURI(name.toString(), key.toString());
    }

    /**
     * Returns the statement values with the specified name.
     * <p>
     *
     * The following statement returns {@code ["value1", "value2"]}
     *
     * <pre>
     * statement key: "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link List} values.
     */
    public List<Object> mapValueAsList(String name, String key) {
        Object value = mapValue(name, key);
        if (value != null) {
            return toListFactory.create(value).getList();
        } else {
            return null;
        }
    }

    /**
     * Returns the statement values with the specified name.
     * <p>
     *
     * The following statement returns {@code ["value1", "value2"]}
     *
     * <pre>
     * statement key: "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link List} values.
     */
    public List<Object> mapValueAsList(Enum<?> name, Enum<?> key) {
        return mapValueAsList(name.toString(), key.toString());
    }

    /**
     * Returns the statement values with the specified name.
     * <p>
     *
     * The following statement returns {@code ["value1", "value2"]}
     *
     * <pre>
     * statement key: "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link List} values.
     */
    public List<String> mapValueAsStringList(String name, String key) {
        Object value = mapValue(name, key);
        if (value != null) {
            List<Object> list = toListFactory.create(value).getList();
            List<String> res = new ArrayList<String>(list.size());
            for (Object object : list) {
                res.add(object.toString());
            }
            return res;
        } else {
            return null;
        }
    }

    /**
     * Returns the statement values with the specified name.
     * <p>
     *
     * The following statement returns {@code ["value1", "value2"]}
     *
     * <pre>
     * statement key: "value1, value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link List} values.
     */
    public List<String> mapValueAsStringList(Enum<?> name, Enum<?> key) {
        return mapValueAsStringList(name.toString(), key.toString());
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns ["value1", "value2"]:
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link List} values or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> mapMultiValue(String name, String key) {
        Map<String, Object> map = args.get(name);
        return map == null ? null : (List<T>) map.get(key);
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns ["value1", "value2"]:
     *
     * <pre>
     * statement key: "value1"
     * statement key: "value2"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link List} values or {@code null}.
     */
    public <T> List<T> mapMultiValue(Enum<?> name, Enum<?> key) {
        return mapMultiValue(name.toString(), key.toString());
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns [URI("/foo.txt"), URI("/bar.txt")]:
     *
     * <pre>
     * statement key: "file:///foo.txt"
     * statement key: "file:///bar.txt"
     * </pre>
     *
     * @param name
     *            the {@link String} name.
     *
     * @param key
     *            the {@link String} key.
     *
     * @return the {@link List} of {@link URI} resources or {@code null}.
     */
    @SuppressWarnings("unchecked")
    public List<URI> mapMultiValueAsURI(String name, String key) {
        Map<String, Object> map = args.get(name);
        if (map == null) {
            return null;
        }
        List<URI> res = new ArrayList<URI>();
        List<Object> list = (List<Object>) map.get(key);
        ToURI touri = toURIFactory.create();
        for (Object object : list) {
            res.add(touri.convert(object));
        }
        return res;
    }

    /**
     * Returns the statement multi-value with the specified name.
     * <p>
     *
     * The following statements returns [URI("/foo.txt"), URI("/bar.txt")]:
     *
     * <pre>
     * statement key: "file:///foo.txt"
     * statement key: "file:///bar.txt"
     * </pre>
     *
     * @param name
     *            the {@link Enum} name.
     *
     * @param key
     *            the {@link Enum} key.
     *
     * @return the {@link List} of {@link URI} resources or {@code null}.
     */
    public List<URI> mapMultiValueAsURI(Enum<?> name, Enum<?> key) {
        return mapMultiValueAsURI(name.toString(), key.toString());
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
     * @throws StatementsException
     *             if the statement is now allowed.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void putMapValue(String name, String key, Object value)
            throws StatementsException {
        log.checkName(allowed, name);
        Set<String> allowedKeys = allowed.get(name);
        Map<String, Boolean> multi = multiValued.get(name);
        log.checkKey(this, allowedKeys, name, key);
        Map<String, Object> map = getArgsMap(name);
        if (value instanceof GString) {
            value = value.toString();
        }
        if (multi.get(key) == true) {
            Object oldValue = map.get(key);
            if (oldValue == null) {
                oldValue = new ArrayList();
                map.put(key, oldValue);
            }
            if (oldValue instanceof List) {
                List list = (List) oldValue;
                if (value instanceof List) {
                    list.addAll((List) value);
                } else {
                    list.add(value);
                }
            }
        } else {
            map.put(key, value);
        }
        log.statementValueAdded(this, name, key, value);
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
     * @throws StatementsException
     *             if the statement is now allowed.
     */
    public void putValue(String name, Object value) throws StatementsException {
        putMapValue(name, NAME_KEY, value);
    }

    /**
     * Put the statement value into the map.
     *
     * @throws StatementsException
     *             if the statement is not allowed for the map.
     */
    public Object methodMissing(String name, Object obj)
            throws StatementsException {
        @SuppressWarnings("rawtypes")
        List list = InvokerHelper.asList(obj);
        if (list.size() == 1) {
            if (list.get(0) instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) list.get(0);
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    putMapValue(name, entry.getKey(), value);
                }
            } else {
                putValue(name, list.get(0));
            }
        }
        if (list.size() == 2) {
            putValue(name, list.get(1));
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) list.get(0);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                putMapValue(name, entry.getKey(), entry.getValue());
            }
        }
        return null;
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

    private String[] convert(Enum<?>[] names) {
        String[] snames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            snames[i] = names[i].toString();
        }
        return snames;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service.toString())
                .toString();
    }

}
