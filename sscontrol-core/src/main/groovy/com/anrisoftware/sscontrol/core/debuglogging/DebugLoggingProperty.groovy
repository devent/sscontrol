package com.anrisoftware.sscontrol.core.debuglogging;

import javax.inject.Inject

/**
 * Debug logging property.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DebugLoggingProperty {

    @Inject
    private DebugLoggingFactory debugLoggingFactory

    /**
     * Returns the default debug logging.
     *
     * <ul>
     * <li>profile property {@code "default_debug"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    DebugLogging defaultDebug(Object script) {
        def str = script.profileProperty "default_debug", script.defaultProperties
        def args = str.split(",").inject([:]) { acc, val ->
            def nameAndValue = val.split(":")
            acc[nameAndValue[0].trim()] = nameAndValue[1].trim()
            acc
        }
        debugLoggingFactory.create args
    }
}
