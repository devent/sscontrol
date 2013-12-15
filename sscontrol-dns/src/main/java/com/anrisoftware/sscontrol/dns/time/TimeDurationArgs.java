/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Map;

import javax.inject.Inject;

import org.joda.time.Duration;

/**
 * Checks and returns the arguments of time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TimeDurationArgs {

	static final String DURATION = "duration";

	@Inject
	private TimeDurationArgsLogger log;

	Duration duration(Object parent, Map<String, Object> args) {
		Duration duration = (Duration) args.get(DURATION);
		log.checkDuration(parent, duration);
		return duration;
	}
}
