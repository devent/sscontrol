/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.statements;

import com.google.inject.assistedinject.Assisted;

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
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primary
	 *            the name of the primary DNS server. The zone placeholder {code
	 *            %} is replaced with the name of the zone.
	 * 
	 * @param email
	 *            the email address for the zone. The zone placeholder {code %}
	 *            is replaced with the name of the zone.
	 * 
	 * @param serial
	 *            the serial for the zone.
	 * 
	 * @return the {@link DnsZone}.
	 */
	DnsZone create(@Assisted("name") String name,
			@Assisted("primary") String primary,
			@Assisted("email") String email, @Assisted long serial);
}
