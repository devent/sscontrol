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
package com.anrisoftware.sscontrol.httpd.staticservice;

import static com.anrisoftware.sscontrol.httpd.staticservice.StaticStatement.FILES_KEY;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticStatement.INCLUDE_KEY;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticStatement.INDEX_KEY;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticStatement.MODE_KEY;
import static com.anrisoftware.sscontrol.httpd.staticservice.StaticStatement.REFS_KEY;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Static files service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class StaticServiceImpl implements StaticService {

    /**
     * Static files service name.
     */
    public static final String SERVICE_NAME = "static";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    /**
     * @see StaticServiceFactory#create(Map, Domain)
     */
    @Inject
    StaticServiceImpl(DefaultWebServiceFactory factory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = factory.create(SERVICE_NAME, args, domain);
        setupStatements(service.getStatementsMap(), args);
        this.statementsMap = service.getStatementsMap();
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(INDEX_KEY, INCLUDE_KEY);
        map.addAllowedKeys(INDEX_KEY, FILES_KEY, MODE_KEY);
        map.addAllowedKeys(INCLUDE_KEY, REFS_KEY);
    }

    @Override
    public Domain getDomain() {
        return service.getDomain();
    }

    @Override
    public String getName() {
        return service.getName();
    }

    public void setAlias(String alias) throws ServiceException {
        service.setAlias(alias);
    }

    @Override
    public String getAlias() {
        return service.getAlias();
    }

    public void setId(String id) throws ServiceException {
        service.setId(id);
    }

    @Override
    public String getId() {
        return service.getId();
    }

    public void setRef(String ref) throws ServiceException {
        service.setRef(ref);
    }

    @Override
    public String getRef() {
        return service.getRef();
    }

    public void setRefDomain(String ref) throws ServiceException {
        service.setRefDomain(ref);
    }

    @Override
    public String getRefDomain() {
        return service.getRefDomain();
    }

    public void setPrefix(String prefix) throws ServiceException {
        service.setPrefix(prefix);
    }

    @Override
    public String getPrefix() {
        return service.getPrefix();
    }

    @Override
    public List<String> getIndexFiles() {
        return statementsMap.mapValueAsStringList(INDEX_KEY, FILES_KEY);
    }

    @Override
    public IndexMode getIndexMode() {
        return statementsMap.mapValue(INDEX_KEY, MODE_KEY);
    }

    @Override
    public List<String> getIncludeRefs() {
        return statementsMap.mapValueAsStringList(INCLUDE_KEY, REFS_KEY);
    }

    /**
     * Redirects to the statements map and table.
     */
    public Object methodMissing(String name, Object args)
            throws StatementsException {
        service.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
