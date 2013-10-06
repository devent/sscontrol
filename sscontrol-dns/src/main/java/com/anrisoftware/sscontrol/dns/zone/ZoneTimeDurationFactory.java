package com.anrisoftware.sscontrol.dns.zone;

import java.util.Map;

/**
 * Factory to create the zone time duration.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface ZoneTimeDurationFactory {

	/**
	 * Creates the zone time duration.
	 * 
	 * @param zone
	 *            the {@link DnsZone} zone.
	 * 
	 * @param args
	 *            the {@link Map} arguments.
	 * 
	 * @return the {@link ZoneTimeDuration}.
	 */
	ZoneTimeDuration create(DnsZone zone, Map<String, Object> args);
}
