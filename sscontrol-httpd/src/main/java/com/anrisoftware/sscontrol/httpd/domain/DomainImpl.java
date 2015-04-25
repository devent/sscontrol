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
package com.anrisoftware.sscontrol.httpd.domain;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.groovy.StatementsTable;
import com.anrisoftware.sscontrol.core.groovy.StatementsTableFactory;
import com.anrisoftware.sscontrol.httpd.memory.Memory;
import com.anrisoftware.sscontrol.httpd.memory.MemoryFactory;
import com.anrisoftware.sscontrol.httpd.redirect.Redirect;
import com.anrisoftware.sscontrol.httpd.redirect.RedirectFactory;
import com.anrisoftware.sscontrol.httpd.service.WebServicesProvider;
import com.anrisoftware.sscontrol.httpd.user.DomainUser;
import com.anrisoftware.sscontrol.httpd.user.DomainUserArgs;
import com.anrisoftware.sscontrol.httpd.user.DomainUserFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;

/**
 * Domain entry.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DomainImpl implements Domain {

    private static final String ROOT_KEY = "root";
    private static final String CODES_KEY = "codes";
    private static final String PAGE_KEY = "page";
    private static final String ERROR_KEY = "error";
    private static final String SERVICE_NAME = "domain";
    private static final String LEVEL_KEY = "level";
    private static final String DEBUG_KEY = "debug";
    private static final String HTTP = "http://";
    private static final String USER = "user";
    private static final String SITE_DIR = "%s/web";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String USE = "use";
    private static final String ROOT = "root";
    private static final String PORT = "port";
    private static final String ADDRESS = "address";

    private final String name;

    private final List<Redirect> redirects;

    private final List<WebService> services;

    @Inject
    private DomainLogger log;

    @Inject
    private Injector injector;

    @Inject
    private RedirectFactory redirectFactory;

    @Inject
    private Map<String, WebServiceFactory> serviceFactories;

    @Inject
    private DomainUserFactory domainUserFactory;

    @Inject
    private DomainUser domainUser;

    @Inject
    private MemoryFactory memoryFactory;

    @Inject
    private WebServicesProvider webServicesProvider;

    private String address;

    private int port;

    private String documentRoot;

    private String useDomain;

    private String id;

    private Memory memory;

    private StatementsTable statementsTable;

    private StatementsMap statementsMap;

    /**
     * @see DomainFactory#create(Map, String)
     */
    @Inject
    DomainImpl(@Assisted Map<String, Object> args, @Assisted String name) {
        this(args, 80, name);
    }

    protected DomainImpl(Map<String, Object> args, int port, String name) {
        this.name = name;
        this.redirects = new ArrayList<Redirect>();
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

    @Inject
    public final void setupDomainStatements(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, SERVICE_NAME);
        map.addAllowed(ERROR_KEY);
        map.addAllowedKeys(ERROR_KEY, PAGE_KEY, CODES_KEY, ROOT_KEY);
        this.statementsMap = map;
    }

    @Inject
    public final void setDomainStatementsTable(StatementsTableFactory factory) {
        StatementsTable table = factory.create(factory, SERVICE_NAME);
        table.addAllowed(DEBUG_KEY);
        table.addAllowedKeys(DEBUG_KEY, LEVEL_KEY);
        this.statementsTable = table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
        user(args);
    }

    public void user(Map<String, Object> args) {
        DomainUser user = domainUserFactory.create(this, args);
        log.userSet(this, user);
        this.domainUser = user;
    }

    public void setDomainUser(DomainUser user) {
        this.domainUser = user;
    }

    @Override
    public DomainUser getDomainUser() {
        return domainUser;
    }

    public void address(String address) {
        this.address = address;
        log.addressSet(this, address);
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void port(int port) {
        this.port = port;
        log.portSet(this, port);
    }

    @Override
    public int getPort() {
        return port;
    }

    public void documentRoot(String root) {
        this.documentRoot = root;
        log.documentRootSet(this, root);
    }

    @Override
    public String getDocumentRoot() {
        return documentRoot;
    }

    public void useDomain(String use) {
        this.useDomain = use;
        log.useDomainSet(this, use);
    }

    @Override
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

    public WebService setup(String name, Object s) throws ServiceException {
        return setup(new HashMap<String, Object>(), name, s);
    }

    public void setup(Map<String, Object> map, String name)
            throws ServiceException {
        setup(map, name, null);
    }

    public WebService setup(Map<String, Object> map, String name, Object s)
            throws ServiceException {
        WebServiceFactory factory = serviceFactories.get(name);
        name = convertName(name);
        factory = findService(name, factory);
        log.checkService(this, factory, name);
        WebService service = factory.create(map, this);
        services.add(service);
        log.servicesAdded(this, service);
        return service;
    }

    private WebServiceFactory findService(final String name,
            WebServiceFactory factory) throws ServiceException {
        return factory != null ? factory : findService(name).getFactory();
    }

    @SuppressWarnings("serial")
    private WebServiceFactoryFactory findService(final String name)
            throws ServiceException {
        WebServiceFactoryFactory find = webServicesProvider
                .find(new WebServiceInfo() {

                    @Override
                    public String getServiceName() {
                        return name;
                    }
                });
        find.setParent(injector.getParent());
        return find;
    }

    private String convertName(String name) {
        return StringUtils.replaceChars(name, " .", "__");
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

    /**
     * Returns the memory limits.
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     *     memory limit: "32 MB", upload: "32 MB", post: "32 MB"
     * }
     * </pre>
     *
     * @return the memory {@link Memory} limits.
     */
    public Memory getMemory() {
        return memory;
    }

    @Override
    public Map<String, Object> getDebug() {
        return statementsTable.tableKeys(DEBUG_KEY, LEVEL_KEY);
    }

    /**
     * Returns the error page for the domain.
     * <p>
     * <h4>Examples</h4>
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     *     error page: "/50x.html"
     * }
     * </pre>
     *
     * @return the error {@link String} page or {@code null}.
     */
    public String getErrorPage() {
        return statementsMap.mapValue(ERROR_KEY, PAGE_KEY);
    }

    /**
     * Returns the error root location for the domain.
     * <p>
     * <h4>Examples</h4>
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     *     error root: "/usr/share/nginx/html"
     * }
     * </pre>
     *
     * @return the error {@link String} root or {@code null}.
     */
    public String getErrorRoot() {
        return statementsMap.mapValue(ERROR_KEY, ROOT_KEY);
    }

    /**
     * Returns the error codes for the domain.
     * <p>
     * <h4>Examples</h4>
     *
     * <pre>
     * domain "test1.com", address: "192.168.0.50", {
     *     error codes: "500, 502, 503, 504"
     * }
     * </pre>
     *
     * @return the error {@link List} codes or {@code null}.
     */
    public List<String> getErrorCodes() {
        return statementsMap.mapValueAsStringList(ERROR_KEY, CODES_KEY);
    }

    /**
     * Returns the protocol of the domain.
     *
     * @return the {@link String} protocol of the domain.
     */
    public String getProto() {
        return HTTP;
    }

    /**
     * Redirects to the statements map and table.
     *
     * @see StatementsMap#methodMissing(String, Object)
     * @see StatementsTable#methodMissing(String, Object)
     */
    public Object methodMissing(String name, Object args)
            throws StatementsException {
        try {
            statementsMap.methodMissing(name, args);
        } catch (StatementsException e) {
            statementsTable.methodMissing(name, args);
        }
        return null;
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
        DomainImpl rhs = (DomainImpl) obj;
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
