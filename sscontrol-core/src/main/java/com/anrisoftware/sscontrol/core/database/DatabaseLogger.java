/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.database;

import static com.anrisoftware.sscontrol.core.database.DatabaseLogger._.encoding_null;
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

        provider_null("Database provider cannot be null or blank for %s."),

        encoding_null("Database encoding cannot be null or blank for %s.");

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

    void checkEncoding(Object service, Object encoding) {
        notNull(encoding, encoding_null.toString(), service);
        notBlank(encoding.toString(), encoding_null.toString(), service);
    }
}
