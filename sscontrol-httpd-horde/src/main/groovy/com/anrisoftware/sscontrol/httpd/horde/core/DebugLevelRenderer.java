/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.horde.core;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;

/**
 * Attribute renderer for {@link DebugLogging}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLevelRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return toString(o);
    }

    public String toString(Object o) {
        Integer level = (Integer) o;
        switch (level) {
        case 0:
            return "EMERGENCY";
        case 1:
            return "ALERT";
        case 2:
            return "CRITICAL";
        case 3:
            return "ERROR";
        case 4:
            return "WARNING";
        case 5:
            return "NOTICE";
        case 6:
            return "INFO";
        default:
            return "DEBUG";
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return DebugLogging.class;
    }

}
