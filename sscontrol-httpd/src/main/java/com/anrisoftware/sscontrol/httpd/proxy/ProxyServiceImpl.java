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
package com.anrisoftware.sscontrol.httpd.proxy;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain proxy service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProxyServiceImpl implements ProxyService {

    private static final String STATIC_FILES_KEY = "staticFiles";

    private static final String CACHE_KEY = "cache";

    private static final String FEEDS_KEY = "feeds";

    private static final String NAME_FORMAT = "%s.%s";

    private static final String PROXY_NAME_FORMAT = "%s_%s";

    private static final String UNDER_STR = "_";

    private static final String DOT_STR = ".";

    public static final String SERVICE_NAME = "proxy";

    private final DefaultWebService webService;

    private final StatementsMap statementsMap;

    @Inject
    private ProxyServiceLogger log;

    private String service;

    private String address;

    private String proxyName;

    private String target;

    /**
     * @see ProxyServiceFactory#create(Map, Domain)
     */
    @Inject
    ProxyServiceImpl(DefaultWebServiceFactory webServiceFactory,
            ProxyServiceArgs proxyaargs, WebServiceLogger webServiceLogger,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.webService = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = webService.getStatementsMap();
        this.service = proxyaargs.service(domain, args);
        this.address = proxyaargs.address(domain, args);
        if (proxyaargs.haveTarget(args)) {
            this.target = proxyaargs.target(domain, args);
        }
        setupStatementsMap(statementsMap);
        setDefaultProxyName(webServiceLogger, args);
    }

    private void setupStatementsMap(StatementsMap map) {
        map.addAllowed(CACHE_KEY);
        map.addAllowedKeys(CACHE_KEY, STATIC_FILES_KEY, FEEDS_KEY);
    }

    private void setDefaultProxyName(WebServiceLogger serviceLog,
            Map<String, Object> args) {
        if (serviceLog.haveProxyName(args)) {
            this.proxyName = serviceLog.proxyName(this, args);
        } else if (serviceLog.haveAlias(args)) {
            this.proxyName = format(PROXY_NAME_FORMAT, service, getAlias());
        } else {
            String domain = webService.getDomain().getName();
            this.proxyName = format(PROXY_NAME_FORMAT, service,
                    replace(domain, DOT_STR, UNDER_STR));
        }
    }

    @Override
    public String getName() {
        return format(NAME_FORMAT, SERVICE_NAME, getService());
    }

    @Override
    public Domain getDomain() {
        return webService.getDomain();
    }

    public void setAlias(String alias) throws ServiceException {
        webService.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return webService.getAlias();
    }

    public void setId(String id) throws ServiceException {
        webService.setId(id);
    }

    @Override
    public String getId() {
        return webService.getId();
    }

    public void setRef(String ref) throws ServiceException {
        webService.setRef(ref);
    }

    @Override
    public String getRef() {
        return webService.getRef();
    }

    public void setRefDomain(String ref) throws ServiceException {
        webService.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return webService.getRefDomain();
    }

    public void setPrefix(String prefix) throws ServiceException {
        webService.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return webService.getPrefix();
    }

    public void setService(String service) {
        this.service = service;
        log.serviceSet(getDomain(), service);
    }

    @Override
    public String getService() {
        return service;
    }

    public void setAddress(String address) {
        this.address = address;
        log.addressSet(getDomain(), address);
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
        log.proxyNameSet(getDomain(), proxyName);
    }

    @Override
    public String getProxyName() {
        return proxyName;
    }

    @Override
    public String getTarget() {
        return target;
    }

    public void setCacheStaticFiles(boolean cache) throws ServiceException {
        statementsMap.putMapValue(CACHE_KEY, STATIC_FILES_KEY, cache);
    }

    public Boolean isCacheStaticFiles() {
        return statementsMap.mapValue(CACHE_KEY, STATIC_FILES_KEY);
    }

    public void setCacheFeeds(boolean cache) throws ServiceException {
        statementsMap.putMapValue(CACHE_KEY, FEEDS_KEY, cache);
    }

    public Boolean isCacheFeeds() {
        return statementsMap.mapValue(CACHE_KEY, FEEDS_KEY);
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        return webService.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return webService.toString();
    }
}
