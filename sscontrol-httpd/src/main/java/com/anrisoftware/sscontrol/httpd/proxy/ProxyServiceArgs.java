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
package com.anrisoftware.sscontrol.httpd.proxy;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Parses arguments for {@link ProxyServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProxyServiceArgs {

    public static final String ALIAS = "alias";

    public static final String SERVICE = "service";

    public static final String ADDRESS = "address";

    @Inject
    private ProxyServiceLogger log;

    public String address(Domain domain, Map<String, Object> args) {
        Object address = args.get(ADDRESS);
        log.checkAddress(domain, address);
        return address.toString();
    }

    public String service(Domain domain, Map<String, Object> args) {
        Object service = args.get(SERVICE);
        log.checkService(domain, service);
        return service.toString();
    }

}
