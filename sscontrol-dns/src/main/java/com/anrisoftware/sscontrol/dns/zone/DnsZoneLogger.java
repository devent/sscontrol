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

import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.duration_null;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.email_null;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.expire_set_debug;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.expire_set_info;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.primary_null;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.record_added_debug;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.record_added_info;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.record_unique;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.refresh_set_debug;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.refresh_set_info;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.retry_set_debug;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.retry_set_info;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.serial_null;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.ttl_set_debug;
import static com.anrisoftware.sscontrol.dns.zone.DnsZoneLogger._.ttl_set_info;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.dns.arecord.ARecord;
import com.anrisoftware.sscontrol.dns.time.TimeDuration;

/**
 * Logging messages for {@link DnsZone}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsZoneLogger extends AbstractLogger {

	enum _ {

		primary_null("Primary DNS server of zone '%s' cannot be null."),

		email_null("Email of zone '%s' cannot be null."),

		serial_null("Serial of zone '%s' cannot be null."),

		record_unique("Record %s must be unique for %s."),

		ttl_set_debug("TTL {} set for {}."),

		ttl_set_info("Time to live {} set for zone '{}'."),

		duration_null("Duration time cannot be null for %s."),

		refresh_set_debug("Refresh time {} set for {}."),

		refresh_set_info("Refresh time {} set for zone '{}'."),

		retry_set_debug("Retry time {} set for {}."),

		retry_set_info("Retry time {} set for zone '{}'."),

		expire_set_debug("Expire time {} set for {}."),

		expire_set_info("Expire time {} set for zone '{}'."),

		record_added_debug("Record {} added for {}."),

		record_added_info("{}/record added for zone '{}'.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final String A_RECORD_SET_INFO = "A-record '{}' set for the zone '{}'.";
	private static final String A_RECORD_SET = "A-record {} set for {}.";

	/**
	 * Creates a logger for {@link DnsZone}.
	 */
	DnsZoneLogger() {
		super(DnsZone.class);
	}

	void checkPrimary(Object primary, String name) {
		notNull(primary, primary_null.toString(), name);
	}

	void checkEmail(Object email, String name) {
		notNull(email, email_null.toString(), name);
	}

	void checkSerial(Number serial, String name) {
		notNull(serial, serial_null.toString(), name);
	}

	void aRecordSet(DnsZone zone, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug(A_RECORD_SET, a, zone);
		} else {
			log.info(A_RECORD_SET_INFO, a.getName(), zone.getName());
		}
	}

	void checkDuration(DnsZone zone, Object duration) {
		notNull(duration, duration_null.toString(), zone);
	}

	void ttlSet(DnsZone zone, ZoneTimeDuration duration) {
		if (isDebugEnabled()) {
			debug(ttl_set_debug, duration, zone);
		} else {
			info(ttl_set_info, duration.getDuration(), zone.getName());
		}
	}

	void checkRecordUnique(DnsZone zone, boolean added, ZoneRecord record) {
		isTrue(added, record_unique.toString(), record, zone);
	}

	void refreshSet(DnsZone zone, TimeDuration duration) {
		if (isDebugEnabled()) {
			debug(refresh_set_debug, duration, zone);
		} else {
			info(refresh_set_info, duration.getDuration(), zone.getName());
		}
	}

	void retrySet(DnsZone zone, TimeDuration duration) {
		if (isDebugEnabled()) {
			debug(retry_set_debug, duration, zone);
		} else {
			info(retry_set_info, duration.getDuration(), zone.getName());
		}
	}

	void expireSet(DnsZone zone, TimeDuration duration) {
		if (isDebugEnabled()) {
			debug(expire_set_debug, duration, zone);
		} else {
			info(expire_set_info, duration.getDuration(), zone.getName());
		}
	}

	void recordAdded(DnsZone zone, ZoneRecord record) {
		if (isDebugEnabled()) {
			debug(record_added_debug, record, zone);
		} else {
			info(record_added_info, record.getRecord(), zone.getName());
		}
	}
}
