package com.anrisoftware.sscontrol.dns.cnamerecord;

import static com.anrisoftware.sscontrol.dns.zone.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.dns.zone.DnsZone;

/**
 * Checks and returns the arguments of CNAME/record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CNAMERecordArgs {

	static final String ALIAS = "alias";

	static final String PRIORITY = "priority";

	static final String NAME = "name";

	@Inject
	private CNAMERecordArgsLogger log;

	String name(Map<String, Object> args, DnsZone zone) {
		Object name = args.get(NAME);
		log.checkName(name, zone);
		return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
	}

	String alias(Map<String, Object> args, DnsZone zone) {
		Object name = args.get(ALIAS);
		log.checkAlias(name, zone);
		return replace(name.toString(), ZONE_PLACEHOLDER, zone.getName());
	}

}
