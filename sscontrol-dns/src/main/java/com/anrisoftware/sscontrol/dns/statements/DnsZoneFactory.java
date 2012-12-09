package com.anrisoftware.sscontrol.dns.statements;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new zone.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DnsZoneFactory {

	/**
	 * Creates a new zone.
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primaryNameServer
	 *            the name of the primary DNS server.
	 * 
	 * @param email
	 *            the email address for the zone.
	 * 
	 * @param serial
	 *            the serial for the zone.
	 * 
	 * @return the {@link DnsZone}.
	 */
	DnsZone create(@Assisted("name") String name,
			@Assisted("primaryNameServer") String primaryNameServer,
			@Assisted("email") String email, @Assisted long serial);
}
