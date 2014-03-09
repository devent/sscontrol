/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

    private static final String SERVICE = "service";

    private final Object service;

    private final String name;

    private final Map<String, Object> args;

    private final Set<String> allowed;

    @Inject
    private StatementsMapLogger log;

    @Inject
    private StringToListFactory toListFactory;

    @Inject
    StatementsMap(@Assisted Object service, @Assisted String name) {
        this.service = service;
        this.name = name;
        this.args = new HashMap<String, Object>();
        this.allowed = new HashSet<String>();
    }

    /**
     * Adds allowed script statements.
     *
     * @param names
     *            the array with script statement {@link String} names.
     */
    public void addAllowed(String... names) {
        allowed.addAll(Arrays.asList(names));
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
        return (T) args.get(name);
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
        Map<String, Object> map = value(name);
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
        return toListFactory.create(value(name)).getList();
    }

    @SuppressWarnings("rawtypes")
    public Object methodMissing(String name, Object args)
            throws ServiceException {
        log.checkName(allowed, name);
        List list = InvokerHelper.asList(args);
        Object value = list;
        if (list.size() == 1) {
            value = list.get(0);
        }
        this.args.put(name, value);
        log.statementValueAdded(this, name, value);
        return null;
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
