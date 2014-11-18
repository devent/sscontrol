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
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger;

/**
 * HTTP/authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractAuthService implements WebService {

    private static final String DOMAIN = "domain";

    private static final String ATTRIBUTE_ARG = "attribute";

    private static final String HOST = "host";

    private static final String USER_ARG = "user";

    private static final String GROUP_ARG = "group";

    private static final String DOMAIN_ARG = DOMAIN;

    private static final String VALID_ARGS = "valid";

    private static final String NAME_ARG = "name";

    private final Domain domain;

    private final List<RequireDomain> requireDomains;

    private final List<RequireGroup> requireGroups;

    private final List<RequireUser> requireUsers;

    private final List<RequireValid> requireValids;

    private final Map<String, Object> attributes;

    private final AuthServiceLogger log;

    private final WebServiceLogger serviceLog;

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

    private SatisfyType satisfy;

    private Boolean authoritative;

    private AuthHost host;

    private AuthCredentials credentials;

    private String alias;

    private String prefix;

    /**
     * Sets the domain for the authentication.
     *
     * @param args
     *            the {@link Map} arguments.
     *
     * @param domain
     *            the {@link DomainImpl}.
     *
     * @param serviceLog
     *            the {@link WebServiceArgs}.
     *
     * @param log
     *            the {@link AuthServiceLogger}.
     */
    protected AbstractAuthService(Map<String, Object> args, Domain domain,
            WebServiceLogger serviceLog, AuthServiceLogger log) {
        this.domain = domain;
        this.serviceLog = serviceLog;
        this.log = log;
        this.requireDomains = new ArrayList<RequireDomain>();
        this.requireGroups = new ArrayList<RequireGroup>();
        this.requireUsers = new ArrayList<RequireUser>();
        this.requireValids = new ArrayList<RequireValid>();
        this.attributes = new HashMap<String, Object>();
        if (serviceLog.haveId(args)) {
            this.id = serviceLog.id(this, args);
        }
        if (serviceLog.haveAlias(args)) {
            this.alias = serviceLog.alias(this, args);
        }
        if (serviceLog.havePrefix(args)) {
            this.prefix = serviceLog.prefix(this, args);
        }
        if (serviceLog.haveRef(args)) {
            this.ref = serviceLog.ref(this, args);
        }
        if (serviceLog.haveRefDomain(args)) {
            this.refDomain = serviceLog.refDomain(this, args);
        }
        if (log.haveAuth(args)) {
            this.authName = log.auth(this, args);
        }
        if (log.haveLocation(args)) {
            this.location = log.location(this, args);
        }
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

    public String getAuthName() {
        return authName;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public void type(Map<String, Object> args, AuthType type) {
        log.checkType(this, type);
        this.type = type;
        if (log.haveSatisfy(args)) {
            this.satisfy = log.satisfy(this, args);
        }
        if (log.haveAuthoritative(args)) {
            this.authoritative = log.authoritative(this, args);
        }
    }

    public void setType(AuthType type) {
        this.type = type;
    }

    public AuthType getType() {
        return type;
    }

    public SatisfyType getSatisfy() {
        return satisfy;
    }

    public void require(Map<String, Object> args) {
        if (args.containsKey(DOMAIN_ARG)) {
            RequireDomain domain = requireDomainFactory.create(this, args);
            requireDomains.add(domain);
            log.requireDomainSet(this, domain);
            return;
        }
        if (args.containsKey(GROUP_ARG)) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requireGroups.add(group);
            log.groupAdded(this, group);
            return;
        }
        if (args.containsKey(USER_ARG)) {
            RequireUser user = requireUserFactory.create(this, args);
            requireUsers.add(user);
            log.userAdded(this, user);
            return;
        }
        if (args.containsKey(VALID_ARGS)) {
            RequireValid valid = requireValidFactory.create(this, args);
            requireValids.add(valid);
            log.validAdded(this, valid);
            return;
        }
        if (args.containsKey(ATTRIBUTE_ARG)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> attr = (Map<String, Object>) args
                    .get(ATTRIBUTE_ARG);
            attr = convertYesNo(attr);
            attributes.putAll(attr);
            log.attributeAdded(this, attr);
            return;
        }
    }

    private Map<String, Object> convertYesNo(Map<String, Object> attr) {
        Map<String, Object> copyattr = new HashMap<String, Object>(attr);
        for (Map.Entry<String, Object> entry : attr.entrySet()) {
            if (entry.getValue() instanceof YesNoFlag) {
                copyattr.put(entry.getKey(),
                        ((YesNoFlag) entry.getValue()).asBoolean());
            }
        }
        return copyattr;
    }

    public Object require(Map<String, Object> args, Object s) {
        if (args.containsKey(GROUP_ARG)) {
            RequireGroup group = requireGroupFactory.create(this, args);
            requireGroups.add(group);
            log.groupAdded(this, group);
            return group;
        }
        return null;
    }

    public List<RequireGroup> getRequireGroups() {
        return requireGroups;
    }

    public List<RequireUser> getRequireUsers() {
        return requireUsers;
    }

    public List<RequireValid> getRequireValids() {
        return requireValids;
    }

    public List<RequireDomain> getRequireDomains() {
        return requireDomains;
    }

    public void setAuthoritative(boolean auth) {
        this.authoritative = auth;
        log.authoritativeSet(this, auth);
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(DOMAIN, domain).toString();
    }
}
