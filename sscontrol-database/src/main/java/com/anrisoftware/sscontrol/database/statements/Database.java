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

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.statementsmap.StatementsMapFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Defines the database name, character set and collate.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Database implements Serializable {

    private static final String NAME = "name";
    private static final String IMPORTING_KEY = "importing";
    private static final String SCRIPT_KEY = "script";
    private static final String DATABASE_KEY = "database";
    private static final String COLLATE_KEY = "collate";
    private static final String CHARSET_KEY = "charset";

    private final DatabaseLogger log;

    private final StatementsMap statementsMap;

    /**
     * @see DatabaseFactory#create(Map)
     */
    @Inject
    Database(DatabaseLogger log, StatementsMapFactory statementsMapFactory,
            @Assisted Map<String, Object> args) {
        this.log = log;
        this.statementsMap = createStatementsMap(statementsMapFactory);
        setupArgs(statementsMap, args);
    }

    private StatementsMap createStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, DATABASE_KEY);
        map.addAllowed(DATABASE_KEY, SCRIPT_KEY);
        map.setAllowValue(true, DATABASE_KEY);
        map.addAllowedKeys(DATABASE_KEY, CHARSET_KEY, COLLATE_KEY);
        map.addAllowedKeys(SCRIPT_KEY, IMPORTING_KEY);
        map.addAllowedMultiKeys(SCRIPT_KEY, IMPORTING_KEY);
        return map;
    }

    private void setupArgs(StatementsMap map, Map<String, Object> args) {
        map.putValue(DATABASE_KEY, log.name(this, args));
        if (args.containsKey(CHARSET_KEY)) {
            map.putMapValue(DATABASE_KEY, CHARSET_KEY, args.get(CHARSET_KEY));
        }
        if (args.containsKey(COLLATE_KEY)) {
            map.putMapValue(DATABASE_KEY, COLLATE_KEY, args.get(COLLATE_KEY));
        }
    }

    /**
     * Returns the name of the database.
     *
     * <pre>
     * database {
     *     database "postfixdb"
     * }
     * </pre>
     *
     * @return the database {@link String} name.
     */
    public String getName() {
        return statementsMap.value(DATABASE_KEY);
    }

    /**
     * Returns the default character set for the database.
     *
     * <pre>
     * database {
     *     database "postfixdb", charset: "latin1"
     * }
     * </pre>
     *
     * @return the character set {@link String} name or {@code null}.
     */
    public String getCharacterSet() {
        return statementsMap.mapValue(DATABASE_KEY, CHARSET_KEY);
    }

    /**
     * Returns the default collate set for the database.
     *
     * <pre>
     * database {
     *     database "postfixdb", collate: "latin1_swedish_ci"
     * }
     * </pre>
     *
     * @return the collate {@link String} name or {@code null}.
     */
    public String getCollate() {
        return statementsMap.mapValue(DATABASE_KEY, COLLATE_KEY);
    }

    /**
     * Returns the script resources to import into the database.
     *
     * <pre>
     * database {
     *     database "postfixdb", {
     *         script importing: "postfixtables.sql"
     *         script importing: "postfixtables.sql.gz"
     *     }
     * }
     * </pre>
     *
     * @return the script {@link URI} resources {@link List} list or
     *         {@code null}.
     */
    public List<URI> getScriptImportings() {
        return statementsMap.mapMultiValueAsURI(SCRIPT_KEY, IMPORTING_KEY);
    }

    public Object methodMissing(String name, Object args) {
        return statementsMap.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, getName()).toString();
    }

}
