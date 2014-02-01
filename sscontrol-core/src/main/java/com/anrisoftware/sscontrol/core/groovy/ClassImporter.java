package com.anrisoftware.sscontrol.core.groovy;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

/**
 * Imports class for the <i>Groovy</i> compiler.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ClassImporter {

    /**
     * Imports the class using the specified <i>Groovy</i> import customizer.
     * 
     * @param customizer
     *            the {@link ImportCustomizer}.
     */
    void importClass(ImportCustomizer customizer);
}
