package com.anrisoftware.sscontrol.core.api;

/**
 * Pre-script that can configure the script compile before the script is loaded.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServicePreScript {

    /**
     * Configures the specified compiler.
     * 
     * @param compiler
     *            the compiler.
     */
    void configureCompiler(Object compiler);

}
