/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

    private static final String MODULE = "module";

    private static final String LEVEL = "level";

    private static final String MODULES = "modules";

    private final Map<String, Object> args;

    @AssistedInject
    DebugLogging() {
        this.args = new HashMap<String, Object>();
        setLevel(0);
    }

    @AssistedInject
    DebugLogging(@Assisted int level) {
        this();
        setLevel(level);
    }

    @AssistedInject
    DebugLogging(DebugLoggingArgs aargs, @Assisted Map<String, Object> args) {
        this();
        setLevel(aargs.level(args));
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
        return (Integer) args.get(LEVEL);
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
