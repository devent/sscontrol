/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel.citadel_ubuntu;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.httpd.citadel.AuthMethod;

/**
 * @see AuthMethod
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AuthMethodAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return toString((AuthMethod) o);
    }

    private String toString(AuthMethod auth) {
        switch (auth) {
        case selfContained:
            return "0";
        case hostSystem:
            return "1";
        case ldap:
            return "2";
        case ldapNonstandard:
            return "3";
        default:
            return null;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return AuthMethod.class;
    }

}
