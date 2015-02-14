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

	public String email(Map<String, Object> args, String name) {
		if (args.containsKey(EMAIL)) {
			Object email = args.get(EMAIL);
			log.checkEmail(email, name);
			return replace(email.toString(), ZONE_PLACEHOLDER, name);
		} else {
			return null;
		}
	}

	public Long serial(Map<String, Object> args, String name) {
		if (args.containsKey(SERIAL)) {
			Number serial = (Number) args.get(SERIAL);
			log.checkSerial(serial, name);
			return serial.longValue();
		} else {
			return null;
		}
	}

}
