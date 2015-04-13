/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-phpmyadmin.
 *
 * sscontrol-httpd-phpmyadmin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-phpmyadmin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-phpmyadmin. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.phpmyadmin;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>phpMyAdmin</i> service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PhpmyadminServiceImpl implements PhpmyadminService {

    private static final String NAME = "service name";

    private static final String PORT_KEY = "port";

    private static final String DATABASE_KEY = "database";

    private static final String PASSWORD_KEY = "password";

    private static final String SERVER_KEY = "server";

    private static final String CONTROL_KEY = "control";

    private static final String ADMIN_KEY = "admin";

    public static final String SERVICE_NAME = "phpmyadmin";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    /**
     * @see PhpmyadminServiceFactory#create(Map, Domain)
     */
    @Inject
    PhpmyadminServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(statementsMap, args);
    }

    private void setupStatements(StatementsMap map, Map<String, Object> args) {
        map.addAllowed(ADMIN_KEY, CONTROL_KEY, SERVER_KEY);
        map.addAllowedKeys(ADMIN_KEY, PASSWORD_KEY);
        map.addAllowedKeys(CONTROL_KEY, PASSWORD_KEY, DATABASE_KEY);
        map.addAllowedKeys(SERVER_KEY, PORT_KEY);
        map.setAllowValue(true, ADMIN_KEY, CONTROL_KEY, SERVER_KEY);
    }

    @Override
    public Domain getDomain() {
        return service.getDomain();
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
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
    public String getAdminUser() {
        return statementsMap.value(ADMIN_KEY);
    }

    @Override
    public String getAdminPassword() {
        return statementsMap.mapValue(ADMIN_KEY, PASSWORD_KEY);
    }

    @Override
    public String getControlUser() {
        return statementsMap.value(CONTROL_KEY);
    }

    @Override
    public String getControlPassword() {
        return statementsMap.mapValue(CONTROL_KEY, PASSWORD_KEY);
    }

    @Override
    public String getControlDatabase() {
        return statementsMap.mapValue(CONTROL_KEY, DATABASE_KEY);
    }

    @Override
    public String getServer() {
        return statementsMap.value(SERVER_KEY);
    }

    @Override
    public Integer getServerPort() {
        return statementsMap.mapValue(SERVER_KEY, PORT_KEY);
    }

    public Object methodMissing(String name, Object args)
            throws ServiceException {
        service.methodMissing(name, args);
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME, getName()).toString();
    }
}
