package com.anrisoftware.sscontrol.dns.mxrecord;

import static com.anrisoftware.sscontrol.dns.zone.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Checks and returns the arguments of MX/record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class MxRecordArgs {

	static final String PRIORITY = "priority";

	static final String NAME = "name";

	static final String ADDRESS = "address";

	@Inject
	private MxRecordArgsLogger log;

	String name(Map<String, Object> args, DnsZone zone) {
		Object name = args.get(NAME);
		log.checkName(name, zone);
		return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
	}

	Long priority(Map<String, Object> args, DnsZone zone) {
		if (args.containsKey(PRIORITY)) {
			long priority = ((Number) args.get(PRIORITY)).longValue();
			log.checkPriority(priority, zone);
			return priority;
		} else {
			return null;
		}
	}

	String address(Map<String, Object> args, DnsZone zone) {
		if (args.containsKey(ADDRESS)) {
			Object name = args.get(ADDRESS);
			log.checkName(name, zone);
			return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
		} else {
			return null;
		}
	}
}
