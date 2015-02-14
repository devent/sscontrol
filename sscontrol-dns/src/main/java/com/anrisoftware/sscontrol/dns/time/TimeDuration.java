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
package com.anrisoftware.sscontrol.dns.time;

import static com.anrisoftware.sscontrol.dns.time.TimeDurationArgs.DURATION;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.google.inject.assistedinject.Assisted;

/**
 * Time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TimeDuration {

	private final Object parent;

	private Duration duration;

	private Map<String, Object> args;

	/**
	 * @see TimeDurationFactory#create(Map)
	 */
	@Inject
	protected TimeDuration(@Assisted Object parent,
			@Assisted Map<String, Object> args) {
		this.parent = parent;
		this.args = args;
	}

	@Inject
	void setTimeDurationArgs(TimeDurationArgs aargs) {
		this.duration = aargs.duration(parent, args);
		this.args = null;
	}

	/**
	 * Returns the time duration.
	 * 
	 * @return the {@link Duration}.
	 */
	public Duration getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(DURATION, duration).toString();
	}
}
