package com.anrisoftware.sscontrol.dns.zone;

import static com.anrisoftware.sscontrol.dns.zone.ZoneTimeDurationArgsLogger._.minimal_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.dns.time.TimeDuration;

/**
 * Logging messages for {@link TimeDuration}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ZoneTimeDurationArgsLogger extends AbstractLogger {

	enum _ {

		minimal_null("Minimal time cannot be null for %s.");

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
	 * Creates a logger for {@link TimeDuration}.
	 */
	public ZoneTimeDurationArgsLogger() {
		super(TimeDuration.class);
	}

	void checkMinimal(DnsZone zone, Duration time) {
		notNull(time, minimal_null.toString(), zone);
	}

}
