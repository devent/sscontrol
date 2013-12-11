package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube;

import static com.anrisoftware.sscontrol.httpd.apache.linux.roundcube.BaseRoundcubeConfigLogger._.database_config_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.apache.linux.apache.ApacheScript;

/**
 * Logging messages for {@link BaseRoundcubeConfig}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class BaseRoundcubeConfigLogger extends AbstractLogger {

    enum _ {

        database_config_null("Database config '%s' not found for %s.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    BaseRoundcubeConfigLogger() {
        super(BaseRoundcubeConfig.class);
    }

    void checkDatabaseConfig(ApacheScript script, Object config, String name) {
        notNull(config, database_config_null.toString(), name, script);
    }
}
