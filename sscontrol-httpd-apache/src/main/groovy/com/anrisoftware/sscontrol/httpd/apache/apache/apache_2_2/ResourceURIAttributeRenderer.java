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

import java.io.File;
import java.net.URI;
import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Formats the resource {@link URI}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ResourceURIAttributeRenderer implements AttributeRenderer {

    private static final String FILE_NAME_FORMAT_NAME = "fileName";
    private static final String FILE_FORMAT_NAME = "file";

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (FILE_FORMAT_NAME.equals(formatString)) {
            return toString((URI) o);
        }
        if (FILE_NAME_FORMAT_NAME.equals(formatString)) {
            return fileName((URI) o);
        }
        return null;
    }

    private String fileName(URI o) {
        return new File(o).getName();
    }

    private String toString(URI o) {
        return new File(o).getAbsolutePath();
    }

    @Override
    public Class<?> getAttributeType() {
        return URI.class;
    }

}
