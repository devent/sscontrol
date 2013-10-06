package com.anrisoftware.sscontrol.dns.zone;

import org.joda.time.Duration;

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
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	void ttl(long timeSeconds);

	/**
	 * Sets the time to live time.
	 * 
	 * @param time
	 *            the {@link Duration} duration to live time.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	void ttl(Duration time);

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
