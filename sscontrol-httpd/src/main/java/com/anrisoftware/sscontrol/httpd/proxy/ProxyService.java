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
package com.anrisoftware.sscontrol.httpd.proxy;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain proxy service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProxyService implements WebService {

    private static final String NAME_FORMAT = "%s.%s";

    private static final String PROXY_NAME_FORMAT = "%s_%s";

    private static final String UNDER_STR = "_";

    private static final String DOT_STR = ".";

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

    private String proxyName;

    /**
     * @see ProxyServiceFactory#create(Map, Domain)
     */
    @Inject
    ProxyService(WebServiceLogger serviceLogger, ProxyServiceLogger logger,
            ProxyServiceArgs proxyaargs, @Assisted Map<String, Object> args,
            @Assisted Domain domain) {
        this.log = logger;
        this.serviceLog = serviceLogger;
        this.domain = domain;
        setService(proxyaargs.service(domain, args));
        setAddress(proxyaargs.address(domain, args));
        if (serviceLog.haveAlias(args)) {
            this.alias = serviceLog.alias(this, args);
        }
        if (serviceLog.haveId(args)) {
            this.id = serviceLog.id(this, args);
        }
        if (serviceLog.haveRef(args)) {
            this.ref = serviceLog.ref(this, args);
        }
        if (serviceLog.haveRefDomain(args)) {
            this.refDomain = serviceLog.refDomain(this, args);
        }
        if (serviceLog.haveProxyName(args)) {
            this.proxyName = serviceLog.proxyName(this, args);
        } else {
            setDefaultProxyName(serviceLog, args);
        }
    }

    private void setDefaultProxyName(WebServiceLogger serviceLog,
            Map<String, Object> args) {
        if (serviceLog.haveAlias(args)) {
            setProxyName(format(PROXY_NAME_FORMAT, service, alias));
        } else {
            setProxyName(format(PROXY_NAME_FORMAT, service,
                    replace(domain.getName(), DOT_STR, UNDER_STR)));
        }
    }

    @Override
    public String getName() {
        return String.format(NAME_FORMAT, NAME, getService());
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

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
        log.proxyNameSet(domain, proxyName);
    }

    public String getProxyName() {
        return proxyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(SERVICE, service)
                .append(ADDRESS, address).toString();
    }
}
