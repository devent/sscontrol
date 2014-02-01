package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.invalid_mode;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.invalid_mode_message;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.OverrideModeArgsLogger._.mode_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Logging for {@link OverrideModeArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class OverrideModeArgsLogger extends AbstractLogger {

    enum _ {

        mode_null("Override mode cannot be null for {}."),

        invalid_mode("Invalid override mode"),

        invalid_mode_message("Invalid override mode '{}' for {}.");

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
     * Sets the context of the logger to {@link OverrideModeArgs}.
     */
    public OverrideModeArgsLogger() {
        super(OverrideModeArgs.class);
    }

    void checkOverrideMode(WebService service, Object mode) {
        notNull(mode, mode_null.toString(), service);
    }

    IllegalArgumentException invalidOverrideMode(WebService service, Object mode) {
        return logException(
                new IllegalArgumentException(invalid_mode.toString()),
                invalid_mode_message, mode, service);
    }
}
