package com.anrisoftware.sscontrol.httpd.apache.linux.wordpress;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link BaseWordpressConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BaseWordpressConfigLogger extends AbstractLogger {

    enum _ {

        message("message");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    BaseWordpressConfigLogger() {
        super(BaseWordpressConfig.class);
    }
}
