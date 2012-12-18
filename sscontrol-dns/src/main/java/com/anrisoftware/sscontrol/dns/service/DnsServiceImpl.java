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
package com.anrisoftware.sscontrol.dns.service;

import static com.anrisoftware.sscontrol.dns.service.DnsFactory.NAME;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.anrisoftware.sscontrol.dns.statements.ARecord;
import com.anrisoftware.sscontrol.dns.statements.DnsZone;
import com.anrisoftware.sscontrol.dns.statements.DnsZoneFactory;

/**
 * DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsServiceImpl extends GroovyObjectSupport implements Service {

	private static final String BIND_ADDRESSES_PROPERTY = "default_bind_addresses";

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 2828043940753655821L;

	private final DnsServiceImplLogger log;

	private final ServiceLoader<ServiceScriptFactory> serviceScripts;

	@Inject
	private DnsZoneFactory dnsZoneFactory;

	private ProfileService profile;

	private final List<String> bindAddresses;

	private final List<DnsZone> zones;

	private int serial;

	/**
	 * Sets the default DNS service properties.
	 * 
	 * @param logger
	 *            the {@link DnsServiceImplLogger} for logging messages.
	 * 
	 * @param scripts
	 *            the {@link Map} with the DNS service {@link Script} scripts.
	 * 
	 * @param templates
	 *            the {@link TemplatesFactory} to create new templates
	 *            resources.
	 * 
	 * @param p
	 *            the default DNS service {@link ContextProperties} properties.
	 *            Expects the following properties:
	 *            <dl>
	 *            <dt>
	 *            {@code com.anrisoftware.sscontrol.dns.service.default_bind_addresses}
	 *            </dt>
	 *            <dd>A list of the default bind addresses.</dd>
	 *            </dl>
	 */
	@Inject
	DnsServiceImpl(DnsServiceImplLogger logger,
			ServiceLoader<ServiceScriptFactory> serviceScripts,
			@Named("dns-defaults-properties") ContextProperties p) {
		this.log = logger;
		this.serviceScripts = serviceScripts;
		this.bindAddresses = new ArrayList<String>();
		this.zones = new ArrayList<DnsZone>();
		setDefaultBindAddresses(p);
	}

	private void setDefaultBindAddresses(ContextProperties p) {
		bindAddresses.addAll(p.getListProperty(BIND_ADDRESSES_PROPERTY));
	}

	/**
	 * Entry point for the DNS service script.
	 * 
	 * @return this {@link Service}.
	 */
	public Object dns() {
		return this;
	}

	/**
	 * Sets the serial number of the zone records.
	 * <p>
	 * The serial number can be any number, it is added to the automatically
	 * generated serial. The DNS service needs the serial number to be updated
	 * for all records that have been changed. The service can create serial
	 * numbers based on the current date but the user needs to update this
	 * serial number if the records are changed more then once in a day.
	 * 
	 * @param newSerial
	 *            the serial.
	 * 
	 * @return this {@link Service}.
	 */
	public Object serial(int newSerial) {
		serial = newSerial;
		log.serialSet(this, serial);
		return this;
	}

	/**
	 * Sets the IP address to where to bind the DNS service.
	 * 
	 * @param hosts
	 *            the IP address or the host name.
	 * 
	 * @return this {@link Service}.
	 * 
	 * @throws ServiceException
	 *             if the host name was not found.
	 */
	public Object bind_address(String... hosts) throws ServiceException {
		List<String> list = new ArrayList<String>();
		for (String host : hosts) {
			list.addAll(Arrays.asList(split(host, " ;,")));
		}
		bindAddresses.addAll(list);
		log.bindAddressesSet(this, list);
		return this;
	}

	/**
	 * Returns a list of the IP addresses where to bind the DNS service.
	 * 
	 * @return an unmodifiable {@link List} of IP addresses.
	 */
	public List<String> getBindAddresses() {
		return unmodifiableList(bindAddresses);
	}

	/**
	 * Adds a new DNS zone.
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primaryNameServer
	 *            the name of the primary DNS server.
	 * 
	 * @param email
	 *            the email address for the zone.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email) {
		DnsZone zone = dnsZoneFactory.create(name, primaryNameServer, email,
				serial);
		zones.add(zone);
		return zone;
	}

	/**
	 * Adds a new DNS zone with an A-record. The A-record is created with the
	 * name of the zone and the specified IP address.
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primaryNameServer
	 *            the name of the primary DNS server.
	 * 
	 * @param email
	 *            the email address for the zone.
	 * 
	 * @param address
	 *            the IP address for the zone.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email,
			String address) {
		DnsZone zone = dnsZoneFactory.create(name, primaryNameServer, email,
				serial);
		zones.add(zone);
		zone.a_record(name, address);
		return zone;
	}

	/**
	 * Adds a new DNS zone with an A-record. The A-record is created with the
	 * name of the zone and the specified IP address. The A-record will have the
	 * specified TTL.
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primaryNameServer
	 *            the name of the primary DNS server.
	 * 
	 * @param email
	 *            the email address for the zone.
	 * 
	 * @param address
	 *            the IP address for the zone.
	 * 
	 * @param ttl
	 *            the TTL for the A-record.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email,
			String address, long ttl) {
		DnsZone zone = dnsZoneFactory.create(name, primaryNameServer, email,
				serial);
		zones.add(zone);
		ARecord arecord = zone.a_record(name, address);
		arecord.ttl(ttl);
		return zone;
	}

	/**
	 * Returns a list of the DNS zones.
	 * 
	 * @return an unmodifiable {@link List} of {@link DnsZone} DNS zones.
	 */
	public List<DnsZone> getZones() {
		return unmodifiableList(zones);
	}

	/**
	 * Returns the DNS service name.
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Sets the profile for the DNS service.
	 * 
	 * @param newProfile
	 *            the {@link ProfileService}.
	 */
	public void setProfile(ProfileService newProfile) {
		profile = newProfile;
		log.profileSet(this, newProfile);
	}

	/**
	 * Returns the profile for the DNS service.
	 * 
	 * @return the {@link ProfileService}.
	 */
	public ProfileService getProfile() {
		return profile;
	}

	@Override
	public Service call() throws ServiceException {
		ServiceScriptFactory scriptFactory = findScriptFactory();
		Script script = (Script) scriptFactory.getScript();
		script.setProperty("system", profile.getEntry("system"));
		script.setProperty("profile", profile.getEntry(NAME));
		script.setProperty("service", this);
		script.setProperty("name", profile.getProfileName());
		script.run();
		return this;
	}

	private ServiceScriptFactory findScriptFactory() throws ServiceException {
		String name = profile.getProfileName();
		String service = profile.getEntry(NAME).get("service").toString();
		for (ServiceScriptFactory scriptFactory : serviceScripts) {
			ServiceScriptInfo info = scriptFactory.getInfo();
			if (info.getProfileName().equals(name)
					&& info.getServiceName().equals(service)) {
				return scriptFactory;
			}
		}
		throw log.errorFindServiceScript(this, name, service);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("serial", serial)
				.append("bind addresses", bindAddresses).toString();
	}

}
