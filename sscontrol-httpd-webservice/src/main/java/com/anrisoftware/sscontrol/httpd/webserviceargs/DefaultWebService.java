/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.ALIAS;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.ID;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.PREFIX;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.PROXYNAME;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REF;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger.REFDOMAIN;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.google.inject.assistedinject.Assisted;

/**
 * Parses the web service arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public final class DefaultWebService implements WebService {

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final String serviceName;

    private final StatementsMap statementsMap;

    @Inject
    private WebServiceToString serviceToString;

    /**
     * @see DefaultWebServiceFactory#create(String, Map, Domain)
     */
    @Inject
    DefaultWebService(WebServiceLogger serviceLog,
            StatementsMapFactory statementsMapFactory,
            @Assisted String serviceName, @Assisted Map<String, Object> args,
            @Assisted Domain domain) throws ServiceException {
        this.serviceLog = serviceLog;
        this.domain = domain;
        this.serviceName = serviceName;
        this.statementsMap = createStatementsMap(serviceLog,
                statementsMapFactory, serviceName, args);
    }

    private StatementsMap createStatementsMap(WebServiceLogger serviceLog,
            StatementsMapFactory statementsMapFactory, String serviceName,
            Map<String, Object> args) throws ServiceException {
        StatementsMap map = statementsMapFactory.create(this, serviceName);
        map.addAllowed(ALIAS, ID, REF, REFDOMAIN, PREFIX, PROXYNAME);
        map.setAllowValue(true, ALIAS, ID, REF, REFDOMAIN, PREFIX, PROXYNAME);
        if (serviceLog.haveAlias(args)) {
            String value = serviceLog.alias(this, args);
            map.putValue(ALIAS, value);
        }
        if (serviceLog.haveId(args)) {
            String value = serviceLog.id(this, args);
            map.putValue(ID, value);
        }
        if (serviceLog.haveRef(args)) {
            String value = serviceLog.ref(this, args);
            map.putValue(REF, value);
        }
        if (serviceLog.haveRefDomain(args)) {
            String value = serviceLog.refDomain(this, args);
            map.putValue(REFDOMAIN, value);
        }
        if (serviceLog.havePrefix(args)) {
            String value = serviceLog.prefix(this, args);
            map.putValue(PREFIX, value);
        }
        if (serviceLog.haveProxyName(args)) {
            String value = serviceLog.proxyName(this, args);
            map.putValue(PROXYNAME, value);
        }
        return map;
    }

    public StatementsMap getStatementsMap() {
        return statementsMap;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String getName() {
        return serviceName;
    }

    public void setAlias(String alias) throws ServiceException {
        statementsMap.putValue(ALIAS, alias);
        serviceLog.aliasSet(this, alias);
    }

    @Override
    public String getAlias() {
        return statementsMap.value(ALIAS);
    }

    public void setId(String id) throws ServiceException {
        statementsMap.putValue(ID, id);
        serviceLog.idSet(this, id);
    }

    @Override
    public String getId() {
        return statementsMap.value(ID);
    }

    public void setRef(String ref) throws ServiceException {
        statementsMap.putValue(REF, ref);
        serviceLog.refSet(this, ref);
    }

    @Override
    public String getRef() {
        return statementsMap.value(REF);
    }

    public void setRefDomain(String ref) throws ServiceException {
        statementsMap.putValue(REFDOMAIN, ref);
    }

    @Override
    public String getRefDomain() {
        return statementsMap.value(REFDOMAIN);
    }

    public void setPrefix(String prefix) throws ServiceException {
        statementsMap.putValue(PREFIX, prefix);
    }

    @Override
    public String getPrefix() {
        return statementsMap.value(PREFIX);
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        statementsMap.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return serviceToString.toString(this);
    }

}
