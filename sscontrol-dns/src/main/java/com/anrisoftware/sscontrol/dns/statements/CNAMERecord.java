package com.anrisoftware.sscontrol.dns.statements;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * CNAME record, it is an alias of one name to another: the DNS lookup will
 * continue by retrying the lookup with the new name.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CNAMERecord extends AbstractRecord {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 5600994339413183333L;

	private final String name;

	private final String alias;

	/**
	 * Sets the parameter of the CNAME record.
	 * 
	 * @param log
	 *            the {@link CNAMERecordLogger}.
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
	 * @param name
	 *            the host name. The place holder {@code %} is replaced by the
	 *            zone name.
	 * 
	 * @param alias
	 *            the alias name. The place holder {@code %} is replaced by the
	 *            zone name.
	 */
	@Inject
	CNAMERecord(CNAMERecordLogger log,
			@Named("dns-defaults-properties") ContextProperties p,
			@Assisted DnsZone zone, @Assisted("name") String name,
			@Assisted("alias") String alias) {
		super(log, p, zone);
		this.name = name.replaceAll("%", zone.getName());
		this.alias = alias.replaceAll("%", zone.getName());
	}

	/**
	 * Returns the name of the record.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the alias name.
	 * 
	 * @return the alias.
	 */
	public String getAlias() {
		return alias;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name)
				.append("alias", alias).append("TTL", getTtl()).toString();
	}
}
