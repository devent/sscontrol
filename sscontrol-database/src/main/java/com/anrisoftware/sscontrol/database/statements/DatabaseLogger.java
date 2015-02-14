/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.database.statements;

import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.charset_null;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.collate_null;
import static com.anrisoftware.sscontrol.database.statements.DatabaseLogger._.name_empty;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Database}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DatabaseLogger extends AbstractLogger {

    private static final String COLLATE_KEY = "collate";
    private static final String CHARSET_KEY = "charset";
    private static final String NAME_KEY = "name";

    enum _ {

        name_empty("Name cannot be null or empty for %s."),

        collate_null("Collate must not be null for %s."),

        charset_null("Character set must not be null for %s.");

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
     * Create logger for {@link Database}.
     */
    DatabaseLogger() {
        super(Database.class);
    }

    String name(Database database, Map<String, Object> args) {
        isTrue(args.containsKey(NAME_KEY), name_empty.toString(), database);
        String v = args.get(NAME_KEY).toString();
        notBlank(v, name_empty.toString(), database);
        return v;
    }

    String charset(Database database, Map<String, Object> args) {
        isTrue(args.containsKey(CHARSET_KEY), charset_null.toString(), database);
        String v = args.get(CHARSET_KEY).toString();
        checkCharacterSet(database, v);
        return v;
    }

    String collate(Database database, Map<String, Object> args) {
        isTrue(args.containsKey(COLLATE_KEY), collate_null.toString(), database);
        String v = args.get(COLLATE_KEY).toString();
        notBlank(v, charset_null.toString(), database);
        return v;
    }

    void checkCharacterSet(Database database, String character) {
        notBlank(character, charset_null.toString(), database);
    }

    void checkCollate(Database database, String collate) {
        notBlank(collate, collate_null.toString(), database);
    }

}
