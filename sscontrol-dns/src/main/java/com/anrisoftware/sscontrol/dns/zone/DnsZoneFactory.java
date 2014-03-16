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
package com.anrisoftware.sscontrol.dns.zone;

import java.util.Map;

/**
 * Factory to create a new zone.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DnsZoneFactory {

	/**
	 * Creates a new zone.
	 * 
	 * @param args
	 *            the arguments for the zone:
	 *            <ul>
	 *            <li>{@code primary:} the name of the primary DNS server. The
	 *            zone placeholder {code %} is replaced with the name of the
	 *            zone.
	 * 
	 *            <li>{@code email:} the email address for the zone. The zone
	 *            placeholder {code %} is replaced with the name of the zone.
	 * 
	 *            <li>{@code serial:} the serial for the zone.
	 *            </ul>
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @return the {@link DnsZone}.
	 */
	DnsZone create(Map<String, Object> args, String name);
}
