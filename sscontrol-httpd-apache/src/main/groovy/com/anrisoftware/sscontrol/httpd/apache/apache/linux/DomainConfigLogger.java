package com.anrisoftware.sscontrol.httpd.apache.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.apache.linux.DomainConfigLogger._.ref_null;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging for {@link DomainConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DomainConfigLogger extends AbstractLogger {

    enum _ {

        ref_null("User referenced domain '%s' not found for %s.");

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
     * Sets the context of the logger to {@link DomainConfig}.
     */
    public DomainConfigLogger() {
        super(DomainConfig.class);
    }

    void checkRef(Domain domain, Domain ref, String refname) {
        notNull(ref, ref_null.toString(), refname, domain);
    }
}
