package com.anrisoftware.sscontrol.dns.cnamerecord;

import static com.anrisoftware.sscontrol.dns.cnamerecord.CnameRecordArgsLogger._.alias_null;
import static com.anrisoftware.sscontrol.dns.cnamerecord.CnameRecordArgsLogger._.name_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.dns.mxrecord.MXRecord;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Logging messages for {@link MXRecord}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CnameRecordArgsLogger extends AbstractLogger {

	enum _ {

		name_null("Name cannot be null or blank for CNAME/record of %s."),

		alias_null("Alias cannot be null or blank for CNAME/record of %s.");

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
	 * Creates a logger for {@link MXRecord}.
	 */
	public CnameRecordArgsLogger() {
		super(CnameRecord.class);
	}

	void checkName(Object name, DnsZone zone) {
		notNull(name, name_null.toString(), zone);
		notBlank(name.toString(), name_null.toString(), zone);
	}

	void checkAlias(Object alias, DnsZone zone) {
		notNull(alias, alias_null.toString(), zone);
		notBlank(alias.toString(), alias_null.toString(), zone);
	}

}
