package com.anrisoftware.sscontrol.dns.statements;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Maps a domain name to a list of message transfer agents for that domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class MXRecord extends AbstractRecord {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 27383643814320053L;

	private final MXRecordLogger log;

	private final DnsZone zone;

	private final String name;

	private ARecord aRecord;

	private long priority;

	/**
	 * Sets the name of the MX record.
	 * 
	 * @param logger
	 *            the {@link MXRecordLogger}.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl_seconds}</dt>
	 *            <dd>the default TTL time for the record in seconds.</dd>
	 *            </dl>
	 * 
	 * @param zone
	 *            the {@link DnsZone} where this record belongs to.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The place holder
	 *            {@code %} is replaced by the zone name.
	 * 
	 */
	@Inject
	MXRecord(MXRecordLogger logger,
			@Named("dns-defaults-properties") ContextProperties p,
			@Assisted DnsZone zone, @Assisted String name) {
		super(logger, p, zone);
		this.log = logger;
		this.priority = 10;
		this.zone = zone;
		this.name = name.replaceAll("%", zone.getName());
	}

	/**
	 * Sets the priority for the MX record.
	 * 
	 * @param newPriority
	 *            the priority.
	 * 
	 * @return this {@link MXRecordLogger}.
	 */
	public MXRecord priority(long newPriority) {
		priority = newPriority;
		log.prioritySet(this, newPriority);
		return this;
	}

	/**
	 * Sets the IP address for this MX record. Creates a new A record to map
	 * this host name to the specified address.
	 * 
	 * @param address
	 *            the IP address.
	 * 
	 * @return this {@link MXRecord}.
	 */
	public void setAddress(String address) {
		this.aRecord = zone.a_record(name, address);
		log.aRecordSet(this, aRecord);
	}

	/**
	 * Returns the name of the MX-record.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the priority for the MX-record.
	 * 
	 * @return the priority.
	 */
	public long getPriority() {
		return priority;
	}

	/**
	 * Returns the A-record of the MX-record.
	 * 
	 * @return the {@link ARecord} or {@code null} if no A-record was set.
	 */
	public ARecord getARecord() {
		return aRecord;
	}

	@Override
	public String toString() {
		ToStringBuilder s = new ToStringBuilder(this).append("name", name)
				.append("priority", priority).append("ttl", getTtl());
		if (aRecord != null) {
			s.append("A-record", aRecord);
		}
		return s.toString();
	}
}
