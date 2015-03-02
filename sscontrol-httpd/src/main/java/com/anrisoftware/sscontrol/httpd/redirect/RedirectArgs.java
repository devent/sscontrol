/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redirect;

import static com.anrisoftware.sscontrol.httpd.redirect.DomainPlaceholder.DOMAIN_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Parses redirect arguments.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class RedirectArgs {

    @Inject
    private RedirectArgsLogger log;

    String to(Domain domain, Map<String, Object> args) {
        Object to = args.get("to");
        log.checkTo(domain, to);
        return replacePlaceholder(to, domain);
    }

    String from(Domain domain, Map<String, Object> args) {
        Object from = args.get("from");
        if (from != null) {
            log.checkTo(domain, from);
            return replacePlaceholder(from, domain);
        } else {
            return null;
        }
    }

    private String replacePlaceholder(Object obj, Domain domain) {
        return replace(obj.toString(), DOMAIN_PLACEHOLDER, domain.getName());
    }

}
