/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
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
    DebugLogging defaultDebug(Object script, String profile = "default_debug") {
        def str = script.profileProperty profile, script.defaultProperties
        def args = str.split(",").inject([:]) { acc, val ->
            def nameAndValue = val.split(":")
            acc[nameAndValue[0].trim()] = nameAndValue[1].trim()
            acc
        }
        debugLoggingFactory.create args
    }
}
