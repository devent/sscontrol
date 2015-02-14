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

import java.text.ParseException;
import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.sscontrol.dns.time.TimeDurationFactory;

/**
 * DNS/zone record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ZoneRecord {

	/**
	 * Returns the record type.
	 * 
	 * @return the {@link Record}.
	 */
	Record getRecord();

	/**
	 * Sets the time to live time.
	 * 
	 * @see TimeDurationFactory#create(Object, Map)
	 * 
	 * @throws ParseException
	 *             if the duration string is not valid ISO 8601.
	 */
	void ttl(Map<String, Object> args) throws ParseException;

	/**
	 * Returns the zone to which this record belongs to.
	 * 
	 * @return the {@link DnsZone}.
	 */
	DnsZone getZone();

	/**
	 * Returns the time to live for the record.
	 * 
	 * @return the time to live {@link Duration}.
	 */
	Duration getTtl();

}
