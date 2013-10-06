package com.anrisoftware.sscontrol.dns.time;

import static com.anrisoftware.sscontrol.dns.time.TimeDurationArgsLogger._.duration_null;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link TimeDuration}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class TimeDurationArgsLogger extends AbstractLogger {

	enum _ {

		duration_null("Duration time cannot be null for %s.");

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
	public TimeDurationArgsLogger() {
		super(TimeDuration.class);
	}

	void checkDuration(Object parent, Duration time) {
		notNull(time, duration_null.toString(), parent);
	}

}
