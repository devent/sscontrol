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

	static final String MINIMUM = "minimum";

	@Inject
	private ZoneTimeDurationArgsLogger log;

	Duration minimum(DnsZone zone, Map<String, Object> args) {
		if (args.containsKey(MINIMUM)) {
			Duration minimal = (Duration) args.get(MINIMUM);
			log.checkMinimal(zone, minimal);
			return minimal;
		} else {
			return null;
		}
	}
}
