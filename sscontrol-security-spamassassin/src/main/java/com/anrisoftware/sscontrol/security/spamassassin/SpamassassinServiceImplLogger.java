/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-spamassassin.
 *
 * sscontrol-security-spamassassin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-spamassassin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-spamassassin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.spamassassin;

import static com.anrisoftware.sscontrol.security.spamassassin.SpamassassinServiceImplLogger._.add_header_added_debug;
import static com.anrisoftware.sscontrol.security.spamassassin.SpamassassinServiceImplLogger._.add_header_added_info;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link SpamassassinServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SpamassassinServiceImplLogger extends AbstractLogger {

    enum _ {

        add_header_added_debug("Header {} to add added to {}."),

        add_header_added_info("Header {} {} to add added to service '{}'.");

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
     * Sets the context of the logger to {@link SpamassassinServiceImpl}.
     */
    public SpamassassinServiceImplLogger() {
        super(SpamassassinServiceImpl.class);
    }

    void addHeaderAdded(SpamassassinServiceImpl service, Header header) {
        if (isDebugEnabled()) {
            debug(add_header_added_debug, header, service);
        } else {
            debug(add_header_added_info, header.getMessageType(),
                    header.getName(), service.getName());
        }
    }
}
