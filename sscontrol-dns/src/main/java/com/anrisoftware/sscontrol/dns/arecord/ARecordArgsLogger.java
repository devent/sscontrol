package com.anrisoftware.sscontrol.dns.arecord;

import static com.anrisoftware.sscontrol.dns.arecord.ARecordArgsLogger._.name_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Logging messages for {@link ARecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ARecordArgsLogger extends AbstractLogger {

	enum _ {

		name_null("Name cannot be null or blank for A/record of %s.");

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
	 * Creates a logger for {@link ARecord}.
	 */
	public ARecordArgsLogger() {
		super(ARecord.class);
	}

	void checkName(Object name, DnsZone zone) {
		notNull(name, name_null.toString(), zone);
		notBlank(name.toString(), name_null.toString(), zone);
	}

}
