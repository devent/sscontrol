package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.DnsZone.MAX_TIME_SECONDS;
import static com.anrisoftware.sscontrol.dns.statements.DnsZone.MIN_TIME_SECONDS;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link DnsZone}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsZoneLogger extends AbstractLogger {

	private static final String CNAME_RECORD_ADDED_INFO = "CNAME-record '{}' added for zone '{}'.";
	private static final String CNAME_RECORD_ADDED = "CNAME-record {} added for {}.";
	private static final String MX_RECORD_ADDED_INFO = "MX-record '{}' added for zone '{}'.";
	private static final String MX_RECORD_ADDED = "MX-record {} added for {}.";
	private static final String A_RECORD_ADDED_INFO = "A-record '{}' added for zone '{}'.";
	private static final String A_RECORD_ADDED = "A-record {} added for {}.";
	private static final String NS_RECORD_ADDED_INFO = "NS-record '{}' added for zone '{}'.";
	private static final String NS_RECORD_ADDED = "NS-record {} added for {}.";
	private static final String MINIMUM_TTL_VALID = "Minimum TTL must be between %d and %d but it is %d for %s.";
	private static final String MINIMUM_TTL_SET_INFO = "Minimum TTL {} s set for zone '{}'.";
	private static final String MINIMUM_TTL_SET = "Minimum TTL {} s set for {}.";
	private static final String EXPIRE_TIME_VALID = "Expire time must be between %d and %d but it is %d for %s.";
	private static final String EXPIRE_TIME_SET_INFO = "Expire time {} s set for zone '{}'.";
	private static final String EXPIRE_TIME_SET = "Expire time {} s set for {}.";
	private static final String RETRY_TIME_VALID = "Retry time must be between %d and %d but it is %d for %s.";
	private static final String RETRY_TIME_SET_INFO = "Retry time {} s set for zone '{}'.";
	private static final String RETRY_TIME_SET = "Retry time {} s set for {}.";
	private static final String REFRESH_TIME_VALID = "Refresh time must be between %d and %d but it is %d for %s.";
	private static final String REFRESH_TIME_SET_INFO = "Refresh time {} s set for zone '{}'.";
	private static final String REFRESH_TIME_SET = "Refresh time {} s set for {}.";
	private static final String TTL_VALID = "TTL must be between %d and %d but it is %d for %s.";
	private static final String TTL_SET_INFO = "TTL {} s set for the zone '{}'.";
	private static final String TTL_SET = "TTL {} s set {}.";
	private static final String A_RECORD_SET_INFO = "A-record '{}' set for the zone '{}'.";
	private static final String A_RECORD_SET = "A-record {} set for {}.";

	/**
	 * Creates a logger for {@link DnsZone}.
	 */
	DnsZoneLogger() {
		super(DnsZone.class);
	}

	void aRecordSet(DnsZone zone, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug(A_RECORD_SET, a, zone);
		} else {
			log.info(A_RECORD_SET_INFO, a.getName(), zone.getName());
		}
	}

	void ttlSet(DnsZone zone, long time) {
		if (log.isDebugEnabled()) {
			log.debug(TTL_SET, time, zone);
		} else {
			log.info(TTL_SET_INFO, time, zone);
		}
	}

	void checkTtl(long time, DnsZone zone) {
		inclusiveBetween(MIN_TIME_SECONDS, MAX_TIME_SECONDS, time, TTL_VALID,
				MIN_TIME_SECONDS, MAX_TIME_SECONDS, time, zone);
	}

	void refreshTimeSet(DnsZone zone, long time) {
		if (log.isDebugEnabled()) {
			log.debug(REFRESH_TIME_SET, time, zone);
		} else {
			log.info(REFRESH_TIME_SET_INFO, time, zone);
		}
	}

	void checkRefreshTime(long time, DnsZone zone) {
		inclusiveBetween(MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				REFRESH_TIME_VALID, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				zone);
	}

	void retryTimeSet(DnsZone zone, long time) {
		if (log.isDebugEnabled()) {
			log.debug(RETRY_TIME_SET, time, zone);
		} else {
			log.info(RETRY_TIME_SET_INFO, time, zone);
		}
	}

	void checkRetryTime(long time, DnsZone zone) {
		inclusiveBetween(MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				RETRY_TIME_VALID, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				zone);
	}

	void expireTimeSet(DnsZone zone, long time) {
		if (log.isDebugEnabled()) {
			log.debug(EXPIRE_TIME_SET, time, zone);
		} else {
			log.info(EXPIRE_TIME_SET_INFO, time, zone);
		}
	}

	void checkExpireTime(long time, DnsZone zone) {
		inclusiveBetween(MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				EXPIRE_TIME_VALID, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				zone);
	}

	void minimumTtlSet(DnsZone zone, long time) {
		if (log.isDebugEnabled()) {
			log.debug(MINIMUM_TTL_SET, time, zone);
		} else {
			log.info(MINIMUM_TTL_SET_INFO, time, zone);
		}
	}

	void checkMinimumTtl(long time, DnsZone zone) {
		inclusiveBetween(MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				MINIMUM_TTL_VALID, MIN_TIME_SECONDS, MAX_TIME_SECONDS, time,
				zone);
	}

	void nsRecordAdded(DnsZone zone, NSRecord record) {
		if (log.isDebugEnabled()) {
			log.debug(NS_RECORD_ADDED, record, zone);
		} else {
			log.info(NS_RECORD_ADDED_INFO, record.getName(), zone);
		}
	}

	void aRecordAdded(DnsZone zone, ARecord record) {
		if (log.isDebugEnabled()) {
			log.debug(A_RECORD_ADDED, record, zone);
		} else {
			log.info(A_RECORD_ADDED_INFO, record.getName(), zone);
		}
	}

	void mxRecordAdded(DnsZone zone, MXRecord record) {
		if (log.isDebugEnabled()) {
			log.debug(MX_RECORD_ADDED, record, zone);
		} else {
			log.info(MX_RECORD_ADDED_INFO, record.getName(), zone);
		}
	}

	void cnameRecordAdded(DnsZone zone, CNAMERecord record) {
		if (log.isDebugEnabled()) {
			log.debug(CNAME_RECORD_ADDED, record, zone);
		} else {
			log.info(CNAME_RECORD_ADDED_INFO, record.getName(), zone);
		}
	}

}
