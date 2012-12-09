package com.anrisoftware.sscontrol.dns.statements;

import static java.lang.String.format;

/**
 * Logging messages for {@link MXRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MXRecordLogger extends AbstractRecordLogger {

	/**
	 * Creates a logger for {@link MXRecord}.
	 */
	MXRecordLogger() {
		super(MXRecord.class);
	}

	MXRecord prioritySet(MXRecord record, long priority) {
		log.debug("Set the priority {} for the MX record {}.", priority, record);
		return record;
	}

	MXRecord aRecordSet(MXRecord record, AbstractRecord a) {
		log.debug("Set the A record {} for the MX record {}.", a, record);
		return record;
	}

	UnsupportedOperationException unsupportedTtl(MXRecord record) {
		UnsupportedOperationException ex = new UnsupportedOperationException(
				format("TTL is not supported for the MX-record %s", record));
		log.error(ex.getLocalizedMessage());
		return ex;
	}
}
