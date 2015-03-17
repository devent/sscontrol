/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.binding_set_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.binding_set_info;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.caching_null;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.caching_set_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.caching_set_info;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.gc_null;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.idle_gc_set_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.idle_gc_set_info;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.override_mode_null;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.override_mode_set_debug;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.override_mode_set_info;
import static com.anrisoftware.sscontrol.httpd.gitit.GititServiceLogger._.repository_type_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging messages for {@link GititService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class GititServiceLogger extends AbstractLogger {

    private static final String GC = "gc";
    private static final String ENABLED = "enabled";
    private static final String TYPE = "type";
    private static final String MODE = "mode";

    enum _ {

        binding_set_debug("Binding address {} set {}."),

        binding_set_info("Binding address {} set for service '{}'."),

        debug_set_debug("Debug logging {} set for {}."),

        debug_set_info("Debug logging level {} set for service '{}'."),

        override_mode_null("Override mode cannot be null for %s."),

        override_mode_set_debug("Override mode {} set for {}."),

        override_mode_set_info("Override mode {} set for service '{}'."),

        repository_type_null("Repository type cannot be null for %s."),

        caching_null("Caching cannot be null for %s."),

        caching_set_debug("Caching {} set for {}."),

        caching_set_info("Caching {} set for service '{}'."),

        gc_null("Idle garbage collection cannot be null for %s."),

        idle_gc_set_debug("Idle garbage collection {} set for {}."),

        idle_gc_set_info("Idle garbage collection {} set for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates a logger for {@link GititService}.
     */
    public GititServiceLogger() {
        super(GititService.class);
    }

    void debugSet(WebService service, DebugLogging debug) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, debug, service);
        } else {
            info(debug_set_info, debug.getLevel(), service.getName());
        }
    }

    OverrideMode override(WebService service, Map<String, Object> args) {
        Object mode = args.get(MODE);
        notNull(mode, override_mode_null.toString(), service);
        if (mode instanceof OverrideMode) {
            return (OverrideMode) mode;
        } else {
            return OverrideMode.valueOf(mode.toString());
        }
    }

    void overrideModeSet(GititService service, OverrideMode mode) {
        if (isDebugEnabled()) {
            debug(override_mode_set_debug, mode, service);
        } else {
            info(override_mode_set_info, mode, service.getName());
        }
    }

    boolean haveType(Map<String, Object> args) {
        return args.containsKey(TYPE);
    }

    RepositoryType type(GititService service, Map<String, Object> args) {
        Object type = args.get(TYPE);
        notNull(type, repository_type_null.toString(), service);
        if (type instanceof RepositoryType) {
            return (RepositoryType) type;
        } else {
            return RepositoryType.valueOf(type.toString());
        }
    }

    boolean caching(GititService service, Map<String, Object> args) {
        Object caching = args.get(ENABLED);
        notNull(caching, caching_null.toString(), service);
        return YesNoFlag.valueOf(caching);
    }

    void cachingSet(GititService service, boolean caching) {
        if (isDebugEnabled()) {
            debug(caching_set_debug, caching, service);
        } else {
            info(caching_set_info, caching, service.getName());
        }
    }

    boolean idleGc(GititService service, Map<String, Object> args) {
        Object gc = args.get(GC);
        notNull(gc, gc_null.toString(), service);
        return YesNoFlag.valueOf(gc);
    }

    void idleGcSet(GititService service, boolean gc) {
        if (isDebugEnabled()) {
            debug(idle_gc_set_debug, gc, service);
        } else {
            info(idle_gc_set_info, gc, service.getName());
        }
    }

    void bindingSet(GititService service, Binding binding) {
        if (isDebugEnabled()) {
            debug(binding_set_debug, binding, service);
        } else {
            info(binding_set_info, binding.getAddresses(), service.getName());
        }
    }

}
