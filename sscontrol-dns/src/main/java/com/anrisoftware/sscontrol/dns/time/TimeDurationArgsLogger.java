/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
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
