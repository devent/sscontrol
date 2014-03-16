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
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.util.Map;

import javax.inject.Inject;

/**
 * Parses arguments for Roundcube mail host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostArgs {

    public static final String DOMAIN = "domain";
    public static final String ALIAS = "alias";
    public static final String HOST = "host";

    @Inject
    private HostLogger log;

    String host(Object service, Map<String, Object> args) {
        Object hostname = args.get(HOST);
        log.checkHost(service, hostname);
        return hostname.toString();
    }

    boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    String alias(Map<String, Object> args) {
        return args.get(ALIAS).toString();
    }

    boolean haveDomain(Map<String, Object> args) {
        return args.containsKey(DOMAIN);
    }

    String domain(Map<String, Object> args) {
        return args.get(DOMAIN).toString();
    }

}
