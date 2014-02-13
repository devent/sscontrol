package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2.AuthFileBasicConfigLogger._.htpasswd_args_missing;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthFileBasicConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileBasicConfigLogger extends AbstractLogger {

    private static final String USER = "user";
    private static final String COMMAND = "command";

    enum _ {

        htpasswd_args_missing("Htpasswd argument '%s' missing for %s.");

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
     * Sets the context of the logger to {@link AuthFileBasicConfig}.
     */
    public AuthFileBasicConfigLogger() {
        super(AuthFileBasicConfig.class);
    }

    void checkHtpasswdArgs(Object script, Map<String, Object> args) {
        isTrue(args.containsKey(COMMAND), htpasswd_args_missing.toString(),
                COMMAND, script);
        isTrue(args.containsKey(USER), htpasswd_args_missing.toString(), USER,
                script);
    }

}
