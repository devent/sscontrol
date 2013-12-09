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

import static com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingLogger._.level_null;
import static com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingLogger._.level_number;
import static com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingLogger._.module_null;
import static com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingLogger._.modules_null;
import static org.apache.commons.lang3.Validate.isInstanceOf;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link DebugLogging}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DebugLoggingLogger extends AbstractLogger {

    enum _ {

        level_null("Logging cannot be null."),

        level_number("Logging must be an integer number."),

        module_null("Module name cannot be null or blank."),

        modules_null("Module names cannot be null or blank.");

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
     * Creates a logger for {@link DebugLogging}.
     */
    public DebugLoggingLogger() {
        super(DebugLogging.class);
    }

    void checkLevel(Object object) {
        notNull(object, level_null.toString());
        isInstanceOf(Integer.class, object, level_number.toString());
    }

    void checkModule(Object module) {
        notNull(module, module_null.toString());
        notBlank(module.toString(), module_null.toString());
    }

    void checkModules(Object modules) {
        notNull(modules, modules_null.toString());
        notBlank(modules.toString(), modules_null.toString());
    }

}
