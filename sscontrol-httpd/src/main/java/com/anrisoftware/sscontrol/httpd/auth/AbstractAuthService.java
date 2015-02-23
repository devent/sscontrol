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
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceStatement.AUTH_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceStatement.GROUP_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceStatement.LOCATION_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceStatement.USER_KEY;
import static com.anrisoftware.sscontrol.httpd.auth.AuthServiceStatement.VALID_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;

/**
 * Authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractAuthService implements AuthService {

    private final Domain domain;

    private final Map<String, Object> args;

    private final String serviceName;

    private final List<RequireGroup> requireGroups;

    private final List<RequireUser> requireUsers;

    private final List<RequireValid> requireValids;

    private AuthServiceLogger log;

    @Inject
    private RequireGroupFactory requireGroupFactory;

    @Inject
    private RequireUserFactory requireUserFactory;

    private DefaultWebService service;

    private StatementsMap statementsMap;

    /**
     * Sets the domain for the authentication.
     *
     * @param args
     *            the {@link Map} arguments.
     *
     * @param domain
     *            the {@link Domain}.
     */
    protected AbstractAuthService(String serviceName, Map<String, Object> args,
            Domain domain) {
        this.serviceName = serviceName;
        this.domain = domain;
        this.args = new HashMap<String, Object>(args);
        this.requireGroups = new ArrayList<RequireGroup>();
        this.requireUsers = new ArrayList<RequireUser>();
        this.requireValids = new ArrayList<RequireValid>();
    }

    @Inject
    public final void setDefaultWebServiceFactory(AuthServiceLogger log,
            DefaultWebServiceFactory factory) {
        DefaultWebService service = factory.create(serviceName, args, domain);
        setupStatements(service.getStatementsMap());
        this.log = log;
        this.statementsMap = service.getStatementsMap();
        this.service = service;
    }

    private void setupStatements(StatementsMap map) {
        map.addAllowed(AUTH_KEY, LOCATION_KEY);
        map.setAllowValue(true, AUTH_KEY, LOCATION_KEY);
        map.putValue(AUTH_KEY.toString(), args.get(AUTH_KEY.toString()));
        if (args.containsKey(LOCATION_KEY.toString())) {
            map.putValue(LOCATION_KEY.toString(),
                    args.get(LOCATION_KEY.toString()));
        }
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
    public String getAuth() {
        return statementsMap.value(AUTH_KEY);
    }

    @Override
    public String getLocation() {
        return statementsMap.value(LOCATION_KEY);
    }

    public void require(Map<String, Object> args) {
        if (args.containsKey(GROUP_KEY.toString())) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requireGroups.add(group);
            log.groupAdded(this, group);
            return;
        }
        if (args.containsKey(USER_KEY.toString())) {
            RequireUser user = requireUserFactory.create(this, args);
            requireUsers.add(user);
            log.userAdded(this, user);
            return;
        }
        if (args.containsKey(VALID_KEY.toString())) {
            RequireValid valid = (RequireValid) args.get(VALID_KEY.toString());
            requireValids.add(valid);
            log.validAdded(this, valid);
            return;
        }
    }

    public Object require(Map<String, Object> args, Object s) {
        if (args.containsKey(GROUP_KEY.toString())) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requireGroups.add(group);
            log.groupAdded(this, group);
            return group;
        }
        return null;
    }

    @Override
    public List<RequireGroup> getRequireGroups() {
        return requireGroups;
    }

    @Override
    public List<RequireUser> getRequireUsers() {
        return requireUsers;
    }

    @Override
    public List<RequireValid> getRequireValids() {
        return requireValids;
    }

    public Object methodMissing(String name, Object args) {
        return service.methodMissing(name, args);
    }

    @Override
    public String toString() {
        return service.toString();
    }
}
