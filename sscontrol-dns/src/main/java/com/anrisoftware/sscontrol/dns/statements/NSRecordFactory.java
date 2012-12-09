package com.anrisoftware.sscontrol.dns.statements;

/**
 * Factory to create a new NS-record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface NSRecordFactory {

	/**
	 * Creates a new NS-record.
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which the record belongs to.
	 * 
	 * @param name
	 *            the name of the record.
	 * 
	 * @return the {@link NSRecord}.
	 */
	NSRecord create(DnsZone zone, String name);
}
