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
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.mxrecord;

import static com.anrisoftware.sscontrol.dns.mxrecord.MxRecordArgs.ADDRESS;
import static com.anrisoftware.sscontrol.dns.mxrecord.MxRecordArgs.NAME;
import static com.anrisoftware.sscontrol.dns.mxrecord.MxRecordArgs.PRIORITY;
import static com.anrisoftware.sscontrol.dns.zone.Record.a;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.dns.zone.AbstractRecord;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;
import com.anrisoftware.sscontrol.dns.zone.Record;
import com.anrisoftware.sscontrol.dns.zone.ZoneRecord;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Maps a domain name to a list of message transfer agents for that domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class MxRecord extends AbstractRecord {

	private final String name;

	private final Long priority;

	private ZoneRecord address;

	/**
	 * @see MxRecordFactory#create(DnsZone, Map)
	 */
	@Inject
	MxRecord(MxRecordArgs aargs, @Assisted DnsZone zone,
			@Assisted Map<String, Object> args) {
		super(zone);
		this.name = aargs.name(args, zone);
		this.priority = aargs.priority(args, zone);
		address(aargs.address(args, zone));
	}

	private void address(String address) {
		if (address == null) {
			return;
		}
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(NAME, getName());
		args.put(ADDRESS, address);
		this.address = getZone().record(args, a, null);
	}

	@Override
	public void ttl(Map<String, Object> args) throws ParseException {
		super.ttl(args);
		if (address != null) {
			address.ttl(args);
		}
	}

	@Override
	public Record getRecord() {
		return Record.mx;
	}

	/**
	 * Returns the name of the MX-record.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the priority for the MX-record.
	 * 
	 * @return the priority or {@code null}.
	 */
	public Long getPriority() {
		return priority;
	}

	/**
	 * Returns the address of the MX/record.
	 * 
	 * @return the {@link ZoneRecord} or {@code null} if no address was set.
	 */
	public ZoneRecord getAddress() {
		return address;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this)
				.appendSuper(super.toString()).append(NAME, name)
				.append(PRIORITY, priority);
		return address == null ? builder.toString() : builder.append(ADDRESS,
				address).toString();
	}
}
