/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.zone;

import static java.util.Collections.unmodifiableList;
import groovy.lang.GroovyObjectSupport;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.globalpom.format.duration.DurationFormatFactory;
import com.anrisoftware.sscontrol.dns.time.TimeDuration;
import com.anrisoftware.sscontrol.dns.time.TimeDurationFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Specifies authoritative information about a DNS zone, including the primary
 * name server, the email of the domain administrator, the domain serial number,
 * and several timers relating to refreshing the zone.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DnsZone extends GroovyObjectSupport implements Serializable {

	private static final String MINIMUM = "minimum";

	private static final String DURATION = "duration";

	private static final String EMAIL = "email";

	private static final String PRIMARY_NAME_SERVER = "primary name server";

	private static final String NAME = "name";

	private static final String SERIAL = "serial";

	private final String name;

	private final String primaryNameServer;

	private final String email;

	private final Long serial;

	private final List<ZoneRecord> records;

	private final Set<ZoneRecord> uniqueRecords;

	@Inject
	private DnsZoneLogger log;

	@Inject
	private Map<Record, RecordFactory> recordFactories;

	@Inject
	private DurationFormatFactory durationFormatFactory;

	@Inject
	private ZoneTimeDurationFactory zoneDurationFactory;

	@Inject
	private TimeDurationFactory durationFactory;

	private ZoneTimeDuration ttl;

	private TimeDuration refresh;

	private TimeDuration retry;

	private TimeDuration expire;

	/**
	 * @see DnsZoneFactory#create(String, String, String, long)
	 */
	@Inject
	DnsZone(DnsZoneArgs zoneArgs, @Assisted Map<String, Object> args,
			@Assisted String name) {
		this.records = new ArrayList<ZoneRecord>();
		this.uniqueRecords = new HashSet<ZoneRecord>();
		this.name = name;
		this.primaryNameServer = zoneArgs.primaryNameServer(args, name);
		this.email = zoneArgs.email(args, name);
		this.serial = zoneArgs.serial(args, name);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @see ZoneTimeDurationFactory#create(DnsZone, Map)
	 * 
	 * @throws ParseException
	 *             if the duration string is not valid ISO 8601.
	 */
	public void ttl(Map<String, Object> args) throws ParseException {
		args.put(DURATION, asDuration(args.get(DURATION)));
		args.put(MINIMUM, asDuration(args.get(MINIMUM)));
		this.ttl = zoneDurationFactory.create(this, args);
		log.ttlSet(this, ttl);
	}

	/**
	 * Sets the refresh time.
	 * 
	 * @see TimeDurationFactory#create(Object, Map)
	 * 
	 * @throws ParseException
	 *             if the duration string is not valid ISO 8601.
	 */
	public void refresh(Map<String, Object> args) throws ParseException {
		args.put(DURATION, asDuration(args.get(DURATION)));
		this.refresh = durationFactory.create(this, args);
		log.refreshSet(this, refresh);
	}

	/**
	 * Sets the retry time.
	 * 
	 * @see TimeDurationFactory#create(Object, Map)
	 * 
	 * @throws ParseException
	 *             if the duration string is not valid ISO 8601.
	 */
	public void retry(Map<String, Object> args) throws ParseException {
		args.put(DURATION, asDuration(args.get(DURATION)));
		this.retry = durationFactory.create(this, args);
		log.retrySet(this, retry);
	}

	/**
	 * Sets the expiration time.
	 * 
	 * @see TimeDurationFactory#create(Object, Map)
	 * 
	 * @throws ParseException
	 *             if the duration string is not valid ISO 8601.
	 */
	public void expire(Map<String, Object> args) throws ParseException {
		args.put(DURATION, asDuration(args.get(DURATION)));
		this.expire = durationFactory.create(this, args);
		log.expireSet(this, expire);
	}

	private Duration asDuration(Object object) throws ParseException {
		log.checkDuration(this, object);
		if (!(object instanceof Duration)) {
			return durationFormatFactory.create().parse(object.toString());
		} else {
			return (Duration) object;
		}
	}

	/**
	 * @see #record(Map, Record, Object)
	 */
	public void record(Map<String, Object> args, Record record) {
		record(args, record, null);
	}

	/**
	 * Adds a new zone record.
	 * 
	 * @param args
	 *            the arguments of the record.
	 * 
	 * @param record
	 *            the {@link Record} type.
	 * 
	 * @return the {@link ZoneRecord}.
	 */
	public ZoneRecord record(Map<String, Object> args, Record record,
			Object statements) {
		ZoneRecord therecord = recordFactories.get(record).create(this, args);
		log.checkRecordUnique(this, uniqueRecords.add(therecord), therecord);
		records.add(therecord);
		log.recordAdded(this, therecord);
		return therecord;
	}

	/**
	 * Returns the name of this zone.
	 *
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of the authoritative master name server for the zone.
	 *
	 * @return the primary name server.
	 */
	public String getPrimaryNameServer() {
		return primaryNameServer;
	}

	/**
	 * Returns the email address of the administrator for the zone.
	 *
	 * @return the administrator email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the serial number of this zone file.
	 *
	 * @return the serial number.
	 */
	public Long getSerial() {
		return serial;
	}

	/**
	 * Returns the time to live time for this zone.
	 *
	 * @return the TTL time {@link Duration}.
	 */
	public Duration getTtl() {
		return ttl == null ? null : ttl.getDuration();
	}

	/**
	 * Returns the refresh time for this zone.
	 *
	 * @return the refresh time {@link Duration}.
	 */
	public Duration getRefresh() {
		return refresh == null ? null : refresh.getDuration();
	}

	/**
	 * Returns the retry time for this zone.
	 *
	 * @return the retry time {@link Duration}.
	 */
	public Duration getRetry() {
		return retry == null ? null : retry.getDuration();
	}

	/**
	 * Returns the expire time for this zone.
	 *
	 * @return the expire time {@link Duration}.
	 */
	public Duration getExpire() {
		return expire == null ? null : expire.getDuration();
	}

	/**
	 * Returns the minimum TTL time for this zone.
	 *
	 * @return the minimum TTL time {@link Duration}.
	 */
	public Duration getMinimumTtl() {
		return ttl == null ? null : ttl.getMinimum();
	}

	/**
	 * Returns the zone records.
	 * 
	 * @return an unmodifiable {@link List} of the {@link ZoneRecord} records.
	 */
	public List<ZoneRecord> getRecords() {
		return unmodifiableList(records);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(SERIAL, serial)
				.append(NAME, name)
				.append(PRIMARY_NAME_SERVER, primaryNameServer)
				.append(EMAIL, email).toString();
	}
}
