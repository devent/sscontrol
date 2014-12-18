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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;

/**
 * Parses arguments for debug logging.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DebugLoggingArgs {

    private static final String LEVEL = "level";

    private static final String MODULE = "module";

    private static final String MODULES = "modules";

    @Inject
    private DebugLoggingLogger log;

    @Inject
    private StringToListFactory toListFactory;

    boolean haveModule(Map<String, Object> args) {
        return args.containsKey(MODULE);
    }

    String module(Map<String, Object> args) {
        Object module = args.get(MODULE);
        log.checkModule(module);
        return module.toString();
    }

    boolean haveModules(Map<String, Object> args) {
        return args.containsKey(MODULES);
    }

    List<Object> modules(Map<String, Object> args) {
        Object modules = args.get(MODULES);
        log.checkModules(modules);
        return toListFactory.create(modules.toString()).getList();
    }

    boolean haveLevel(Map<String, Object> args) {
        return args.containsKey(LEVEL);
    }

}
