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
