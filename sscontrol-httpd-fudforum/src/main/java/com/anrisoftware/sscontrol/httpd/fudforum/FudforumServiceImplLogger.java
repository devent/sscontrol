/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum;

import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceImplLogger._.error_parse_language;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceImplLogger._.error_parse_language_message;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceImplLogger._.error_parse_locale;
import static com.anrisoftware.sscontrol.httpd.fudforum.FudforumServiceImplLogger._.error_parse_locale_message;

import java.text.ParseException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging for {@link FudforumServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FudforumServiceImplLogger extends AbstractLogger {

    enum _ {

        error_parse_language("Error parse language"),

        error_parse_language_message("Error parse language for '{}': {}"),

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
     * Sets the context of the logger to {@link FudforumServiceImpl}.
     */
    public FudforumServiceImplLogger() {
        super(FudforumServiceImpl.class);
    }

    ServiceException errorParseLanguage(FudforumServiceImpl service,
            ParseException e) {
        return logException(new ServiceException(error_parse_language, e),
                error_parse_language_message, service.getName(),
                e.getLocalizedMessage());
    }

    ServiceException errorParseLocales(FudforumServiceImpl service,
            ParseException e) {
        return logException(new ServiceException(error_parse_locale, e),
                error_parse_locale_message, service.getName(),
                e.getLocalizedMessage());
    }
}
