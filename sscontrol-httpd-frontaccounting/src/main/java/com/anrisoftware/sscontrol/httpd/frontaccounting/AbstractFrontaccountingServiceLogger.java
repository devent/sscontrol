/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting;

import static com.anrisoftware.sscontrol.httpd.frontaccounting.AbstractFrontaccountingServiceLogger._.error_parse_locale;
import static com.anrisoftware.sscontrol.httpd.frontaccounting.AbstractFrontaccountingServiceLogger._.error_parse_locale_message;

import java.text.ParseException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link AbstractFrontaccountingService}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractFrontaccountingServiceLogger extends AbstractLogger {

    enum _ {

        error_parse_locale("Error parse locale"),

        error_parse_locale_message("Error parse locale for '{}': {}");

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
     * Sets the context of the logger to {@link AbstractFrontaccountingService}.
     */
    public AbstractFrontaccountingServiceLogger() {
        super(AbstractFrontaccountingService.class);
    }

    ServiceException errorParseLocales(AbstractFrontaccountingService service,
            ParseException e) {
        return logException(new ServiceException(error_parse_locale, e),
                error_parse_locale_message, service.getName(),
                e.getLocalizedMessage());
    }
}
