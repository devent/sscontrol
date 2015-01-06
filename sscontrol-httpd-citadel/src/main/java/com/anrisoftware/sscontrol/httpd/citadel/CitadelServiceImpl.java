/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-citadel.
 *
 * sscontrol-httpd-citadel is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-citadel is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-citadel. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.citadel;

import static com.anrisoftware.sscontrol.httpd.service.HttpdFactory.NAME;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTable;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Citadel</i> service.
 *
 * @see <a href="http://citadel.org">http://citadel.org</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CitadelServiceImpl implements CitadelService {

    private static final String PASSWORD_KEY = "password";

    private static final String METHOD_KEY = "method";

    private static final String ADMIN_KEY = "admin";

    private static final String AUTH_KEY = "auth";

    /**
     * The <i>Citadel</i> service name.
     */
    public static final String SERVICE_NAME = "citadel";

    private final DefaultWebService service;

    private final StatementsMap statementsMap;

    private BindingAddressesStatementsTable bindingAddresses;

    /**
     * @see CitadelServiceFactory#create(Map, Domain)
     */
    @Inject
    CitadelServiceImpl(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
        this.statementsMap = service.getStatementsMap();
        setupStatements(service.getStatementsMap());
    }

    private void setupStatements(StatementsMap map) {
        map.addAllowed(AUTH_KEY, ADMIN_KEY);
        map.setAllowValue(true, ADMIN_KEY);
        map.addAllowedKeys(AUTH_KEY, METHOD_KEY);
        map.addAllowedKeys(ADMIN_KEY, PASSWORD_KEY);
    }

    @Inject
    public final void setBindingAddressesStatementsTable(
            BindingAddressesStatementsTableFactory factory) {
        this.bindingAddresses = factory.create(this, NAME);
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
    public Map<String, List<Integer>> getBindingAddresses() {
        return bindingAddresses.getBindingAddresses();
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
    public AuthMethod getAuthMethod() {
        return statementsMap.mapValue(AUTH_KEY, METHOD_KEY);
    }

    public Object methodMissing(String name, Object args) {
        try {
            return service.methodMissing(name, args);
        } catch (StatementsException e) {
            return bindingAddresses.methodMissing(name, args);
        }
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
