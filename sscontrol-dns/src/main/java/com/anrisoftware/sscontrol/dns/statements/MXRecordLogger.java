package com.anrisoftware.sscontrol.dns.statements;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link MXRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MXRecordLogger extends AbstractLogger {

	private static final String TTL_NOT_SUPPORTED_MESSAGE = "TTL not supported for MX-record '{}'.";
	private static final String TTL_NOT_SUPPORTED = "TTL not supported for MX-record";
	private static final String A_RECORD_SET_INFO = "A-record '{}' set for MX record '{}'.";
	private static final String A_RECORD_SET = "A-record {} set for {}.";
	private static final String PRIORITY_SET_INFO = "Priority {} set for MX record '{}'.";
	private static final String PRIORITY_SET = "Priority {} set for {}.";

	/**
	 * Creates a logger for {@link MXRecord}.
	 */
	MXRecordLogger() {
		super(MXRecord.class);
	}

	void prioritySet(MXRecord record, long priority) {
		if (log.isDebugEnabled()) {
			log.debug(PRIORITY_SET, priority, record);
		} else {
			log.info(PRIORITY_SET_INFO, priority, record.getName());
		}
	}

	void aRecordSet(MXRecord record, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug(A_RECORD_SET, a, record);
		} else {
			log.info(A_RECORD_SET_INFO, a.getName(), record.getName());
		}
	}

	UnsupportedOperationException unsupportedTtl(MXRecord record) {
		return logException(
				new UnsupportedOperationException(TTL_NOT_SUPPORTED),
				TTL_NOT_SUPPORTED_MESSAGE, record.getName());
	}
}
