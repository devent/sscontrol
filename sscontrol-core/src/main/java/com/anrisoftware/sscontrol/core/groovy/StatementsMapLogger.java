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

import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.allowed_name;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.invalid_statement;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.invalid_statement_message;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.key_not_allowed;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.key_not_allowed_message;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.name_value_error;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.name_value_error_message;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.statement_added_debug;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.statement_added_info;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.statement_key;
import static com.anrisoftware.sscontrol.core.groovy.StatementsMapLogger._.statements;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.resources.texts.api.TextsFactory;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link StatementsMap}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StatementsMapLogger extends AbstractLogger {

    enum _ {

        invalid_statement,

        allowed_name,

        invalid_statement_message,

        statement_added_debug,

        statement_added_info,

        statements,

        name_value_error,

        name_value_error_message,

        key_not_allowed,

        statement_key,

        key_not_allowed_message;

        public static void loadTexts(TextsFactory factory) {
            String name = StatementsMapLogger.class.getSimpleName();
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
     * Sets the context of the logger to {@link StatementsMap}.
     */
    public StatementsMapLogger() {
        super(StatementsMap.class);
    }

    @Inject
    public void loadTexts(TextsFactory factory) {
        _.loadTexts(factory);
    }

    void checkName(Map<String, Set<String>> allowed, String name)
            throws ServiceException {
        if (!allowed.containsKey(name)) {
            throw logException(new ServiceException(invalid_statement).add(
                    allowed_name, name), invalid_statement_message, name);
        }
    }

    void statementValueAdded(StatementsMap statements, String name, Object value) {
        if (isDebugEnabled()) {
            debug(statement_added_debug, name, value, statements);
        } else {
            info(statement_added_info, name, value, statements.getName());
        }
    }

    void checkNameValueAllowed(StatementsMap map, Set<String> allowedKeys,
            String name) throws ServiceException {
        if (allowedKeys.contains(StatementsMap.NAME_KEY)) {
            return;
        }
        throw logException(
                new ServiceException(name_value_error).add(statements, map)
                        .add(allowed_name, name), name_value_error_message,
                name, map.getName());
    }

    void checkKey(StatementsMap map, Set<String> allowedKeys, String name,
            String key) throws ServiceException {
        if (allowedKeys.contains(key)) {
            return;
        }
        throw logException(
                new ServiceException(key_not_allowed).add(statements, map)
                        .add(allowed_name, name).add(statement_key, key),
                key_not_allowed_message, key, name, map.getName());
    }
}
