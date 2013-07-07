package com.anrisoftware.sscontrol.dns.statements;

import groovy.lang.GroovyObjectSupport;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.DateContextProperties;

/**
 * Saves the zone and the time to live time for the record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
abstract class AbstractRecord extends GroovyObjectSupport implements
		Serializable {

	private static final String TTL = "ttl [s]";

	private static final String ZONE = "zone";

	static final long TTL_MIN_SECONDS = 1;

	static final long TTL_MAX_SECONDS = Long.MAX_VALUE;

	private static final String TTL_PROPERTY = "default_ttl";

	private AbstractRecordLogger log;

	private final DnsZone zone;

	private Duration ttl;

	/**
	 * Sets the DNS zone to which this record belongs to.
	 * 
	 * @param zone
	 *            the {@link DnsZone}.
	 * 
	 */
	protected AbstractRecord(DnsZone zone) {
		this.zone = zone;
	}

	/**
	 * Injects the DNS service properties for the record.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl}</dt>
	 *            <dd>the default TTL time for the record.</dd>
	 *            </dl>
	 */
	@Inject
	@Named("dns-service-properties")
	public void setDnsServiceProperties(ContextProperties p) {
		setupDefaultTimes(new DateContextProperties(p.getContext(), p));
	}

	private void setupDefaultTimes(DateContextProperties p) {
		this.ttl = p.getDurationProperty(TTL_PROPERTY);
	}

	/**
	 * Injects the logger for this record.
	 * 
	 * @param logger
	 *            the {@link AbstractRecordLogger}.
	 */
	@Inject
	public void setAbstractRecordLogger(AbstractRecordLogger logger) {
		this.log = logger;
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	public void ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, ttl);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param time
	 *            the {@link Duration} duration to live time.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	public void ttl(Duration time) {
		ttl = time;
		log.ttlSet(this, time);
	}

	/**
	 * Returns the zone to which this record belongs to.
	 * 
	 * @return the {@link DnsZone}.
	 */
	public DnsZone getZone() {
		return zone;
	}

	/**
	 * Returns the time to live for the record.
	 * 
	 * @return the time to live {@link Duration}.
	 */
	public Duration getTtl() {
		return ttl;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(ZONE, zone.getName())
				.append(TTL, ttl).toString();
	}

}