package com.anrisoftware.sscontrol.mail.resetdomains;

import static com.anrisoftware.sscontrol.mail.resetdomains.ResetDomainsLogger._.flag_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link ResetDomains}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ResetDomainsLogger extends AbstractLogger {

	enum _ {

		flag_null("Flag cannot be null.");

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
	 * Creates a logger for {@link ResetDomains}.
	 */
	public ResetDomainsLogger() {
		super(ResetDomains.class);
	}

	void checkFlag(Object flag) {
		notNull(flag, flag_null.toString());
	}

}
