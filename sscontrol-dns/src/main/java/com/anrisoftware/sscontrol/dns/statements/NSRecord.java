package com.anrisoftware.sscontrol.dns.statements;

import static org.apache.commons.lang3.StringUtils.replace;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Delegates a DNS zone to use the given authoritative name servers.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class NSRecord extends AbstractRecord {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -7223762700710514007L;

	private final NSRecordLogger log;

	private final DnsZone zone;

	private final String name;

	private long ttlInSeconds;

	private ARecord aRecord;

	/**
	 * Sets the name of the NS-record.
	 * 
	 * @param logger
	 *            the {@link NSRecordLogger}.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl_seconds}</dt>
	 *            <dd>the default TTL time for the record in seconds.</dd>
	 *            </dl>
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which this record belongs to.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The place holder
	 *            {@code %} is replaced by the zone name.
	 * 
	 * @since 1.1
	 */
	@Inject
	NSRecord(NSRecordLogger logger,
			@Named("dns-service-properties") ContextProperties p,
			@Assisted DnsZone zone, @Assisted String name) {
		super(logger, p, zone);
		this.log = logger;
		this.zone = zone;
		this.name = replace(name, "%", zone.getName());
	}

	/**
	 * Returns the name of the NS-record.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the IP address for this NS-record. We will create a new A-record to
	 * map this host name to the specified address.
	 * 
	 * @param address
	 *            the IP address.
	 */
	public void setAddress(String address) {
		this.aRecord = zone.a_record(name, address);
		log.aRecordSet(this, aRecord);
	}

	@Override
	public String toString() {
		ToStringBuilder s = new ToStringBuilder(this).append("name", name)
				.append("ttl [s]", ttlInSeconds);
		if (aRecord != null) {
			s.append("A-record", aRecord);
		}
		return s.toString();
	}
}
