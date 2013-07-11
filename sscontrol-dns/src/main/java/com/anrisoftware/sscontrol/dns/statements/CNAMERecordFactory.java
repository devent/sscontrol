package com.anrisoftware.sscontrol.dns.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new CNAME-record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CNAMERecordFactory {

	/**
	 * Creates a new CNAME-record.
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which the record belongs to.
	 * 
	 * @param name
	 *            the name of the record. The zone placeholder {code %} is
	 *            replaced with the name of the zone.
	 * 
	 * @param alias
	 *            the alias of the record. The zone placeholder {code %} is
	 *            replaced with the name of the zone.
	 * 
	 * @return the {@link CNAMERecord}.
	 */
	CNAMERecord create(@Assisted DnsZone zone, @Assisted("name") String name,
			@Assisted("alias") String alias);
}
