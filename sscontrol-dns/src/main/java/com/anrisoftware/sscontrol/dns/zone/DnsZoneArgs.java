package com.anrisoftware.sscontrol.dns.zone;

import static com.anrisoftware.sscontrol.dns.zone.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import java.util.Map;

import javax.inject.Inject;

/**
 * Checks and returns the arguments of the DNS/zone.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DnsZoneArgs {

	public static final String EMAIL = "email";

	public static final String PRIMARY = "primary";

	public static final String SERIAL = "serial";

	public static final String ADDRESS = "address";

	@Inject
	private DnsZoneLogger log;

	public String primaryNameServer(Map<String, Object> args, String name) {
		if (args.containsKey(PRIMARY)) {
			Object primary = args.get(PRIMARY);
			log.checkPrimary(primary, name);
			return replace(primary.toString(), ZONE_PLACEHOLDER, name);
		} else {
			return null;
		}
	}

	String email(Map<String, Object> args, String name) {
		if (args.containsKey(EMAIL)) {
			Object email = args.get(EMAIL);
			log.checkEmail(email, name);
			return replace(email.toString(), ZONE_PLACEHOLDER, name);
		} else {
			return null;
		}
	}

	Long serial(Map<String, Object> args, String name) {
		if (args.containsKey(SERIAL)) {
			Number serial = (Number) args.get(SERIAL);
			log.checkSerial(serial, name);
			return serial.longValue();
		} else {
			return null;
		}
	}

}
