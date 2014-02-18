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
package com.anrisoftware.sscontrol.httpd.phpmyadmin;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * Phpmyadmin service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PhpmyadminService implements WebService {

    private static final String ALIAS = "alias";

    public static final String NAME = "phpmyadmin";

    private final Domain domain;

    private final WebServiceLogger serviceLog;

    @Inject
    private PhpmyadminServiceLogger log;

    @Inject
    private AdminUser adminUser;

    @Inject
    private ControlUser controlUser;

    @Inject
    private Server server;

    private String alias;

    private String id;

    private String ref;

    private String refDomain;

    /**
     * @see PhpmyadminServiceFactory#create(Map, Domain)
     */
    @Inject
    PhpmyadminService(WebServiceLogger serviceLog,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.serviceLog = serviceLog;
        this.domain = domain;
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
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public String getName() {
        return NAME;
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

    public void setRefDomain(String ref) {
        this.refDomain = ref;
    }

    @Override
    public String getRefDomain() {
        return refDomain;
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
        return new ToStringBuilder(this).append(NAME).append(ALIAS, alias)
                .append(adminUser).append(controlUser).append(server)
                .toString();
    }
}
