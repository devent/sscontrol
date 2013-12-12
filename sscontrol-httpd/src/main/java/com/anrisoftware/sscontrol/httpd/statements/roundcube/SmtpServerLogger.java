package com.anrisoftware.sscontrol.httpd.statements.roundcube;

import static com.anrisoftware.sscontrol.httpd.statements.roundcube.SmtpServerLogger._.host_null;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.SmtpServerLogger._.password_null;
import static com.anrisoftware.sscontrol.httpd.statements.roundcube.SmtpServerLogger._.user_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SmtpServer}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SmtpServerLogger extends AbstractLogger {

    enum _ {

        host_null("Smpt server host cannot be null or empty."),

        user_null("Smpt server user cannot be null or empty."),

        password_null("Smpt server password cannot be null or empty.");

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
     * Sets the context of the logger to {@link SmtpServer}.
     */
    public SmtpServerLogger() {
        super(SmtpServer.class);
    }

    void checkHost(Object service, Object host) {
        notNull(host, host_null.toString(), service);
        notBlank(host.toString(), host_null.toString(), service);
    }

    void checkUser(Object service, Object user) {
        notNull(user, user_null.toString(), service);
        notBlank(user.toString(), user_null.toString(), service);
    }

    void checkPassword(Object service, Object password) {
        notNull(password, password_null.toString(), service);
        notBlank(password.toString(), password_null.toString(), service);
    }
}
