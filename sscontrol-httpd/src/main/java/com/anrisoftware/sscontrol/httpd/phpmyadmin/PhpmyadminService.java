/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.phpmyadmin;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Phpmyadmin</i> service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PhpmyadminService implements WebService {

    public static final String SERVICE_NAME = "phpmyadmin";

    private final DefaultWebService service;

    @Inject
    private PhpmyadminServiceLogger log;

    @Inject
    private AdminUser adminUser;

    @Inject
    private ControlUser controlUser;

    @Inject
    private Server server;

    /**
     * @see PhpmyadminServiceFactory#create(Map, Domain)
     */
    @Inject
    PhpmyadminService(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.service = webServiceFactory.create(SERVICE_NAME, args, domain);
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

    public void admin(Map<String, Object> map, String admin) {
        adminUser.setUser(admin);
        adminUser.setPassword(map.get("password"));
        log.adminSet(this, adminUser);
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void control(Map<String, Object> map, String user) {
        controlUser.setUser(user);
        controlUser.setPassword(map.get("password"));
        controlUser.setDatabase(map.get("database"));
        log.controlSet(this, controlUser);
    }

    public ControlUser getControlUser() {
        return controlUser;
    }

    public void server(Map<String, Object> map, String host) {
        server.setHost(host);
        server.setPort((Integer) map.get("port"));
        log.serverSet(this, server);
    }

    public Server getServer() {
        return server;
    }

    @Override
    public String toString() {
        return server.toString();
    }
}
