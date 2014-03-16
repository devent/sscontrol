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
package com.anrisoftware.sscontrol.dns.zone;

import static com.anrisoftware.sscontrol.dns.zone.ZoneTimeDurationArgs.MINIMUM;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.sscontrol.dns.time.TimeDuration;
import com.google.inject.assistedinject.Assisted;

/**
 * Zone time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ZoneTimeDuration extends TimeDuration {

	private final Duration minimum;

	/**
	 * @see ZoneTimeDurationFactory#create(DnsZone, Map)
	 */
	@Inject
	ZoneTimeDuration(ZoneTimeDurationArgs aargs, @Assisted DnsZone zone,
			@Assisted Map<String, Object> args) {
		super(zone, args);
		this.minimum = aargs.minimum(zone, args);
	}

	public Duration getMinimum() {
		return minimum;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append(MINIMUM, minimum).toString();
	}
}
