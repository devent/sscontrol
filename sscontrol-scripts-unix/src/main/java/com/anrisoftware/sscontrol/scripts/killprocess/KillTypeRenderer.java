/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.killprocess;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * @see KillType
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class KillTypeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return toString((KillType) o);
    }

    private String toString(KillType o) {
        switch (o) {
        case KILL:
            return "9";
        case TERM:
            return "11";
        default:
            return null;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return KillType.class;
    }

}
