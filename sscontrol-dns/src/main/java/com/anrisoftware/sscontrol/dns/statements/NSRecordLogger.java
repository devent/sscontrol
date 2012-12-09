package com.anrisoftware.sscontrol.dns.statements;

/**
 * Logging messages for {@link NSRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class NSRecordLogger extends AbstractRecordLogger {

	/**
	 * Creates a logger for {@link NSRecord}.
	 */
	NSRecordLogger() {
		super(NSRecord.class);
	}

	void aRecordSet(NSRecord record, ARecord a) {
		if (log.isDebugEnabled()) {
			log.debug("Set the A-record {} for the NS-record {}.", a, record);
		} else {
			log.info("Set the A-record {} for the NS-record {}.", a.getName(),
					record.getName());
		}
	}
}
