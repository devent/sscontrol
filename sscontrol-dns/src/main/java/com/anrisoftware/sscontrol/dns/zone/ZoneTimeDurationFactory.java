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
