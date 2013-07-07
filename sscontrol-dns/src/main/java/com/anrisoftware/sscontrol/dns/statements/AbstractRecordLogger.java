package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.AbstractRecord.TTL_MAX_SECONDS;
import static com.anrisoftware.sscontrol.dns.statements.AbstractRecord.TTL_MIN_SECONDS;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logger for the zone records.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractRecordLogger extends AbstractLogger {

	private static final String TTL_VALID = "TTL time must be between %d and %d but it is %d for %s.";
	private static final String TTL_SET_INFO = "TTL time {} s set for the zone record {}.";
	private static final String TTL_SET = "TTL time {} s set for {}.";

	/**
	 * Creates a new {@link Logger} for the given {@link AbstractRecord}.
	 */
	AbstractRecordLogger() {
		super(AbstractRecord.class);
	}

	void ttlSet(AbstractRecord record, Duration ttl) {
		if (log.isDebugEnabled()) {
			log.debug(TTL_SET, ttl, record);
		} else {
			log.info(TTL_SET_INFO, ttl, record.getClass().getSimpleName());
		}
	}

	void checkTtl(long time, AbstractRecord record) {
		inclusiveBetween(TTL_MIN_SECONDS, TTL_MAX_SECONDS, time, TTL_VALID,
				TTL_MIN_SECONDS, TTL_MAX_SECONDS, time, record);
	}

}