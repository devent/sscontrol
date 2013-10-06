package com.anrisoftware.sscontrol.dns.zone;

import java.util.Map;

import javax.inject.Inject;

import org.joda.time.Duration;

/**
 * Checks and returns the arguments of zone time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ZoneTimeDurationArgs {

	static final String MINIMAL = "minimal";

	@Inject
	private ZoneTimeDurationArgsLogger log;

	Duration minimal(DnsZone zone, Map<String, Object> args) {
		Duration minimal = (Duration) args.get(MINIMAL);
		log.checkMinimal(zone, minimal);
		return minimal;
	}
}
