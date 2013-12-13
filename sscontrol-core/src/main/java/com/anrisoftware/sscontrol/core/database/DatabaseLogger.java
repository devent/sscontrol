package com.anrisoftware.sscontrol.core.database;

import static com.anrisoftware.sscontrol.core.database.DatabaseLogger._.provider_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link Database}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DatabaseLogger extends AbstractLogger {

    enum _ {

        provider_null("Database provider cannot be null or blank for %s.");

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
     * Sets the context of the logger to {@link Database}.
     */
    public DatabaseLogger() {
        super(Database.class);
    }

    void checkProvider(Object service, Object provider) {
        notNull(provider, provider_null.toString(), service);
        notBlank(provider.toString(), provider_null.toString(), service);
    }
}
