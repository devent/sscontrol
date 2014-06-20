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
            @Assisted Domain domain) {
        this.serviceLog = serviceLog;
        this.domain = domain;
        this.serviceName = serviceName;
        this.statementsMap = statementsMapFactory.create(this, serviceName);
        statementsMap.addAllowed(ALIAS);
        statementsMap.addAllowed(ID);
        statementsMap.addAllowed(REF);
        statementsMap.addAllowed(REFDOMAIN);
        statementsMap.addAllowed(PREFIX);
        statementsMap.addAllowed(PROXYNAME);
        if (serviceLog.haveAlias(args)) {
            String value = serviceLog.alias(this, args);
            statementsMap.putValue(ALIAS, value);
        }
        if (serviceLog.haveId(args)) {
            String value = serviceLog.id(this, args);
            statementsMap.putValue(ID, value);
        }
        if (serviceLog.haveRef(args)) {
            String value = serviceLog.ref(this, args);
            statementsMap.putValue(REF, value);
        }
        if (serviceLog.haveRefDomain(args)) {
            String value = serviceLog.refDomain(this, args);
            statementsMap.putValue(REFDOMAIN, value);
        }
        if (serviceLog.havePrefix(args)) {
            String value = serviceLog.prefix(this, args);
            statementsMap.putValue(PREFIX, value);
        }
        if (serviceLog.haveProxyName(args)) {
            String value = serviceLog.proxyName(this, args);
            statementsMap.putValue(PROXYNAME, value);
        }
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

    public void setAlias(String alias) {
        statementsMap.putValue(ALIAS, alias);
        serviceLog.aliasSet(this, alias);
    }

    @Override
    public String getAlias() {
        return statementsMap.value(ALIAS);
    }

    public void setId(String id) {
        statementsMap.putValue(ID, id);
        serviceLog.idSet(this, id);
    }

    @Override
    public String getId() {
        return statementsMap.value(ID);
    }

    public void setRef(String ref) {
        statementsMap.putValue(REF, ref);
        serviceLog.refSet(this, ref);
    }

    @Override
    public String getRef() {
        return statementsMap.value(REF);
    }

    public void setRefDomain(String ref) {
        statementsMap.putValue(REFDOMAIN, ref);
    }

    @Override
    public String getRefDomain() {
        return statementsMap.value(REFDOMAIN);
    }

    public void setPrefix(String prefix) {
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
