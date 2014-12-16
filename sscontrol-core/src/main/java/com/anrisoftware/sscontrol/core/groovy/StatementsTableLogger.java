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

import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.allowed_name;
import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.invalid_statement;
import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.key_not_allowed;
import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.name_value_error;
import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.statement_key;
import static com.anrisoftware.sscontrol.core.groovy.StatementsTableLogger._.statement_table_added;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;

/**
 * Logging for {@link StatementsTable}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatementsTableLogger extends AbstractLogger {

    enum _ {

        invalid_statement,

        allowed_name,

        invalid_statement_message,

        statement_table_added,

        statements,

        name_value_error,

        name_value_error_message,

        key_not_allowed,

        statement_key,

        key_not_allowed_message;

        public static void loadTexts(TextsFactory factory) {
            String name = StatementsTableLogger.class.getSimpleName();
            Texts texts = factory.create(name);
            for (_ value : values()) {
                value.text = texts.getResource(value.name()).getText();
            }
        }

        private String text;

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Sets the context of the logger to {@link StatementsTable}.
     */
    public StatementsTableLogger() {
        super(StatementsTable.class);
    }

    @Inject
    public void loadTexts(TextsFactory factory) {
        _.loadTexts(factory);
    }

    void checkName(Map<String, Set<String>> allowed, String name)
            throws StatementsException {
        if (!allowed.containsKey(name)) {
            throw new StatementsException(invalid_statement).add(allowed_name,
                    name);
        }
    }

    void statementValueAdded(StatementsTable statements, String name,
            String table, Object key, Object value) {
        debug(statement_table_added, name, table, key, value, statements);
    }

    void checkNameValueAllowed(StatementsTable statements,
            Set<String> allowedKeys, String name) throws StatementsException {
        if (allowedKeys.contains(StatementsMap.NAME_KEY)) {
            return;
        }
        throw new StatementsException(name_value_error).add(statements,
                statements).add(allowed_name, name);
    }

    void checkKey(StatementsTable statements, Set<String> allowedKeys,
            String name, String key) throws StatementsException {
        if (allowedKeys.contains(key)) {
            return;
        }
        throw new StatementsException(key_not_allowed)
                .add(statements, statements).add(allowed_name, name)
                .add(statement_key, key);
    }
}
