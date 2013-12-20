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
package com.anrisoftware.sscontrol.httpd.service;

import static com.anrisoftware.sscontrol.httpd.service.HttpdFactory.NAME;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingArgs;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.statements.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.statements.auth.SatisfyType;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.anrisoftware.sscontrol.httpd.statements.domain.DomainFactory;

/**
 * Firewall service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class HttpdServiceImpl extends AbstractService {

    private final List<Domain> domains;

    private final Set<Domain> virtualDomains;

    @Inject
    private HttpdServiceImplLogger log;

    @Inject
    private DomainFactory domainFactory;

    @Inject
    private Binding binding;

    @Inject
    private BindingArgs bindingArgs;

    HttpdServiceImpl() {
        this.domains = new ArrayList<Domain>();
        this.virtualDomains = new HashSet<Domain>();
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
        return (Script) scriptFactory.getScript();
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    /**
     * Returns the httpd service name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Entry point for the httpd service script.
     * 
     * @param statements
     *            the httpd statements.
     * 
     * @return this {@link Service}.
     */
    public Service httpd(Object statements) {
        return this;
    }

    /**
     * Sets the IP addresses or host names to where to bind the DNS service.
     * 
     * @see BindingFactory#create(Map, String...)
     */
    public void bind(Map<String, Object> args) throws ServiceException {
        List<Address> addresses = bindingArgs.createAddress(this, args);
        binding.addAddress(addresses);
        log.bindingSet(this, binding);
    }

    /**
     * Sets the IP addresses or host names to where to bind the DNS service.
     * 
     * @see BindingFactory#create(BindingAddress)
     */
    public void bind(BindingAddress address) throws ServiceException {
        binding.addAddress(address);
        log.bindingSet(this, binding);
    }

    /**
     * Returns a list of the IP addresses where to bind the DNS service.
     * 
     * @return the {@link Binding}.
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * @see DomainFactory#create(Map, String)
     */
    public void domain(Map<String, Object> args, String name) {
        domain(args, name, null);
    }

    /**
     * @see DomainFactory#create(Map, String)
     */
    public Domain domain(Map<String, Object> args, String name, Object s) {
        Domain domain = domainFactory.create(args, name);
        domains.add(domain);
        virtualDomains.add(domain);
        log.domainAdded(this, domain);
        return domain;
    }

    /**
     * @see DomainFactory#createSsl(Map, String)
     */
    public void ssl_domain(Map<String, Object> args, String name) {
        ssl_domain(args, name, null);
    }

    /**
     * @see DomainFactory#createSsl(Map, String)
     */
    public Domain ssl_domain(Map<String, Object> args, String name, Object s) {
        Domain domain = domainFactory.createSsl(args, name);
        domains.add(domain);
        virtualDomains.add(domain);
        log.sslDomainAdded(this, domain);
        return domain;
    }

    /**
     * Returns all domains of the service.
     * 
     * @return the {@link List} of the {@link Domain} domains.
     */
    public List<Domain> getDomains() {
        return domains;
    }

    /**
     * Returns a set of the virtual domains of the service.
     * 
     * @return the {@link Set} of the virtual {@link Domain} domains.
     */
    public Set<Domain> getVirtualDomains() {
        return virtualDomains;
    }

    public YesNoFlag getYes() {
        return YesNoFlag.yes;
    }

    public YesNoFlag getNo() {
        return YesNoFlag.no;
    }

    public AuthProvider getFile() {
        return AuthProvider.file;
    }

    public AuthProvider getLdap() {
        return AuthProvider.ldap;
    }

    public AuthType getDigest() {
        return AuthType.digest;
    }

    public AuthType getBasic() {
        return AuthType.basic;
    }

    public SatisfyType getAll() {
        return SatisfyType.all;
    }

    public SatisfyType getAny() {
        return SatisfyType.any;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
