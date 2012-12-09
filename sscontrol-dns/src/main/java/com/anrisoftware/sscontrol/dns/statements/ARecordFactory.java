package com.anrisoftware.sscontrol.dns.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new A-record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ARecordFactory {

	/**
	 * Creates a new A-record.
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which the record belongs to.
	 * 
	 * @param name
	 *            the name of the record.
	 * 
	 * @param address
	 *            the address of the record.
	 * 
	 * @return the {@link ARecord}.
	 */
	ARecord create(@Assisted DnsZone zone, @Assisted("name") String name,
			@Assisted("address") String address);
}
