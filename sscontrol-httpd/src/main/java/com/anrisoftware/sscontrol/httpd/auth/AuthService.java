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
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceArgs;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;
import com.google.inject.assistedinject.Assisted;

/**
 * HTTP/authentication service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthService implements WebService {

    private static final String HOST = "host";

    private static final String USER_ARG = "user";

    private static final String GROUP_ARG = "group";

    private static final String DOMAIN_ARG = "domain";

    private static final String VALID_ARGS = "valid";

    private static final String NAME_ARG = "name";

    private static final String REFERENCED_DOMAIN = "referenced domain";

    private static final String REFERENCE = "reference";

    private static final String ID = "id";

    private static final String DOMAIN = "domain";

    private static final String NAME = "name";

    /**
     * HTTP/authentication service name.
     */
    public static final String SERVICE_NAME = "auth";

    private final WebServiceLogger serviceLog;

    private final Domain domain;

    private final List<AuthRequire> requires;

    @Inject
    private AuthServiceLogger log;

    @Inject
    private RequireDomainFactory requireDomainFactory;

    @Inject
    private RequireUserFactory requireUserFactory;

    @Inject
    private RequireGroupFactory requireGroupFactory;

    @Inject
    private AuthHostFactory authHostFactory;

    @Inject
    private AuthCredentialsFactory authCredentialsFactory;

    @Inject
    private RequireValidFactory requireValidFactory;

    private String id;

    private String ref;

    private String refDomain;

    private String authName;

    private String location;

    private AuthType type;

    private AuthProvider provider;

    private SatisfyType satisfy;

    private RequireDomain requireDomain;

    private Boolean authoritative;

    private AuthHost host;

    private AuthCredentials credentials;

    /**
     * @see AuthServiceFactory#create(Domain, Map)
     */
    @Inject
    AuthService(AuthServiceLogger log, WebServiceArgs aargs,
            WebServiceLogger logger, @Assisted Domain domain,
            @Assisted Map<String, Object> args) {
        this.serviceLog = logger;
        this.domain = domain;
        this.requires = new ArrayList<AuthRequire>();
        if (aargs.haveId(args)) {
            setId(aargs.id(this, args));
        }
        if (aargs.haveRef(args)) {
            setRef(aargs.ref(this, args));
        }
        if (aargs.haveRefDomain(args)) {
            setRefDomain(aargs.refDomain(this, args));
        }
        if (log.haveAuth(args)) {
            setAuthoritative(log.auth(this, args));
        }
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
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

    @Override
    public Domain getDomain() {
        return domain;
    }

    public void setRefDomain(String domain) {
        this.refDomain = domain;
        serviceLog.refDomainSet(this, domain);
    }

    @Override
    public String getRefDomain() {
        return refDomain;
    }

    public void auth(Map<String, Object> args, String name) {
        log.checkAuthName(this, name);
        this.authName = name;
        if (log.haveLocation(args)) {
            this.location = log.location(this, args);
        }
    }

    public String getAuthName() {
        return authName;
    }

    public String getLocation() {
        return location;
    }

    public void type(Map<String, Object> args, AuthType type) {
        log.checkType(this, type);
        this.type = type;
        if (log.haveProvider(args)) {
            this.provider = log.provider(this, args);
        }
        if (log.haveSatisfy(args)) {
            this.satisfy = log.satisfy(this, args);
        }
    }

    public AuthType getType() {
        return type;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public SatisfyType getSatisfy() {
        return satisfy;
    }

    public void require(Map<String, Object> args) {
        if (args.containsKey(DOMAIN_ARG)) {
            this.requireDomain = requireDomainFactory.create(this, args);
            log.requireDomainSet(this, requireDomain);
            return;
        }
        if (args.containsKey(GROUP_ARG)) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requires.add(group);
            log.groupAdded(this, group);
            return;
        }
        if (args.containsKey(USER_ARG)) {
            RequireUser user = requireUserFactory.create(this, args);
            requires.add(user);
            log.userAdded(this, user);
            return;
        }
        if (args.containsKey(VALID_ARGS)) {
            RequireValid valid = requireValidFactory.create(this, args);
            requires.add(valid);
            log.validAdded(this, valid);
            return;
        }
    }

    public Object require(Map<String, Object> args, Object s) {
        if (args.containsKey(GROUP_ARG)) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requires.add(group);
            log.groupAdded(this, group);
            return group;
        }
        return null;
    }

    public RequireDomain getRequireDomain() {
        return requireDomain;
    }

    public List<AuthRequire> getRequires() {
        return requires;
    }

    public void setAuthoritative(boolean auth) {
        this.authoritative = auth;
        log.authSet(this, auth);
    }

    public Boolean getAuthoritative() {
        return authoritative;
    }

    public void host(Map<String, Object> args, String name) {
        args.put(HOST, name);
        AuthHost host = authHostFactory.create(this, args);
        log.hostSet(this, host);
        this.host = host;
    }

    public AuthHost getHost() {
        return host;
    }

    public AuthCredentials getCredentials() {
        return credentials;
    }

    public void credentials(Map<String, Object> args, String name) {
        args.put(NAME_ARG, name);
        AuthCredentials cred = authCredentialsFactory.create(this, args);
        log.credentialsSet(this, cred);
        this.credentials = cred;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(NAME, getName());
        builder.append(DOMAIN, getDomain());
        builder = id != null ? builder.append(ID, getName()) : builder;
        builder = ref != null ? builder.append(REFERENCE, getRef()) : builder;
        builder = refDomain != null ? builder.append(REFERENCED_DOMAIN,
                getRefDomain()) : builder;
        return builder.toString();
    }
}
