/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-dns.
 * 
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-dns is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.cnamerecord;

import static com.anrisoftware.sscontrol.dns.cnamerecord.CNAMERecordArgs.ALIAS;
import static com.anrisoftware.sscontrol.dns.cnamerecord.CNAMERecordArgs.NAME;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.dns.zone.AbstractRecord;
import com.anrisoftware.sscontrol.dns.zone.DnsZone;
import com.anrisoftware.sscontrol.dns.zone.Record;
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

	private final String name;

	private final String alias;

	/**
	 * @see MXRecordFactory#create(DnsZone, Map)
	 */
	@Inject
	CNAMERecord(CNAMERecordArgs aargs, @Assisted DnsZone zone,
			@Assisted Map<String, Object> args) {
		super(zone);
		this.name = aargs.name(args, zone);
		this.alias = aargs.alias(args, zone);
	}

	@Override
	public Record getRecord() {
		return Record.cname;
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
