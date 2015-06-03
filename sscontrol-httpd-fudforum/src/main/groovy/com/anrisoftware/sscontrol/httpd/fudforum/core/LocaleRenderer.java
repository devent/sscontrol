/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum.core;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Attribute renderer for the locale.
 *
 * @see Locale
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LocaleRenderer implements AttributeRenderer {

    private static final String LANGUAGE_KEY = "language";

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return toString((Locale) o, formatString);
    }

    private String toString(Locale locale, String formatString) {
        if (StringUtils.equals(formatString, LANGUAGE_KEY)) {
            return locale.getLanguage();
        }
        return locale.toString();
    }

    @Override
    public Class<?> getAttributeType() {
        return Locale.class;
    }

}
