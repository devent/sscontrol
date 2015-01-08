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
package com.anrisoftware.sscontrol.httpd.citadel.spamassassin;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;

/**
 * Renders the report safe mode.
 */
@SuppressWarnings("serial")
public class ReportSafeModeAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        return toString((ReportSafeMode) o);
    }

    private String toString(ReportSafeMode o) {
        switch (o) {
        case off:
            return "0";
        case mimeAttachement:
            return "1";
        case plainAttachement:
            return "2";
        }
        return null;
    }

    @Override
    public Class<?> getAttributeType() {
        return ReportSafeMode.class;
    }

}
