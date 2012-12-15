package com.anrisoftware.sscontrol.dns.statements;

import java.io.Serializable;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Saves the zone and the time to live time for the record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class AbstractRecord implements Serializable {

	private static final String TTL_PROPERTY = "default_ttl";

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -588712728377074238L;

	private final AbstractRecordLogger log;

	private final DnsZone zone;

	private Duration ttl;

	static final long TTL_MIN_TIME_SECONDS = 0l;

	static final long TTL_MAX_TIME_SECONDS = Long.MAX_VALUE;

	/**
	 * Sets the parameter of the A record.
	 * 
	 * @param log
	 *            the {@link ARecordLogger}.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl}</dt>
	 *            <dd>the default TTL time for the record.</dd>
	 *            </dl>
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which this record belongs to.
	 * 
	 */
	@Inject
	AbstractRecord(AbstractRecordLogger log,
			@Named("dns-service-properties") ContextProperties p,
			@Assisted DnsZone zone) {
		this.log = log;
		this.zone = zone;
		setupDefaults(p);
	}

	private void setupDefaults(ContextProperties p) {
		this.ttl = p.getDurationProperty(TTL_PROPERTY);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @return this {@link AbstractRecord}.
	 */
	public AbstractRecord ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, timeSeconds);
		return this;
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
		return new ToStringBuilder(this).append("zone", zone.getName())
				.append("ttl [s]", ttl).toString();
	}

}