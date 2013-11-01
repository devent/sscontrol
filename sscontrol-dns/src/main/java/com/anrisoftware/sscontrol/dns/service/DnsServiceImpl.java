/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-dns.
 * 
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-dns is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service;

import static com.anrisoftware.sscontrol.dns.service.DnsServiceFactory.NAME;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneArgs.ADDRESS;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import groovy.lang.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.service.AbstractService;
import com.anrisoftware.sscontrol.dns.aliases.Alias;
import com.anrisoftware.sscontrol.dns.aliases.AliasFactory;
import com.anrisoftware.sscontrol.dns.aliases.Aliases;
import com.anrisoftware.sscontrol.dns.bindings.Binding;
import com.anrisoftware.sscontrol.dns.bindings.BindingAddress;
import com.anrisoftware.sscontrol.dns.bindings.BindingFactory;
import com.anrisoftware.sscontrol.dns.recursive.Recursive;
import com.anrisoftware.sscontrol.dns.roots.Roots;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;
import com.anrisoftware.sscontrol.dns.zone.DnsZoneFactory;
import com.anrisoftware.sscontrol.dns.zone.Record;

/**
 * DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
class DnsServiceImpl extends AbstractService {

	private static final String BINDINGS = "bindings";

	private static final String SERIAL = "serial";

	@Inject
	private DnsServiceImplLogger log;

	@Inject
	private DnsZoneFactory zoneFactory;

	@Inject
	private AliasFactory aliasFactory;

	@Inject
	private BindingFactory bindingFactory;

	@Inject
	private Binding binding;

	@Inject
	private Aliases aliases;

	@Inject
	private Roots roots;

	@Inject
	private Recursive recursive;

	private final List<DnsZone> zones;

	private int serial;

	private boolean generate;

	DnsServiceImpl() {
		this.zones = new ArrayList<DnsZone>();
		this.generate = true;
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
	 * Returns the DNS service name.
	 */
	@Override
	public String getName() {
		return NAME;
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
	 * 
	 * @param serial
	 *            the serial.
	 * 
	 * @param args
	 *            the {@link Map} of additional named parameter:
	 *            <dl>
	 *            <dt>generate</dt>
	 *            <dd>if set to {@code true} then the serial number is added to
	 *            the automatically generated serial. The DNS service needs the
	 *            serial number to be updated for all records that have been
	 *            changed. The service can create serial numbers based on the
	 *            current date but the user needs to update this serial number
	 *            if the records are changed more then once in a day. If set to
	 *            {@code false} then the serial number is used as specified.</dd>
	 *            </dl>
	 * 
	 */
	public void serial(Map<String, Object> args, int serial) {
		if (args.containsKey("generate")) {
			setGenerate((Boolean) args.get("generate"));
		}
		serial(serial);
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
		this.serial = serial;
		log.serialSet(this, serial, generate);
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
	 * @see BindingFactory#create(Map, String...)
	 */
	public void bind(Map<String, Object> args) throws ServiceException {
		this.binding = bindingFactory.create(args);
		log.bindingSet(this, binding);
	}

	/**
	 * Sets the IP addresses or host names to where to bind the DNS service.
	 * 
	 * @see BindingFactory#create(Map, String...)
	 */
	public void bind(Map<String, Object> args, String... array)
			throws ServiceException {
		this.binding = bindingFactory.create(args, array);
		log.bindingSet(this, binding);
	}

	/**
	 * Sets the IP addresses or host names to where to bind the DNS service.
	 * 
	 * @see BindingFactory#create(BindingAddress)
	 */
	public void bind(BindingAddress address) throws ServiceException {
		this.binding = bindingFactory.create(address);
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
	 * Adds a new DNS zone.
	 * 
	 * @see DnsZoneFactory#create(Map, String)
	 * 
	 * @return the {@link DnsZone}.
	 */
	public void zone(Map<String, Object> args, String name) {
		zone(args, name, null);
	}

	/**
	 * Adds a new DNS zone.
	 * 
	 * @see DnsZoneFactory#create(Map, String)
	 * 
	 * @return the {@link DnsZone}.
	 */
	public DnsZone zone(Map<String, Object> args, String name, Object statements) {
		if (!args.containsKey(SERIAL)) {
			args.put(SERIAL, getSerial());
		}
		DnsZone zone = zoneFactory.create(args, name);
		zones.add(zone);
		if (args.containsKey(ADDRESS)) {
			args.put(NAME, name);
			zone.record(args, Record.a, (Object) null);
		}
		log.zoneAdded(this, zone);
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
	 * Adds a new alias.
	 * 
	 * @param name
	 *            the name of the alias.
	 * 
	 * @return the {@link Alias}.
	 */
	public Alias alias(String name) {
		Alias alias = aliasFactory.create();
		alias.setName(name);
		aliases.addAlias(alias);
		return alias;
	}

	/**
	 * Returns the aliases.
	 * 
	 * @return the {@link Aliases}.
	 */
	public Aliases getAliases() {
		return aliases;
	}

	/**
	 * Returns the root servers.
	 * 
	 * @param statements
	 *            the roots statements.
	 * 
	 * @return the {@link Roots}.
	 */
	public Roots roots(Object statements) {
		return roots;
	}

	/**
	 * Returns the root servers.
	 * 
	 * @return the {@link Roots}.
	 */
	public Roots getRoots() {
		return roots;
	}

	/**
	 * Returns the recursive servers.
	 * 
	 * @param statements
	 *            the recursive statements.
	 * 
	 * @return the {@link Recursive}.
	 */
	public Recursive recursive(Object statements) {
		return recursive;
	}

	/**
	 * Returns the recursive servers.
	 * 
	 * @return the {@link Recursive}.
	 */
	public Recursive getRecursive() {
		return recursive;
	}

	/**
	 * @see BindingAddress#local
	 */
	public BindingAddress getLocal() {
		return BindingAddress.local;
	}

	/**
	 * @see BindingAddress#all
	 */
	public BindingAddress getAll() {
		return BindingAddress.all;
	}

	/**
	 * @see Record#a
	 */
	public Record getA() {
		return Record.a;
	}

	/**
	 * @see Record#cname
	 */
	public Record getCname() {
		return Record.cname;
	}

	/**
	 * @see Record#mx
	 */
	public Record getMx() {
		return Record.mx;
	}

	/**
	 * @see Record#ns
	 */
	public Record getNs() {
		return Record.ns;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append(SERIAL, serial).append(BINDINGS, binding).toString();
	}

}
