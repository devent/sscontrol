package com.anrisoftware.sscontrol.dns.mxrecord;

import static com.anrisoftware.sscontrol.dns.mxrecord.MxRecordArgsLogger._.name_null;
import static com.anrisoftware.sscontrol.dns.mxrecord.MxRecordArgsLogger._.priority_negative;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Logging messages for {@link MxRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class MxRecordArgsLogger extends AbstractLogger {

	enum _ {

		name_null("Name cannot be null or blank for MX/record of %s."),

		priority_negative("Priority must be positive for MX/record of %s.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link MxRecord}.
	 */
	public MxRecordArgsLogger() {
		super(MxRecord.class);
	}

	void checkName(Object name, DnsZone zone) {
		notNull(name, name_null.toString(), zone);
		notBlank(name.toString(), name_null.toString(), zone);
	}

	void checkPriority(long priority, DnsZone zone) {
		isTrue(priority > -1, priority_negative.toString(), zone);
	}

}
