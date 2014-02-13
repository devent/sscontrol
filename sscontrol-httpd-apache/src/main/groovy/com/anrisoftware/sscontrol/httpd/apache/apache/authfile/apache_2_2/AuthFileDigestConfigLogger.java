package com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2;

import static com.anrisoftware.sscontrol.httpd.apache.apache.authfile.apache_2_2.AuthFileDigestConfigLogger._.digest_password_args_missing;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AuthFileBasicConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthFileDigestConfigLogger extends AbstractLogger {

    private static final String USER = "user";
    private static final String AUTH = "auth";

    enum _ {

        digest_password_args_missing(
                "Digest password argument '%s' missing for %s.");

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
    public AuthFileDigestConfigLogger() {
        super(AuthFileBasicConfig.class);
    }

    void checkDigestPasswordArgs(Object script, Map<String, Object> args) {
        isTrue(args.containsKey(AUTH), digest_password_args_missing.toString(),
                AUTH, script);
        isTrue(args.containsKey(USER), digest_password_args_missing.toString(),
                USER, script);
    }

}
