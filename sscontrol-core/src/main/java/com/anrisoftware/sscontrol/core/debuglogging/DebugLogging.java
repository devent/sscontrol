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
package com.anrisoftware.sscontrol.core.debuglogging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Debug logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLogging extends Number {

    private static final String LEVEL = "level";

    private static final String MODULES = "modules";

    private final Map<String, Object> args;

    /**
     * @see DebugLoggingFactory#createOff()
     */
    @AssistedInject
    DebugLogging() {
        this.args = new HashMap<String, Object>();
        setLevel(0);
    }

    /**
     * @see DebugLoggingFactory#create(int)
     */
    @AssistedInject
    DebugLogging(@Assisted int level) {
        this();
        setLevel(level);
    }

    /**
     * @see DebugLoggingFactory#create(Map)
     */
    @AssistedInject
    DebugLogging(DebugLoggingArgs aargs, @Assisted Map<String, Object> args) {
        this.args = new HashMap<String, Object>(args);
        if (!aargs.haveLevel(args)) {
            setLevel(0);
        }
        if (aargs.haveModule(args)) {
            setModule(aargs.module(args));
        }
        if (aargs.haveModules(args)) {
            setModules(aargs.modules(args));
        }
    }

    public void setLevel(int level) {
        args.put(LEVEL, level);
    }

    public int getLevel() {
        Object level = args.get(LEVEL);
        if (level instanceof Number) {
            return ((Number) level).intValue();
        } else {
            return Integer.valueOf(args.get(LEVEL).toString());
        }
    }

    public void setModule(String module) {
        List<String> modules = new ArrayList<String>();
        modules.add(module);
        setModules(modules);
    }

    public void setModules(List<String> modules) {
        args.put(MODULES, modules);
    }

    @SuppressWarnings("unchecked")
    public List<String> getModules() {
        return (List<String>) args.get(MODULES);
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    @Override
    public int intValue() {
        return getLevel();
    }

    @Override
    public long longValue() {
        return getLevel();
    }

    @Override
    public float floatValue() {
        return getLevel();
    }

    @Override
    public double doubleValue() {
        return getLevel();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(args).toString();
    }
}
