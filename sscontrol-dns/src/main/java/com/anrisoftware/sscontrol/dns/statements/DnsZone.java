package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.ZonePlaceholder.ZONE_PLACEHOLDER;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.replace;
import groovy.lang.GroovyObjectSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.DateContextProperties;
import com.anrisoftware.sscontrol.dns.service.DnsPropertiesProvider;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Specifies authoritative information about a DNS zone, including the primary
 * name server, the email of the domain administrator, the domain serial number,
 * and several timers relating to refreshing the zone.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DnsZone extends GroovyObjectSupport implements Serializable {

	private static final String EMAIL = "email";

	private static final String PRIMARY_NAME_SERVER = "primary name server";

	private static final String NAME = "name";

	private static final String SERIAL = "serial";

	static final long MIN_TIME_SECONDS = 0l;

	static final long MAX_TIME_SECONDS = Long.MAX_VALUE;

	private static final String TTL_PROPERTY = "default_ttl";

	private static final String REFRESH_PROPERTY = "default_refresh";

	private static final String RETRY_PROPERTY = "default_retry";

	private static final String EXPIRE_PROPERTY = "default_expire";

	private static final String MINIMUM_TTL_PROPERTY = "default_minimum_ttl";

	private final ARecordFactory aRecordFactory;

	private final NSRecordFactory nsRecordFactory;

	private final MXRecordFactory mxRecordFactory;

	private final CNAMERecordFactory cnameRecordFactory;

	private final DnsZoneLogger log;

	private final String name;

	private final String primaryNameServer;

	private final String email;

	private final long serial;

	private Duration ttl;

	private Duration refresh;

	private Duration retry;

	private Duration expire;

	private Duration minimumTtl;

	private final List<ARecord> aaRecords;

	private final List<MXRecord> mxRecords;

	private final List<NSRecord> nsRecords;

	private final List<CNAMERecord> cnameRecords;

	/**
	 * @see DnsZoneFactory#create(String, String, String, long)
	 */
	@SuppressWarnings("unchecked")
	@Inject
	DnsZone(DnsZoneLogger log, ARecordFactory aRecordFactory,
			NSRecordFactory nsRecordFactory, MXRecordFactory mxRecordFactory,
			CNAMERecordFactory cnameRecordFactory, DnsPropertiesProvider p,
			@Assisted(NAME) String name, @Assisted("primary") String primary,
			@Assisted(EMAIL) String email, @Assisted long serial) {
		this.log = log;
		this.aRecordFactory = aRecordFactory;
		this.nsRecordFactory = nsRecordFactory;
		this.mxRecordFactory = mxRecordFactory;
		this.cnameRecordFactory = cnameRecordFactory;
		this.aaRecords = SetUniqueList.decorate(new ArrayList<ARecord>());
		this.cnameRecords = new ArrayList<CNAMERecord>();
		this.mxRecords = new ArrayList<MXRecord>();
		this.nsRecords = new ArrayList<NSRecord>();
		this.name = name;
		this.primaryNameServer = replace(primary, ZONE_PLACEHOLDER, name);
		this.email = replace(email, ZONE_PLACEHOLDER, name);
		this.serial = serial;
		ContextProperties pp = p.get();
		setupDefaultTimes(new DateContextProperties(pp.getContext(), pp));
	}

	private void setupDefaultTimes(DateContextProperties p) {
		this.ttl = p.getDurationProperty(TTL_PROPERTY);
		this.refresh = p.getDurationProperty(REFRESH_PROPERTY);
		this.retry = p.getDurationProperty(RETRY_PROPERTY);
		this.expire = p.getDurationProperty(EXPIRE_PROPERTY);
		this.minimumTtl = p.getDurationProperty(MINIMUM_TTL_PROPERTY);
	}

	/**
	 * Sets the IP address for this zone. Creates a new A-record to map this
	 * host name to the specified address.
	 * 
	 * @param address
	 *            the IP address.
	 */
	public void setAddress(String address) {
		a_record(name, address);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the time is not between {@value #MIN_TIME_SECONDS} and
	 *             {@value #MAX_TIME_SECONDS}.
	 */
	public void ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, timeSeconds);
	}

	/**
	 * Sets the refresh time.
	 * 
	 * @param timeSeconds
	 *            the refresh time, in seconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the refresh time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public void refresh(long timeSeconds) {
		log.checkRefreshTime(timeSeconds, this);
		refresh = new Duration(timeSeconds * 1000);
		log.refreshTimeSet(this, timeSeconds);
	}

	/**
	 * Sets the retry time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the retry time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public void retry(long timeSeconds) {
		log.checkRetryTime(timeSeconds, this);
		retry = new Duration(timeSeconds * 1000);
		log.retryTimeSet(this, timeSeconds);
	}

	/**
	 * Sets the expiration time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the expire time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public void expire(long timeSeconds) {
		log.checkExpireTime(timeSeconds, this);
		expire = new Duration(timeSeconds * 1000);
		log.expireTimeSet(this, timeSeconds);
	}

	/**
	 * Sets the minimum time to live time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @throws IllegalArgumentException
	 *             if the minimum TTL is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public void minimum_ttl(long timeSeconds) {
		log.checkMinimumTtl(timeSeconds, this);
		minimumTtl = new Duration(timeSeconds * 1000);
		log.minimumTtlSet(this, timeSeconds);
	}

	/**
	 * Adds a new NS record with the specified name and address. A new A record
	 * will be created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 */
	public void ns_record(String name, String address) {
		ns_record(name, address, (Object) null);
	}

	/**
	 * Adds a new NS record with the specified name and address. A new A record
	 * will be created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link NSRecord}.
	 */
	public NSRecord ns_record(String name, String address, Object statements) {
		NSRecord record = nsRecordFactory.create(this, name);
		record.setAddress(address);
		nsRecords.add(record);
		log.nsRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new NS record with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @return the new {@link NSRecord}.
	 */
	public void ns_record(String name) {
		ns_record(name, (Object) null);
	}

	/**
	 * Adds a new NS record with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link NSRecord}.
	 */
	public NSRecord ns_record(String name, Object statements) {
		NSRecord record = nsRecordFactory.create(this, name);
		nsRecords.add(record);
		log.nsRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new A record with the specified name and address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 */
	public void a_record(String name, String address) {
		a_record(name, address, (Object) null);
	}

	/**
	 * Adds a new A record with the specified name and address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link ARecord}.
	 */
	public ARecord a_record(String name, String address, Object statements) {
		ARecord record = aRecordFactory.create(this, name, address);
		int index = aaRecords.indexOf(record);
		if (index == -1) {
			aaRecords.add(record);
			log.aRecordAdded(this, record);
			return record;
		} else {
			return aaRecords.get(index);
		}
	}

	/**
	 * Adds a new MX record with the specified name and address. A new A record
	 * will be created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 */
	public void mx_record(String name, String address) {
		mx_record(name, address, (Object) null);
	}

	/**
	 * Adds a new MX record with the specified name and address. A new A record
	 * will be created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link MXRecord}.
	 */
	public MXRecord mx_record(String name, String address, Object statements) {
		MXRecord record = mxRecordFactory.create(this, name);
		record.setAddress(address);
		mxRecords.add(record);
		log.mxRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new MX-record with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @return the new {@link MXRecord}.
	 */
	public void mx_record(String name) {
		mx_record(name, (Object) null);
	}

	/**
	 * Adds a new MX-record with the specified name.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link MXRecord}.
	 */
	public MXRecord mx_record(String name, Object statements) {
		MXRecord record = mxRecordFactory.create(this, name);
		mxRecords.add(record);
		log.mxRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new CNAME record with the specified name and alias.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param alias
	 *            the {@link String} alias. The zone placeholder {code %} is
	 *            replaced with the name of the zone.
	 */
	public void cname_record(String name, String alias) {
		cname_record(name, alias, (Object) null);
	}

	/**
	 * Adds a new CNAME record with the specified name and alias.
	 * 
	 * @param name
	 *            the {@link String} name of the record. The zone placeholder
	 *            {code %} is replaced with the name of the zone.
	 * 
	 * @param alias
	 *            the {@link String} alias. The zone placeholder {code %} is
	 *            replaced with the name of the zone.
	 * 
	 * @param statements
	 *            the zone statements.
	 * 
	 * @return the new {@link CNAMERecord}.
	 */
	public CNAMERecord cname_record(String name, String alias, Object statements) {
		CNAMERecord record = cnameRecordFactory.create(this, name, alias);
		cnameRecords.add(record);
		log.cnameRecordAdded(this, record);
		return record;
	}

	/**
	 * Returns the name of this zone.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of the authoritative master name server for the zone.
	 * 
	 * @return the primary name server.
	 */
	public String getPrimaryNameServer() {
		return primaryNameServer;
	}

	/**
	 * Returns the email address of the administrator for the zone.
	 * 
	 * @return the administrator email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the serial number of this zone file.
	 * 
	 * @return the serial number.
	 */
	public long getSerial() {
		return serial;
	}

	/**
	 * Returns the time to live time for this zone.
	 * 
	 * @return the TTL time {@link Duration}.
	 */
	public Duration getTtl() {
		return ttl;
	}

	/**
	 * Returns the refresh time for this zone.
	 * 
	 * @return the refresh time {@link Duration}.
	 */
	public Duration getRefresh() {
		return refresh;
	}

	/**
	 * Returns the retry time for this zone.
	 * 
	 * @return the retry time {@link Duration}.
	 */
	public Duration getRetry() {
		return retry;
	}

	/**
	 * Returns the expire time for this zone.
	 * 
	 * @return the expire time {@link Duration}.
	 */
	public Duration getExpire() {
		return expire;
	}

	/**
	 * Returns the minimum TTL time for this zone.
	 * 
	 * @return the minimum TTL time {@link Duration}.
	 */
	public Duration getMinimumTtl() {
		return minimumTtl;
	}

	/**
	 * Returns the A-records of the zone.
	 * 
	 * @return an unmodifiable {@link List} of the {@link ARecord} records.
	 */
	public List<ARecord> getAaRecords() {
		return unmodifiableList(aaRecords);
	}

	/**
	 * Returns the NS-records of the zone.
	 * 
	 * @return an unmodifiable {@link List} of the {@link NSRecord} records.
	 */
	public List<NSRecord> getNsRecords() {
		return unmodifiableList(nsRecords);
	}

	/**
	 * Returns the MX-records of the zone.
	 * 
	 * @return an unmodifiable {@link List} of the {@link MXRecord} records.
	 */
	public List<MXRecord> getMxRecords() {
		return unmodifiableList(mxRecords);
	}

	/**
	 * Returns the CNAME-records of the zone.
	 * 
	 * @return an unmodifiable {@link List} of the {@link CNAMERecord} records.
	 */
	public List<CNAMERecord> getCnameRecords() {
		return unmodifiableList(cnameRecords);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(SERIAL, serial)
				.append(NAME, name)
				.append(PRIMARY_NAME_SERVER, primaryNameServer)
				.append(EMAIL, email).toString();
	}
}
