package com.anrisoftware.sscontrol.dns.statements;

import static com.anrisoftware.sscontrol.dns.statements.ZonePlaceholder.ZONE_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.replace;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * CNAME record, it is an alias of one name to another: the DNS lookup will
 * continue by retrying the lookup with the new name.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CNAMERecord extends AbstractRecord {

	private static final String ALIAS = "alias";

	private static final String NAME = "name";

	private final String name;

	private final String alias;

	/**
	 * @see CNAMERecordFactory#create(DnsZone, String, String)
	 */
	@Inject
	CNAMERecord(@Assisted DnsZone zone, @Assisted(NAME) String name,
			@Assisted(ALIAS) String alias) {
		super(zone);
		this.name = replace(name, ZONE_PLACEHOLDER, zone.getName());
		this.alias = replace(alias, ZONE_PLACEHOLDER, zone.getName());
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
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append(NAME, name).append(ALIAS, alias).toString();
	}
}
