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
package com.anrisoftware.sscontrol.httpd.statements.domain;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.authfile.AuthFileFactory;
import com.anrisoftware.sscontrol.httpd.statements.authldap.AuthLdapFactory;
import com.anrisoftware.sscontrol.httpd.statements.memory.Memory;
import com.anrisoftware.sscontrol.httpd.statements.memory.MemoryFactory;
import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect;
import com.anrisoftware.sscontrol.httpd.statements.redirect.RedirectFactory;
import com.anrisoftware.sscontrol.httpd.statements.user.DomainUser;
import com.anrisoftware.sscontrol.httpd.statements.user.DomainUserArgs;
import com.anrisoftware.sscontrol.httpd.statements.user.DomainUserFactory;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain entry.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Domain {

    private static final String HTTP = "http://";
    private static final String USER = "user";
    private static final String AUTH_PROVIDER = "provider";
    private static final String SITE_DIR = "%s/web";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String USE = "use";
    private static final String ROOT = "root";
    private static final String PORT = "port";
    private static final String ADDRESS = "address";

    private final String name;

    private final List<Redirect> redirects;

    private final List<AbstractAuth> auths;

    private final List<WebService> services;

    @Inject
    private DomainLogger log;

    @Inject
    private RedirectFactory redirectFactory;

    @Inject
    private AuthFileFactory authFactory;

    @Inject
    private AuthLdapFactory authLdapFactory;

    @Inject
    private Map<String, WebServiceFactory> serviceFactories;

    @Inject
    private DomainUserFactory domainUserFactory;

    @Inject
    private DomainUser domainUser;

    @Inject
    private MemoryFactory memoryFactory;

    private String address;

    private int port;

    private String documentRoot;

    private String useDomain;

    private String id;

    private Memory memory;

    /**
     * @see DomainFactory#create(Map, String)
     */
    @Inject
    Domain(@Assisted Map<String, Object> args, @Assisted String name) {
        this(args, 80, name);
    }

    protected Domain(Map<String, Object> args, int port, String name) {
        this.name = name;
        this.redirects = new ArrayList<Redirect>();
        this.auths = new ArrayList<AbstractAuth>();
        this.services = new ArrayList<WebService>();
        this.port = port;
        if (args.containsKey(ADDRESS)) {
            this.address = (String) args.get(ADDRESS);
        }
        if (args.containsKey(PORT)) {
            this.port = (Integer) args.get(PORT);
        }
        if (args.containsKey(ROOT)) {
            this.documentRoot = (String) args.get(ROOT);
        }
        if (args.containsKey(USE)) {
            this.useDomain = (String) args.get(USE);
        }
        if (args.containsKey(ID)) {
            this.id = (String) args.get(ID);
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return format("100-robobee-%s.conf", getName());
    }

    /**
     * Returns the directory of the site for this domain.
     */
    public String getSiteDirectory() {
        if (useDomain != null) {
            return format(SITE_DIR, useDomain);
        } else {
            return format(SITE_DIR, getName());
        }
    }

    public void user(Map<String, Object> args, String name) {
        args.put(DomainUserArgs.USER, name);
        DomainUser user = domainUserFactory.create(this, args);
        log.userSet(this, user);
        this.domainUser = user;
    }

    public DomainUser getDomainUser() {
        return domainUser;
    }

    public void address(String address) {
        this.address = address;
        log.addressSet(this, address);
    }

    public String getAddress() {
        return address;
    }

    public void port(int port) {
        this.port = port;
        log.portSet(this, port);
    }

    public int getPort() {
        return port;
    }

    public void documentRoot(String root) {
        this.documentRoot = root;
        log.documentRootSet(this, root);
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void useDomain(String use) {
        this.useDomain = use;
        log.useDomainSet(this, use);
    }

    public String getUseDomain() {
        return useDomain;
    }

    public void redirect(Map<String, Object> args) {
        Redirect redirect = redirectFactory.create(this, args);
        addRedirect(redirect);
    }

    public List<Redirect> getRedirects() {
        return redirects;
    }

    public void addRedirect(Redirect redirect) {
        redirects.add(redirect);
        log.redirectAdded(this, redirect);
    }

    public AbstractAuth auth(Map<String, Object> args, String name, Object s) {
        AbstractAuth auth = null;
        if (args.containsKey(AUTH_PROVIDER)) {
            AuthProvider provider = (AuthProvider) args.get(AUTH_PROVIDER);
            switch (provider) {
            case file:
                auth = authFactory.create(args, name);
                break;
            case ldap:
                auth = authLdapFactory.create(args, name);
                break;
            }
        }
        auth = auth == null ? authFactory.create(args, name) : auth;
        auths.add(auth);
        return auth;
    }

    public List<AbstractAuth> getAuths() {
        return auths;
    }

    public WebService setup(String name, Object s) {
        return setup(new HashMap<String, Object>(), name, s);
    }

    public void setup(Map<String, Object> map, String name) {
        setup(map, name, null);
    }

    public WebService setup(Map<String, Object> map, String name, Object s) {
        WebServiceFactory factory = serviceFactories.get(name);
        log.checkService(this, factory, name);
        WebService service = factory.create(this, map);
        services.add(service);
        log.servicesAdded(this, service);
        return service;
    }

    public List<WebService> getServices() {
        return services;
    }

    public void memory(Map<String, Object> args) {
        Memory memory = memoryFactory.create(args);
        log.memorySet(this, memory);
        this.memory = memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Memory getMemory() {
        return memory;
    }

    /**
     * Returns the protocol of the domain.
     * 
     * @return the {@link String} protocol of the domain.
     */
    public String getProto() {
        return HTTP;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(address).append(port).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Domain rhs = (Domain) obj;
        return new EqualsBuilder().append(address, rhs.getAddress())
                .append(port, rhs.getPort()).isEquals();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(NAME, name);
        builder = id != null ? builder.append(ID, id) : builder;
        builder.append(USER, domainUser);
        builder.append(ADDRESS, address).append(PORT, port);
        return builder.toString();
    }
}
