/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress;

import static java.lang.String.format;

/**
 * Type of the multi-site.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum MultiSite {

    none,

    subdir,

    subdomain;

    private static String no_value = "No value for string '%s'";

    /**
     * Parses the specified string to a value.
     * 
     * @param string
     *            the {@link String}.
     * 
     * @return the {@link MultiSite}.
     * 
     *         throws {@link IllegalArgumentException} if the specified string
     *         does not correspond to a value.
     */
    public static MultiSite parse(String string) {
        for (MultiSite value : values()) {
            if (value.toString().equals(string)) {
                return value;
            }
        }
        throw new IllegalArgumentException(format(no_value, string));
    }
}
