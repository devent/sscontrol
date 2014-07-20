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
