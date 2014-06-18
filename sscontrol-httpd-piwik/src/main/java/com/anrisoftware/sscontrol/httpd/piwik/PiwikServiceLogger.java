/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik;

import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceLogger._.debug_set_debug;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceLogger._.debug_set_info;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceLogger._.override_mode_null;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceLogger._.override_mode_set_debug;
import static com.anrisoftware.sscontrol.httpd.piwik.PiwikServiceLogger._.override_mode_set_info;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging messages for {@link PiwikService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PiwikServiceLogger extends AbstractLogger {

    private static final String MODE = "mode";

    enum _ {

        debug_set_debug("Debug logging {} set for {}."),

        debug_set_info("Debug logging level {} set for service '{}'."),

        override_mode_null("Override mode cannot be null for %s."),

        override_mode_set_debug("Override mode {} set for {}."),

        override_mode_set_info("Override mode {} set for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates a logger for {@link PiwikService}.
     */
    public PiwikServiceLogger() {
        super(PiwikService.class);
    }

    void debugSet(WebService service, DebugLogging debug) {
        if (isDebugEnabled()) {
            debug(debug_set_debug, debug, service);
        } else {
            info(debug_set_info, debug.getLevel(), service.getName());
        }
    }

    OverrideMode override(WebService service, Map<String, Object> args) {
        Object mode = args.get(MODE);
        notNull(mode, override_mode_null.toString(), service);
        if (mode instanceof OverrideMode) {
            return (OverrideMode) mode;
        } else {
            return OverrideMode.valueOf(mode.toString());
        }
    }

    void overrideModeSet(PiwikService service, OverrideMode mode) {
        if (isDebugEnabled()) {
            debug(override_mode_set_debug, mode, service);
        } else {
            info(override_mode_set_info, mode, service.getName());
        }
    }


}
