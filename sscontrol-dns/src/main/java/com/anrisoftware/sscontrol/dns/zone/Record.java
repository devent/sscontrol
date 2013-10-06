package com.anrisoftware.sscontrol.dns.zone;

/**
 * DNS zone record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum Record {

	/**
	 * A-record.
	 * 
	 * @see ARecord
	 */
	a,

	/**
	 * CNAME-record.
	 * 
	 * @see CNAMERecord
	 */
	cname,

	/**
	 * MX-record.
	 * 
	 * @see MXRecord
	 */
	mx,

	/**
	 * NS-record.
	 * 
	 * @see NSRecord
	 */
	ns
}
