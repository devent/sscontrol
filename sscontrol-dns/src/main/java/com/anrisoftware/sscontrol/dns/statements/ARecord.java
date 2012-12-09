package com.anrisoftware.sscontrol.dns.statements;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * An A record, maps a host name to an IP address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ARecord extends AbstractRecord {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 2247832837914419440L;

	private final String name;

	private final String address;

	/**
	 * Sets the parameter of the A record.
	 * 
	 * @param log
	 *            the {@link ARecordLogger}.
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
	 *            the host name. The place holder {@code %} is replaced by the
	 *            zone name.
	 * 
	 * @param address
	 *            the IP address.
	 */
	@Inject
	ARecord(ARecordLogger logger,
			@Named("dns-service-properties") ContextProperties p,
			@Assisted DnsZone zone, @Assisted("name") String name,
			@Assisted("address") String address) {
		super(logger, p, zone);
		this.name = name.replaceAll("%", zone.getName());
		this.address = address;
	}

	/**
	 * Returns the host name of the A-record.
	 * 
	 * @return the host name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the IP address of the host name.
	 * 
	 * @return the IP address.
	 */
	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("address", address).append("TTL", getTtlSeconds())
				.toString();
	}
}
