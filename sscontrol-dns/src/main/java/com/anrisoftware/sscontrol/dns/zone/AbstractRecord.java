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
package com.anrisoftware.sscontrol.dns.zone;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

/**
 * Saves the zone and the time to live time for the record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractRecord implements ZoneRecord, Serializable {

	private static final String TTL = "ttl [s]";

	private static final String ZONE = "zone";

	private final DnsZone zone;

	private AbstractRecordLogger log;

	private Duration ttl;

	/**
	 * Sets the DNS zone to which this record belongs to.
	 * 
	 * @param zone
	 *            the {@link DnsZone}.
	 * 
	 */
	protected AbstractRecord(DnsZone zone) {
		this.zone = zone;
	}

	/**
	 * Injects the logger for this record.
	 * 
	 * @param logger
	 *            the {@link AbstractRecordLogger}.
	 */
	@Inject
	void setAbstractRecordLogger(AbstractRecordLogger logger) {
		this.log = logger;
	}

	@Override
	public void ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, ttl);
	}

	@Override
	public void ttl(Duration time) {
		ttl = time;
		log.ttlSet(this, time);
	}

	@Override
	public DnsZone getZone() {
		return zone;
	}

	@Override
	public Duration getTtl() {
		return ttl;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ZONE, zone.getName())
				.append(TTL, ttl).toString();
	}

}