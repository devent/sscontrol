package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.httpd.auth.RequireValidMode;

/**
 * Atttribute renderer for {@link RequireValidMode}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class RequireValidModeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        RequireValidMode mode = (RequireValidMode) o;
        switch (mode) {
        case valid_user:
            return "requireValidUser";
        default:
            return null;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return RequireValidMode.class;
    }

}
