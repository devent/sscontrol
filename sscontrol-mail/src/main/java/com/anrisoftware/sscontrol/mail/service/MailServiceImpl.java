/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service;

import static com.anrisoftware.sscontrol.mail.service.MailFactory.NAME;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.anrisoftware.sscontrol.core.bindings.Address;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.bindings.BindingAddress;
import com.anrisoftware.sscontrol.core.bindings.BindingArgs;
import com.anrisoftware.sscontrol.core.bindings.BindingFactory;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLoggingFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.mail.api.MailService;
import com.anrisoftware.sscontrol.mail.certificate.Certificate;
import com.anrisoftware.sscontrol.mail.certificate.CertificateFactory;
import com.anrisoftware.sscontrol.mail.resetdomains.ResetDomains;
import com.anrisoftware.sscontrol.mail.resetdomains.ResetDomainsFactory;
import com.anrisoftware.sscontrol.mail.statements.Database;
import com.anrisoftware.sscontrol.mail.statements.DatabaseFactory;
import com.anrisoftware.sscontrol.mail.statements.Domain;
import com.anrisoftware.sscontrol.mail.statements.DomainFactory;
import com.anrisoftware.sscontrol.mail.statements.MasqueradeDomains;

/**
 * Mail service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class MailServiceImpl extends AbstractService implements MailService {

    private final List<Domain> domains;

    private final Set<String> destinations;

    @Inject
    private MailServiceImplLogger log;

    @Inject
    private CertificateFactory certificateFactory;

    @Inject
    private DebugLoggingFactory debugLoggingFactory;

    @Inject
    private Binding binding;

    @Inject
    private BindingArgs bindingArgs;

    @Inject
    private MasqueradeDomains masqueradeDomains;

    @Inject
    private DomainFactory domainFactory;

    @Inject
    private DatabaseFactory databaseFactory;

    private DebugLogging debug;

    private String domainName;

    private String origin;

    private String relayHost;

    private Certificate certificate;

    private ResetDomainsFactory resetDomainsFactory;

    private ResetDomains resetDomains;

    private Database database;

    /**
     * @see MailFactory#create(ProfileService)
     */
    @Inject
    MailServiceImpl() {
        this.domains = new ArrayList<Domain>();
        this.relayHost = null;
        this.destinations = new HashSet<String>();
    }

    @Inject
    void setResetDomainsFactory(ResetDomainsFactory factory) {
        this.resetDomainsFactory = factory;
        this.resetDomains = resetDomainsFactory
                .create(new HashMap<String, Object>());
    }

    @Override
    protected Script getScript(String profileName) throws ServiceException {
        ServiceScriptFactory scriptFactory = findScriptFactory(NAME);
        return (Script) scriptFactory.getScript();
    }

    @Override
    protected boolean serviceScriptCompare(ServiceScriptInfo info,
            String serviceName, ProfileService profile) {
        boolean found = super.serviceScriptCompare(info, serviceName, profile);
        if (info instanceof MailServiceScriptInfo) {
            MailServiceScriptInfo mail = (MailServiceScriptInfo) info;
            Object storage = getProfile().getEntry(serviceName).get("storage");
            found = mail.getStorage().equals(storage);
        }
        return found;
    }

    /**
     * Because we load the script from a script service the dependencies are
     * already injected.
     */
    @Override
    protected void injectScript(Script script) {
    }

    /**
     * Returns the mail service name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Entry point for the mail service script.
     * 
     * @param statements
     *            the mail script statements.
     * 
     * @return this {@link Service}.
     */
    public Service mail(Object statements) {
        return this;
    }

    /**
     * Sets the debug logging for the database server.
     * 
     * @see DebugLoggingFactory#create(Map)
     */
    public void debug(Map<String, Object> args) {
        debug = debugLoggingFactory.create(args);
        log.debugLoggingSet(this, debug);
    }

    @Override
    public void setDebug(DebugLogging debug) {
        this.debug = debug;
    }

    @Override
    public DebugLogging getDebug() {
        return debug;
    }

    /**
     * Sets the IP addresses or host names to where to bind the mail service.
     * 
     * @see BindingFactory#create(Map, String...)
     */
    public void bind(Map<String, Object> args) throws ServiceException {
        List<Address> addresses = bindingArgs.createAddress(this, args);
        binding.addAddress(addresses);
        log.bindingSet(this, binding);
    }

    /**
     * Sets the IP addresses or host names to where to bind the mail service.
     * 
     * @see BindingFactory#create(BindingAddress)
     */
    public void bind(BindingAddress address) throws ServiceException {
        binding.addAddress(address);
        log.bindingSet(this, binding);
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    /**
     * Sets the relay host.
     * 
     * @param host
     *            the host {@link String}, can be empty or {@code null}.
     */
    public void relay(String host) {
        this.relayHost = host;
        log.relayHostSet(this, host);
    }

    @Override
    public String getRelayHost() {
        return relayHost;
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the domain name of the server.
     * 
     * @param name
     *            the domain {@link String} name.
     */
    public void name(String name) {
        log.checkDomainName(this, name);
        this.domainName = name;
        log.domainNameSet(this, name);
    }

    @Override
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets domain name that locally-posted mail appears to come from, and that
     * locally posted mail is delivered to.
     * 
     * @param name
     *            the domain {@link String} name.
     */
    public void origin(String name) {
        log.checkDomainName(this, name);
        this.origin = name;
        log.originSet(this, name);
    }

    /**
     * Additional list of domains that are delivered to local mail users.
     * 
     * @param list
     *            the list of the domains.
     */
    public void destinations(Object... list) {
        destinations(Arrays.asList(list));
    }

    /**
     * Additional list of domains that are delivered to local mail users.
     * 
     * @param list
     *            the list of the domains.
     */
    public void destinations(List<?> list) {
        log.checkDestinations(this, list);
        for (Object object : list) {
            String destination = object.toString().trim();
            destinations.add(destination);
            log.destinationAdded(this, destination);
        }
    }

    @Override
    public Collection<String> getDestinations() {
        return unmodifiableCollection(destinations);
    }

    /**
     * Sets the masquerade domains.
     * 
     * @param statements
     *            the script statements of the masquerade domains.
     * 
     * @return the {@link MasqueradeDomains}.
     */
    public MasqueradeDomains masquerade(Object statements) {
        return masqueradeDomains;
    }

    @Override
    public MasqueradeDomains getMasqueradeDomains() {
        return masqueradeDomains;
    }

    /**
     * Sets the location of the certificate, certificate key and CA file.
     * 
     * @param args
     *            the {@link Map} arguments of the statement.
     * 
     * @throws ServiceException
     *             if one the specified locations could not be parsed in a valid
     *             URL.
     */
    public void certificate(Map<String, Object> args) throws ServiceException {
        Certificate files = certificateFactory.create(this, args);
        log.certificateSet(this, files);
        this.certificate = files;
    }

    @Override
    public Certificate getCertificate() {
        return certificate;
    }

    public void domain(String name) {
        domain(name, null);
    }

    public Domain domain(String name, Object statements) {
        log.checkDomainName(this, name);
        Domain domain = domainFactory.create(name);
        domains.add(domain);
        log.domainAdded(this, domain);
        return domain;
    }

    @Override
    public List<Domain> getDomains() {
        return unmodifiableList(domains);
    }

    public void reset(Map<String, Object> args) {
        ResetDomains reset = resetDomainsFactory.create(args);
        this.resetDomains = reset;
        log.resetDomainSet(this, reset);
    }

    @Override
    public ResetDomains getResetDomains() {
        return resetDomains;
    }

    public void database(Map<String, Object> args, String name) {
        this.database = databaseFactory.create(args, name);
        log.databaseSet(this, database);
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    /**
     * @see YesNoFlag#yes
     */
    public YesNoFlag getYes() {
        return YesNoFlag.yes;
    }

    /**
     * @see YesNoFlag#no
     */
    public YesNoFlag getNo() {
        return YesNoFlag.no;
    }

    /**
     * @see BindingAddress#all
     */
    public BindingAddress getAll() {
        return BindingAddress.all;
    }

    /**
     * @see BindingAddress#loopback
     */
    public BindingAddress getLoopback() {
        return BindingAddress.loopback;
    }

    /**
     * @see BindingAddress#local
     */
    public BindingAddress getLocal() {
        return BindingAddress.local;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .toString();
    }

}
