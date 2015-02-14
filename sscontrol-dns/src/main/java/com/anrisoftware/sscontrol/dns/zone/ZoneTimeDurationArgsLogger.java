/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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
