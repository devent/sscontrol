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

import groovy.lang.GroovyObjectSupport;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.DateContextProperties;
import com.anrisoftware.sscontrol.dns.service.DnsPropertiesProvider;

/**
 * Saves the zone and the time to live time for the record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
abstract class AbstractRecord extends GroovyObjectSupport implements
		Serializable {

	private static final String TTL = "ttl [s]";

	private static final String ZONE = "zone";

	static final long TTL_MIN_SECONDS = 1;

	static final long TTL_MAX_SECONDS = Long.MAX_VALUE;

	private static final String TTL_PROPERTY = "default_ttl";

	private AbstractRecordLogger log;

	private final DnsZone zone;

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
	 * Injects the DNS service properties for the record.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl}</dt>
	 *            <dd>the default TTL time for the record.</dd>
	 *            </dl>
	 */
	@Inject
	public final void setDnsServiceProperties(DnsPropertiesProvider p) {
		ContextProperties pp = p.get();
		setupDefaultTimes(new DateContextProperties(pp.getContext(), pp));
	}

	private void setupDefaultTimes(DateContextProperties p) {
		this.ttl = p.getDurationProperty(TTL_PROPERTY);
	}

	/**
	 * Injects the logger for this record.
	 * 
	 * @param logger
	 *            the {@link AbstractRecordLogger}.
	 */
	@Inject
	public final void setAbstractRecordLogger(AbstractRecordLogger logger) {
		this.log = logger;
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	public void ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, ttl);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param time
	 *            the {@link Duration} duration to live time.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	public void ttl(Duration time) {
		ttl = time;
		log.ttlSet(this, time);
	}

	/**
	 * Returns the zone to which this record belongs to.
	 * 
	 * @return the {@link DnsZone}.
	 */
	public DnsZone getZone() {
		return zone;
	}

	/**
	 * Returns the time to live for the record.
	 * 
	 * @return the time to live {@link Duration}.
	 */
	public Duration getTtl() {
		return ttl;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ZONE, zone.getName())
				.append(TTL, ttl).toString();
	}

}