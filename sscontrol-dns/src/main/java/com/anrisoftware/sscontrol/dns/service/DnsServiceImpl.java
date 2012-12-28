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
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.split;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

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

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 2828043940753655821L;

	private final DnsServiceImplLogger log;

	private final ServiceLoader<ServiceScriptFactory> serviceScripts;

	@Inject
	private DnsZoneFactory dnsZoneFactory;

	private ProfileService profile;

	private final BindAddresses bindAddresses;

	private final List<DnsZone> zones;

	private int serial;

	private boolean generate;

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
			BindAddresses bindAddresses) {
		this.log = logger;
		this.serviceScripts = serviceScripts;
		this.bindAddresses = bindAddresses;
		this.zones = new ArrayList<DnsZone>();
		this.generate = true;
	}

	/**
	 * Entry point for the DNS service script.
	 * 
	 * @param statements
	 *            the DNS service statements.
	 * 
	 * @return this {@link Service}.
	 */
	public Service dns(Object statements) {
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
	 * @param serial
	 *            the serial.
	 */
	public void serial(int serial) {
		serial(serial, true);
	}

	/**
	 * Sets the serial number of the zone records.
	 * 
	 * @param newSerial
	 *            the serial.
	 * 
	 * @param generate
	 *            if set to {@code true} then the serial number is added to the
	 *            automatically generated serial. The DNS service needs the
	 *            serial number to be updated for all records that have been
	 *            changed. The service can create serial numbers based on the
	 *            current date but the user needs to update this serial number
	 *            if the records are changed more then once in a day.
	 *            <p>
	 *            if set to {@code false} then the serial number is used as
	 *            specified.
	 */
	public void serial(int newSerial, boolean generate) {
		this.serial = newSerial;
		log.serialSet(this, newSerial, generate);
		setGenerate(generate);
	}

	/**
	 * Sets whether the serial should be generated.
	 * <p>
	 * The service can create serial numbers based on the current date but the
	 * user needs to update this serial number if the records are changed more
	 * then once in a day.
	 * 
	 * @param generate
	 *            {@code true} if the serial number should be generated,
	 *            {@code false} if not.
	 */
	public void setGenerate(boolean generate) {
		this.generate = generate;
		log.generateSet(this, generate);
	}

	/**
	 * Sets the IP addresses or host names to where to bind the DNS service.
	 * 
	 * @param hosts
	 *            the IP address or the host names.
	 * 
	 * @throws ServiceException
	 *             if the host name was not found.
	 */
	public void bind_address(String... hosts) throws ServiceException {
		Set<String> list = new HashSet<String>();
		for (String host : hosts) {
			list.addAll(Arrays.asList(split(host, " ;,")));
		}
		bindAddresses.addAll(list);
		log.bindAddressesSet(this, bindAddresses);
	}

	/**
	 * Returns a list of the IP addresses where to bind the DNS service.
	 * 
	 * @return an unmodifiable {@link List} of IP addresses.
	 */
	public List<String> getBindAddresses() {
		return bindAddresses.asList();
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
	 */
	public void zone(String name, String primaryNameServer, String email) {
		zone(name, primaryNameServer, email, (Object) null);
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
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email,
			Object statements) {
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
	 */
	public void zone(String name, String primaryNameServer, String email,
			String address) {
		zone(name, primaryNameServer, email, address, (Object) null);
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
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email,
			String address, Object statements) {
		DnsZone zone = dnsZoneFactory.create(name, primaryNameServer, email,
				getSerial());
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
	 */
	public void zone(String name, String primaryNameServer, String email,
			String address, long ttl) {
		zone(name, primaryNameServer, email, address, ttl, (Object) null);
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
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the {@link DnsZone} DNS zone.
	 */
	public DnsZone zone(String name, String primaryNameServer, String email,
			String address, long ttl, Object statements) {
		DnsZone zone = dnsZoneFactory.create(name, primaryNameServer, email,
				getSerial());
		zones.add(zone);
		ARecord arecord = zone.a_record(name, address, (Object) null);
		arecord.ttl(ttl);
		return zone;
	}

	/**
	 * Returns the serial number.
	 */
	public int getSerial() {
		return generate ? generateSerial(serial) : serial;
	}

	private int generateSerial(int serial) {
		DateTime date = new DateTime();
		String string = format("%d%d%d%02d", date.getYear(),
				date.getMonthOfYear(), date.getDayOfMonth(), serial);
		return Integer.parseInt(string);
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
		return new ToStringBuilder(this)
				.append("profile", profile.getProfileName())
				.append("serial", serial)
				.append("bind addresses", bindAddresses).toString();
	}

}
