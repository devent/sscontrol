package com.anrisoftware.sscontrol.dns.recursive;

import java.util.Map;

import com.anrisoftware.sscontrol.dns.arecord.ARecord;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;
import com.anrisoftware.sscontrol.dns.zone.ZoneRecord;

/**
 * Factory to create a new DNS/zone record.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RecordFactory {

	/**
	 * Creates a new DNS/zone record.
	 * 
	 * @param zone
	 *            the {@link DnsZone} to which the record belongs to.
	 * 
	 * @param args
	 *            the {@link Map} arguments for the record.
	 * 
	 * @return the {@link ARecord}.
	 */
	ZoneRecord create(DnsZone zone, Map<String, Object> args);
}
