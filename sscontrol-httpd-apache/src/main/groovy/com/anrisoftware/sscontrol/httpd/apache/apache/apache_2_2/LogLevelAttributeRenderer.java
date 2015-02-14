/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.apache.apache_2_2;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Formats the debug error level.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LogLevelAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (!StringUtils.equals(formatString, "loglevel")) {
            return String.valueOf(o);
        }
        int number = ((Number) o).intValue();
        switch (number) {
        case 0:
            return "emerg";
        case 1:
            return "alert";
        case 2:
            return "crit";
        case 3:
            return "error";
        case 4:
            return "warn";
        case 5:
            return "notice";
        case 6:
            return "info";
        default:
            return "debug";
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return Integer.class;
    }

}
