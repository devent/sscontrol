/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.core.groovy.statementsmap;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.split;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Returns the statements key name from the statement enumeration constant.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public final class StatementsEnumToString {

    private static final String EMPTY = "";

    private static final String SEPERATOR = "_";

    /**
     * Returns the statements key name from the statement enumeration constant.
     *
     * @param statementsEnum
     *            the enumeration {@link Enum} constant.
     *
     * @return the statement key {@link String} name.
     */
    public static String toString(Enum<?> statementsEnum) {
        String[] split = split(lowerCase(statementsEnum.name()), SEPERATOR);
        for (int i = 1; i < split.length - 1; i++) {
            split[i] = WordUtils.capitalize(split[i]);
        }
        String name = join(split, EMPTY, 0, split.length - 1);
        return name;
    }

}
