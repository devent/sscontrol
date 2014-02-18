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
package com.anrisoftware.sscontrol.httpd.phpldapadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * Phpldapadmin service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PhpldapadminService implements WebService {

    public static final String NAME = "phpldapadmin";

    private final List<LdapServer> servers;

    private final Domain domain;

    private final WebServiceLogger serviceLog;

    @Inject
    private PhpldapadminServiceLogger log;

    @Inject
    private LdapServerFactory serverFactory;

    private String alias;

    private String id;

    private String ref;

    private String refDomain;

    /**
     * @see PhpldapadminServiceFactory#create(Map, Domain)
     */
    @Inject
    PhpldapadminService(WebServiceLogger serviceLog,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        this.serviceLog = serviceLog;
        this.domain = domain;
        this.servers = new ArrayList<LdapServer>();
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

    public void server(String name) {
        server(new HashMap<String, Object>(), name);
    }

    public void server(Map<String, Object> args, String name) {
        LdapServer server = serverFactory.create(args, name);
        servers.add(server);
        log.serverAdded(this, server);
    }

    public List<LdapServer> getServers() {
        return servers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(NAME).toString();
    }

}
