package com.anrisoftware.sscontrol.dns.arecord;

import static com.anrisoftware.sscontrol.dns.zone.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Checks and returns the arguments of A/record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ARecordArgs {

	public static final String NAME = "name";

	public static final String ADDRESS = "address";

	@Inject
	private ARecordArgsLogger log;

	public String name(Map<String, Object> args, DnsZone zone) {
		Object name = args.get(NAME);
		log.checkName(name, zone);
		return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
	}

	String address(Map<String, Object> args, DnsZone zone) {
		Object name = args.get(ADDRESS);
		log.checkName(name, zone);
		return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
	}
}
