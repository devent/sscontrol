package com.anrisoftware.sscontrol.httpd.nginx.linux.nginx;

import java.util.Locale;

import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;

/**
 * Debug logging renderer.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLoggingRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String formatString, Locale locale) {
        if (o instanceof DebugLogging) {
            return toString((DebugLogging) o);
        }
        return null;
    }

    private String toString(DebugLogging debug) {
        StringBuilder builder = new StringBuilder();
        builder.append(debug.getArgs().get("storage"));
        builder.append(" ").append(loggingLevel(debug));
        return builder.toString();
    }

    private Object loggingLevel(DebugLogging debug) {
        switch (debug.getLevel()) {
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
        case 7:
            return "debug";
        default:
            return "debug";
        }
    }

    @Override
    public Class<?> getAttributeType() {
        return DebugLogging.class;
    }

}
