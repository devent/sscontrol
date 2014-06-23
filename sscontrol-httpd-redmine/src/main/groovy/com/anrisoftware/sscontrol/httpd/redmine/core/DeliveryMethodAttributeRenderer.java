package com.anrisoftware.sscontrol.httpd.redmine.core;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.httpd.redmine.DeliveryMethod;

/**
 * @see DeliveryMethod
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DeliveryMethodAttributeRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        DeliveryMethod method = (DeliveryMethod) o;
        switch (method) {
        case smtp:
            return ":smtp";
        case sendmail:
            return ":sendmail";
        case async_smtp:
            return ":async_smtp";
        case async_sendmail:
            return ":async_sendmail";
        default:
            return null;
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return DeliveryMethod.class;
    }

}
