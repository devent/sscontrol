package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import static com.anrisoftware.sscontrol.httpd.statements.roundcube.HostLogger._.host_blank;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Host}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostLogger extends AbstractLogger {

    enum _ {

        message("message"),

        host_blank("Host cannot be null or empty for service %s.");

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
     * Sets the context of the logger to {@link Host}.
     */
    public HostLogger() {
        super(Host.class);
    }

    void checkHost(Object service, Object hostname) {
        notNull(hostname, host_blank.toString(), service);
        notBlank(hostname.toString(), host_blank.toString(), service);
    }
}
