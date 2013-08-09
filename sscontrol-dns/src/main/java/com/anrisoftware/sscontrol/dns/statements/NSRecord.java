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
package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Delegates a DNS zone to use the given authoritative name servers.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class NSRecord extends AbstractRecord {

	private static final String A_RECORD = "A-record";

	private static final String NAME = "name";

	private final NSRecordLogger log;

	private final DnsZone zone;

	private final String name;

	private ARecord aRecord;

	/**
	 * @see NSRecordFactory#create(DnsZone, String)
	 */
	@Inject
	NSRecord(NSRecordLogger logger, @Assisted DnsZone zone,
			@Assisted String name) {
		super(zone);
		this.log = logger;
		this.zone = zone;
		this.name = replace(name, ZONE_PLACEHOLDER, zone.getName());
	}

	/**
	 * Returns the name of the NS-record.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the IP address for this NS-record. We will create a new A-record to
	 * map this host name to the specified address.
	 * 
	 * @param address
	 *            the IP address.
	 */
	public void setAddress(String address) {
		this.aRecord = zone.a_record(name, address, (Object) null);
		log.aRecordSet(this, aRecord);
	}

	@Override
	public String toString() {
		ToStringBuilder s = new ToStringBuilder(this).appendSuper(
				super.toString()).append(NAME, name);
		return aRecord == null ? s.toString() : s.append(A_RECORD, aRecord)
				.toString();
	}
}
