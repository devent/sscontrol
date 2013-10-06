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
