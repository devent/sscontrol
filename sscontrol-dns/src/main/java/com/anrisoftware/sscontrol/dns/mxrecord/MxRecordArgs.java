/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
