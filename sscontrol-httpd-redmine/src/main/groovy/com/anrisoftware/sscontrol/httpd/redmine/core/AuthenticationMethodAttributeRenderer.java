package com.anrisoftware.sscontrol.httpd.redmine.core;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.httpd.redmine.AuthenticationMethod;

/**
 * @see AuthenticationMethod
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AuthenticationMethodAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        AuthenticationMethod method = (AuthenticationMethod) o;
        switch (method) {
        case none:
            return "nil";
        case plain:
            return ":plain";
        case login:
            return ":login";
        case cram_md5:
            return ":cram_md5";
        default:
            return null;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return AuthenticationMethod.class;
    }

}
