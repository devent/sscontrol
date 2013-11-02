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
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service

/**
 * Loads DNS service resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DnsResources {

	static ubuntu1004Profile = DnsResources.class.getResource("Ubuntu_10_04Profile.groovy")
	static serialFixed = DnsResources.class.getResource("SerialFixed.groovy")
	static serialGenerated = DnsResources.class.getResource("SerialGenerated.groovy")
	static aRecords = DnsResources.class.getResource("ARecords.groovy")
	static cnameRecords = DnsResources.class.getResource("CNAMERecords.groovy")
	static mxRecordsWithARecords = DnsResources.class.getResource("MXRecordsWithARecords.groovy")
	static mxRecords = DnsResources.class.getResource("MXRecords.groovy")
	static nsRecordsWithARecords = DnsResources.class.getResource("NSRecordsWithARecords.groovy")
	static nsRecords = DnsResources.class.getResource("NSRecords.groovy")
	static zoneARecord = DnsResources.class.getResource("ZoneARecord.groovy")
	static originShortcutScript = DnsResources.class.getResource("OriginShortcut.groovy")
	static bindOneAddress = DnsResources.class.getResource("BindOneAddress.groovy")
	static bindMultipleAddressString = DnsResources.class.getResource("BindMultipleAddressString.groovy")
	static bindMultipleAddressArray = DnsResources.class.getResource("BindMultipleAddressArray.groovy")
	static bindLocal = DnsResources.class.getResource("BindLocal.groovy")
	static bindAll = DnsResources.class.getResource("BindAll.groovy")
	static dnsRecursive = DnsResources.class.getResource("DnsRecursive.groovy")
}
