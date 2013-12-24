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
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.statements.webserviceargs.WebServiceArgs;
import com.anrisoftware.sscontrol.httpd.statements.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain proxy service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProxyService implements WebService {

    public static final String NAME = "proxy";

    private static final String SERVICE = "service";

    private static final String ADDRESS = "address";

    private final Domain domain;

    private final ProxyServiceLogger log;

    private final WebServiceLogger serviceLog;

    private String service;

    private String address;

    private String alias;

    private String id;

    private String ref;

    private String refDomain;

    /**
     * @see ProxyServiceFactory#create(Domain, Map)
     */
    @Inject
    ProxyService(WebServiceArgs aargs, WebServiceLogger serviceLogger,
            ProxyServiceLogger logger, ProxyServiceArgs proxyaargs, @Assisted Domain domain,
            @Assisted Map<String, Object> args) {
        this.log = logger;
        this.serviceLog = serviceLogger;
        this.domain = domain;
        setService(proxyaargs.service(domain, args));
        setAddress(proxyaargs.address(domain, args));
        if (aargs.haveAlias(args)) {
            setAlias(aargs.alias(this, args));
        }
        if (aargs.haveId(args)) {
            setId(aargs.id(this, args));
        }
        if (aargs.haveRef(args)) {
            setRef(aargs.ref(this, args));
        }
        if (aargs.haveRefDomain(args)) {
            setRefDomain(aargs.refDomain(this, args));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        serviceLog.aliasSet(this, alias);
    }

    public String getAlias() {
        return alias;
    }

    public void setId(String id) {
        this.id = id;
        serviceLog.idSet(this, id);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setRef(String ref) {
        this.ref = ref;
        serviceLog.refSet(this, ref);
    }

    @Override
    public String getRef() {
        return ref;
    }

    public void setRefDomain(String refDomain) {
        this.refDomain = refDomain;
    }

    @Override
    public String getRefDomain() {
        return refDomain;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service)
                .append(ADDRESS, address).toString();
    }
}
