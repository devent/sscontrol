package com.anrisoftware.sscontrol.dns.statements;

/**
 * Factory to create a new MX-record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
interface MXRecordFactory {

	/**
	 * Creates a new MX-record.
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which the record belongs to.
	 * 
	 * @param name
	 *            the name of the record.
	 * 
	 * @return the {@link MXRecord}.
	 */
	MXRecord create(DnsZone zone, String name);
}
