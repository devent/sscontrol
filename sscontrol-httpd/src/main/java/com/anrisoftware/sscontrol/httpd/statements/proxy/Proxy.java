/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.statements.proxy;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain proxy.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Proxy {

    private static final String SERVICE = "service";

    private static final String ADDRESS = "address";

    private final Domain domain;

    private final ProxyLogger log;

    private String service;

    private String address;

    private String alias;

    @Inject
    Proxy(ProxyLogger logger, ProxyArgs aargs, @Assisted Domain domain,
            @Assisted Map<String, Object> args) {
        this.log = logger;
        this.domain = domain;
        setService(aargs.service(domain, args));
        setAddress(aargs.address(domain, args));
        if (aargs.haveAlias(args)) {
            setAlias(aargs.alias(domain, args));
        }
    }

    public void setService(String service) {
        this.service = service;
        log.serviceSet(domain, service);
    }

    public String getService() {
        return service;
    }

    public void setAddress(String address) {
        this.address = address;
        log.addressSet(domain, address);
    }

    public String getAddress() {
        return address;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        log.aliasSet(domain, alias);
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service)
                .append(ADDRESS, address).toString();
    }
}
