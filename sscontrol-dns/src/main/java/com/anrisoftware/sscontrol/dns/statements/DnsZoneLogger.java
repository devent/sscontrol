package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.DnsZone.MAX_TIME_SECONDS;
import static com.anrisoftware.sscontrol.dns.statements.DnsZone.MIN_TIME_SECONDS;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link DnsZone}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.1
 */
class DnsZoneLogger extends AbstractSerializedLogger {

	/**
	 * Creates a logger for {@link DnsZone}.
	 */
	DnsZoneLogger() {
		super(DnsZone.class);
	}

	void aRecordSet(DnsZone zone, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug("Set the A-record {} for the zone {}.", a, zone);
		} else {
			log.info("Set the A-record {} for the zone {}.", a.getName(),
					zone.getName());
		}
	}

	DnsZone ttlSet(DnsZone zone, long time) {
		log.debug("Set the time to live time {}s for the zone {}.", time, zone);
		return zone;
	}

	void checkTtl(long time, DnsZone zone) {
		inclusiveBetween(
				MIN_TIME_SECONDS,
				MAX_TIME_SECONDS,
				time,
				"The TTL time of the zone %s must be between %d and %d but it is %d.",
				zone, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time);
	}

	DnsZone refreshTimeSet(DnsZone zone, long time) {
		log.debug("Set the refresh time {}s for the zone {}.", time, zone);
		return zone;
	}

	void checkRefreshTime(long time, DnsZone zone) {
		inclusiveBetween(
				MIN_TIME_SECONDS,
				MAX_TIME_SECONDS,
				time,
				"The refresh time of the zone %s must be between %d and %d but it is %d.",
				zone, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time);
	}

	DnsZone retryTimeSet(DnsZone zone, long time) {
		log.debug("Set the retry time {}s for the zone {}.", time, zone);
		return zone;
	}

	void checkRetryTime(long time, DnsZone zone) {
		inclusiveBetween(
				MIN_TIME_SECONDS,
				MAX_TIME_SECONDS,
				time,
				"The retry time of the zone %s must be between %d and %d but it is %d.",
				zone, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time);
	}

	DnsZone expireTimeSet(DnsZone zone, long time) {
		log.debug("Set the expire time {}s for the zone {}.", time, zone);
		return zone;
	}

	void checkExpireTime(long time, DnsZone zone) {
		inclusiveBetween(
				MIN_TIME_SECONDS,
				MAX_TIME_SECONDS,
				time,
				"The expire time of the zone %s must be between %d and %d but it is %d.",
				zone, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time);
	}

	DnsZone minimumTtlSet(DnsZone zone, long time) {
		log.debug("Set the minimum TTL {}s for the zone {}.", time, zone);
		return zone;
	}

	void checkMinimumTtl(long time, DnsZone zone) {
		inclusiveBetween(
				MIN_TIME_SECONDS,
				MAX_TIME_SECONDS,
				time,
				"The minimum TTL of the zone %s must be between %d and %d but it is %d.",
				zone, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time);
	}

	void nsRecordAdded(DnsZone zone, NSRecord record) {
		if (log.isDebugEnabled()) {
			log.debug("New NS-record {} added for the zone {}.", record, zone);
		} else {
			log.info("New NS-record {} added for the zone {}.",
					record.getName(), zone);
		}
	}

	void aRecordAdded(DnsZone zone, ARecord record) {
		if (log.isDebugEnabled()) {
			log.debug("New A-record {} added for the zone {}.", record, zone);
		} else {
			log.info("New A-record {} added for the zone {}.",
					record.getName(), zone);
		}
	}

	void mxRecordAdded(DnsZone zone, MXRecord record) {
		if (log.isDebugEnabled()) {
			log.debug("New MX-record {} added for the zone {}.", record, zone);
		} else {
			log.info("New MX-record {} added for the zone {}.",
					record.getName(), zone);
		}
	}

	void cnameRecordAdded(DnsZone zone, CNAMERecord record) {
		if (log.isDebugEnabled()) {
			log.debug("New CNAME-record {} added for the zone {}.", record,
					zone);
		} else {
			log.info("New CNAME-record {} added for the zone {}.",
					record.getName(), zone);
		}
	}

}
