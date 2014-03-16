/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.RequireDomainLogger._.domain_null;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link RequireDomain}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RequireDomainLogger extends AbstractLogger {

    private static final String DOMAIN_PLACEHOLDER = "%";
    private static final String DOMAIN = "domain";

    enum _ {

        domain_null("Domain cannot be null or blank for %s.");

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
     * Sets the context of the logger to {@link RequireDomain}.
     */
    public RequireDomainLogger() {
        super(RequireDomain.class);
    }

    String domain(AbstractAuthService service, Map<String, Object> args) {
        Object domain = args.get(DOMAIN);
        notNull(domain, domain_null.toString(), service);
        notBlank(domain.toString(), domain_null.toString(), service);
        String str = domain.toString();
        return replace(str, DOMAIN_PLACEHOLDER, service.getDomain().getName());
    }
}
