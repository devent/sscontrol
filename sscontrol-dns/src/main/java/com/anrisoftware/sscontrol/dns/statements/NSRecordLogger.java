package com.anrisoftware.sscontrol.dns.statements;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link NSRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NSRecordLogger extends AbstractLogger {

	private static final String A_RECORD_SET_INFO = "A-record '{}' set for NS-record '{}'.";
	private static final String A_RECORD_SET = "A-record {} set for {}.";

	/**
	 * Creates a logger for {@link NSRecord}.
	 */
	NSRecordLogger() {
		super(NSRecord.class);
	}

	void aRecordSet(NSRecord record, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug(A_RECORD_SET, a, record);
		} else {
			log.info(A_RECORD_SET_INFO, a.getName(), record.getName());
		}
	}
}
