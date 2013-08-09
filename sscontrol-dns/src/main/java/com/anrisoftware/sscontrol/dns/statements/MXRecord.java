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
 * Maps a domain name to a list of message transfer agents for that domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class MXRecord extends AbstractRecord {

	private static final String A_RECORD = "A-record";

	private static final String PRIORITY = "priority";

	private static final String NAME = "name";

	private final MXRecordLogger log;

	private final DnsZone zone;

	private final String name;

	private ARecord aRecord;

	private long priority;

	/**
	 * @see MXRecordFactory#create(DnsZone, String)
	 */
	@Inject
	MXRecord(MXRecordLogger logger, @Assisted DnsZone zone,
			@Assisted String name) {
		super(zone);
		this.log = logger;
		this.priority = 10;
		this.zone = zone;
		this.name = replace(name, ZONE_PLACEHOLDER, zone.getName());
	}

	/**
	 * Sets the priority for the MX record.
	 * 
	 * @param newPriority
	 *            the priority.
	 */
	public void priority(long newPriority) {
		priority = newPriority;
		log.prioritySet(this, newPriority);
	}

	/**
	 * Sets the IP address for this MX record. Creates a new A record to map
	 * this host name to the specified address.
	 * 
	 * @param address
	 *            the IP address.
	 * 
	 * @return this {@link MXRecord}.
	 */
	public void setAddress(String address) {
		this.aRecord = zone.a_record(name, address, (Object) null);
		log.aRecordSet(this, aRecord);
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
	 * @return the priority.
	 */
	public long getPriority() {
		return priority;
	}

	/**
	 * Returns the A-record of the MX-record.
	 * 
	 * @return the {@link ARecord} or {@code null} if no A-record was set.
	 */
	public ARecord getARecord() {
		return aRecord;
	}

	@Override
	public String toString() {
		ToStringBuilder s = new ToStringBuilder(this)
				.appendSuper(super.toString()).append(NAME, name)
				.append(PRIORITY, priority);
		return aRecord == null ? s.toString() : s.append(A_RECORD, aRecord)
				.toString();
	}
}
