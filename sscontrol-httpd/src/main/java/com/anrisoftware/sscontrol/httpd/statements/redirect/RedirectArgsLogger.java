package com.anrisoftware.sscontrol.httpd.statements.redirect;

import static com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectArgsLogger._.destination_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging for {@link RedirectArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedirectArgsLogger extends AbstractLogger {

    enum _ {

        destination_null("Redirect destination cannot be null for %s.");

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
     * Sets the context of the logger to {@link RedirectArgs}.
     */
    public RedirectArgsLogger() {
        super(RedirectArgs.class);
    }

    void checkTo(Domain domain, Object to) {
        notNull(to, destination_null.toString(), domain);
    }
}
