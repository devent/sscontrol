package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.AbstractRecord.TTL_MAX_SECONDS;
import static com.anrisoftware.sscontrol.dns.statements.AbstractRecord.TTL_MIN_SECONDS;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logger for the zone records.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractRecordLogger extends AbstractSerializedLogger {

	/**
	 * Creates a new {@link Logger} for the given {@link AbstractRecord}.
	 */
	AbstractRecordLogger(Class<? extends AbstractRecord> contextClass) {
		super(contextClass);
	}

	void ttlSet(AbstractRecord record, Duration ttl) {
		if (log.isDebugEnabled()) {
			log.debug("Set the TTL time {}s for the zone record {}.", ttl,
					record);
		} else {
			log.info("Set the TTL time {}s for the zone record {}.", ttl,
					record);
		}
	}

	void checkTtl(long time, AbstractRecord record) {
		inclusiveBetween(
				TTL_MIN_SECONDS,
				TTL_MAX_SECONDS,
				time,
				"The TTL time of the zone record %s must be between %d and %d but it is %d.",
				record, TTL_MIN_SECONDS, TTL_MAX_SECONDS, time);
	}

}