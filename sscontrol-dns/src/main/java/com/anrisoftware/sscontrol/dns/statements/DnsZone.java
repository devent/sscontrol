package com.anrisoftware.sscontrol.dns.statements;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.Duration;

import com.anrisoftware.propertiesutils.ContextProperties;
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
public class DnsZone implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -1960952282894291558L;

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
	 * Sets the parameter of the zone.
	 * 
	 * @param log
	 *            the {@link DnsZoneLogger}.
	 * 
	 * @param p
	 *            the {@link ContextProperties} with the property:
	 *            <dl>
	 *            <dt>{@code default_ttl}</dt>
	 *            <dd>the default TTL time for the zone in seconds.</dd>
	 * 
	 *            <dt>{@code default_refresh}</dt>
	 *            <dd>the default refresh time for the zone in seconds.</dd>
	 * 
	 *            <dt>{@code default_retry}</dt>
	 *            <dd>the default retry time for the zone in seconds.</dd>
	 * 
	 *            <dt>{@code default_expire}</dt>
	 *            <dd>the default expire time for the zone in seconds.</dd>
	 * 
	 *            <dt>{@code default_minimum_ttl}</dt>
	 *            <dd>the default minimum TTL time for the zone in seconds.</dd>
	 *            </dl>
	 * 
	 * @param name
	 *            the name of the zone.
	 * 
	 * @param primaryNameServer
	 *            the host name of the primary name server. The place holder
	 *            {@code %} is replaced by the zone name.
	 * 
	 * @param email
	 *            The email address of the administrator for the zone. The place
	 *            holder {@code %} is replaced by the zone name.
	 * 
	 * @param serial
	 *            The serial number of this zone file.
	 */
	@Inject
	DnsZone(DnsZoneLogger log, ARecordFactory aRecordFactory,
			NSRecordFactory nsRecordFactory, MXRecordFactory mxRecordFactory,
			CNAMERecordFactory cnameRecordFactory,
			@Named("dns-defaults-properties") ContextProperties p,
			@Assisted("name") String name,
			@Assisted("primaryNameServer") String primaryNameServer,
			@Assisted("email") String email, @Assisted long serial) {
		this.log = log;
		this.aRecordFactory = aRecordFactory;
		this.nsRecordFactory = nsRecordFactory;
		this.mxRecordFactory = mxRecordFactory;
		this.cnameRecordFactory = cnameRecordFactory;
		this.aaRecords = new ArrayList<ARecord>();
		this.cnameRecords = new ArrayList<CNAMERecord>();
		this.mxRecords = new ArrayList<MXRecord>();
		this.nsRecords = new ArrayList<NSRecord>();
		this.name = name;
		this.primaryNameServer = primaryNameServer.replaceAll("%", name);
		this.email = email.replaceAll("%", name);
		this.serial = serial;
		setupDefaults(p);
	}

	private void setupDefaults(ContextProperties p) {
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
		ARecord record = a_record(name, address);
		aaRecords.add(record);
		log.aRecordSet(this, record);
	}

	/**
	 * Sets the time to live time.
	 * 
	 * @param timeSeconds
	 *            the time to live time, in seconds.
	 * 
	 * @return this {@link DnsZone}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the time is not between {@value #MIN_TIME_SECONDS} and
	 *             {@value #MAX_TIME_SECONDS}.
	 */
	public DnsZone ttl(long timeSeconds) {
		log.checkTtl(timeSeconds, this);
		ttl = new Duration(timeSeconds * 1000);
		log.ttlSet(this, timeSeconds);
		return this;
	}

	/**
	 * Sets the refresh time.
	 * 
	 * @param timeSeconds
	 *            the refresh time, in seconds.
	 * 
	 * @return this {@link DnsZone}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the refresh time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public DnsZone refresh(long timeSeconds) {
		log.checkRefreshTime(timeSeconds, this);
		refresh = new Duration(timeSeconds * 1000);
		log.refreshTimeSet(this, timeSeconds);
		return this;
	}

	/**
	 * Sets the retry time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @return this {@link DnsZone}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the retry time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public DnsZone retry(long timeSeconds) {
		log.checkRetryTime(timeSeconds, this);
		retry = new Duration(timeSeconds * 1000);
		log.retryTimeSet(this, timeSeconds);
		return this;
	}

	/**
	 * Sets the expiration time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @return this {@link DnsZone}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the expire time is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public DnsZone expire(long timeSeconds) {
		log.checkExpireTime(timeSeconds, this);
		expire = new Duration(timeSeconds * 1000);
		log.expireTimeSet(this, timeSeconds);
		return this;
	}

	/**
	 * Sets the minimum time to live time.
	 * 
	 * @param timeSeconds
	 *            the time, in seconds.
	 * 
	 * @return this {@link DnsZone}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the minimum TTL is not between {@value #MIN_TIME_SECONDS}
	 *             and {@value #MAX_TIME_SECONDS}.
	 */
	public DnsZone minimum_ttl(long timeSeconds) {
		log.checkMinimumTtl(timeSeconds, this);
		minimumTtl = new Duration(timeSeconds * 1000);
		log.minimumTtlSet(this, timeSeconds);
		return this;
	}

	/**
	 * Adds a new NS record with the name and address. A new A record will be
	 * created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @return the new {@link NSRecord}.
	 */
	public NSRecord ns_record(String name, String address) {
		NSRecord record = nsRecordFactory.create(this, name);
		record.setAddress(address);
		nsRecords.add(record);
		log.nsRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new NS record with the name.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @return the new {@link NSRecord}.
	 */
	public NSRecord ns_record(String name) {
		NSRecord record = nsRecordFactory.create(this, name);
		nsRecords.add(record);
		log.nsRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new A record with the name and address.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @return the new {@link ARecord}.
	 */
	public ARecord a_record(String name, String address) {
		ARecord record = aRecordFactory.create(this, name, address);
		aaRecords.add(record);
		log.aRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new MX record with the name and address. A new A record will be
	 * created that maps this name to the specified address.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @param address
	 *            the {@link String} IP address.
	 * 
	 * @return the new {@link MXRecord}.
	 */
	public MXRecord mx_record(String name, String address) {
		MXRecord record = mxRecordFactory.create(this, name);
		record.setAddress(address);
		mxRecords.add(record);
		log.mxRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new MX-record with the name.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @return the new {@link MXRecord}.
	 */
	public MXRecord mx_record(String name) {
		MXRecord record = mxRecordFactory.create(this, name);
		mxRecords.add(record);
		log.mxRecordAdded(this, record);
		return record;
	}

	/**
	 * Adds a new CNAME record with the name and alias.
	 * 
	 * @param name
	 *            the {@link String} name of the record.
	 * 
	 * @param alias
	 *            the {@link String} alias.
	 * 
	 * @return the new {@link CNAMERecord}.
	 */
	public CNAMERecord cname_record(String name, String alias) {
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
		return new ToStringBuilder(this).append("serial", serial)
				.append("name", name)
				.append("primary name server", primaryNameServer)
				.append("email", email).toString();
	}
}
