/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-hostname.
 * 
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service;

import static com.anrisoftware.sscontrol.mail.service.MailFactory.NAME;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.mail.statements.BindAddresses;
import com.anrisoftware.sscontrol.mail.statements.BindAddressesFactory;
import com.anrisoftware.sscontrol.mail.statements.CertificateFile;
import com.anrisoftware.sscontrol.mail.statements.CertificateFileFactory;
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
public class MailServiceImpl extends AbstractService {

	private final MailServiceImplLogger log;

	private final BindAddressesFactory bindAddressesFactory;

	private final CertificateFileFactory certificateFileFactory;

	private String domainName;

	private String origin;

	private String relayHost;

	private BindAddresses bindAddresses;

	private final MasqueradeDomains masqueradeDomains;

	private final List<Domain> domains;

	private CertificateFile certificateFile;

	private final DomainFactory domainFactory;

	private final Set<String> destinations;

	/**
	 * @see MailFactory#create(ProfileService)
	 */
	@Inject
	MailServiceImpl(MailServiceImplLogger logger,
			BindAddressesFactory bindAddressesFactory,
			MasqueradeDomains masqueradeDomains,
			CertificateFileFactory certificateFileFactory,
			DomainFactory domainFactory) {
		this.log = logger;
		this.bindAddressesFactory = bindAddressesFactory;
		this.masqueradeDomains = masqueradeDomains;
		this.certificateFileFactory = certificateFileFactory;
		this.domains = new ArrayList<Domain>();
		this.domainFactory = domainFactory;
		this.relayHost = null;
		this.destinations = new HashSet<String>();
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
	 * Sets the network interfaces addresses that this mail system receives mail
	 * on.
	 * 
	 * @param address
	 *            the {@link BindAddresses}.
	 * 
	 * @throws NullPointerException
	 *             if the specified addresses are {@code null}.
	 * 
	 * @see BindAddresses#ALL
	 * @see BindAddresses#LOOPBACK
	 */
	public void bind_addresses(BindAddresses address) {
		log.checkBindAddress(this, address);
		this.bindAddresses = address;
		log.bindAddressesSet(this, address);
	}

	/**
	 * Sets the network interfaces addresses that this mail system receives mail
	 * on.
	 * 
	 * @param address
	 *            the {@link BindAddresses}.
	 * 
	 * @throws NullPointerException
	 *             if the specified addresses are {@code null}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified addresses are empty.
	 */
	public void bind_addresses(String addresses) {
		log.checkBindAddresses(this, addresses);
		this.bindAddresses = bindAddressesFactory.create(addresses);
		log.bindAddressesSet(this, bindAddresses);
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

	/**
	 * Returns the relay host.
	 * 
	 * @return the relay host {@link String} or {@code null}.
	 */
	public String getRelayHost() {
		return relayHost;
	}

	/**
	 * Returns the domain name of the server.
	 * 
	 * @return the domain name of the server.
	 */
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

	/**
	 * The domain name that locally-posted mail appears to come from, and that
	 * locally posted mail is delivered to.
	 * 
	 * @return the origin domain name.
	 */
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

	/**
	 * Returns the list of additional domains that are delivered to local mail
	 * users.
	 * 
	 * @return the {@link Collection} of domains.
	 */
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

	/**
	 * Returns the domains that should be stripped of the sub-domains.
	 * 
	 * @return the {@link MasqueradeDomains}.
	 */
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
		Object file = args.get("file");
		Object keyFile = args.get("key");
		Object caFile = args.get("ca");
		this.certificateFile = certificateFileFactory.create(file, keyFile,
				caFile);
		log.certificateSet(this, certificateFile);
	}

	/**
	 * Returns the network interface addresses that this mail system receives
	 * mail on.
	 * 
	 * @return the {@link BindAddresses}.
	 */
	public BindAddresses getBindAddresses() {
		return bindAddresses;
	}

	/**
	 * Returns the certificate file for TLS.
	 * 
	 * @return the {@link CertificateFile}.
	 */
	public CertificateFile getCertificateFile() {
		return certificateFile;
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

	/**
	 * Returns the to the mail service known domains list.
	 * 
	 * @return an unmodifiable {@link List} of {@link Domain} domains.
	 */
	public List<Domain> getDomains() {
		return unmodifiableList(domains);
	}

	public BindAddresses getAll() {
		return BindAddresses.ALL;
	}

	public BindAddresses getLoopback() {
		return BindAddresses.LOOPBACK;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.toString();
	}

}
