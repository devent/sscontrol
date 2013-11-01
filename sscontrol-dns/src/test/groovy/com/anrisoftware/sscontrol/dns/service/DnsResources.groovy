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
	static dnsSerialScript = DnsResources.class.getResource("DnsSerial.groovy")
	static dnsSerialGenerateScript = DnsResources.class.getResource("DnsSerialGenerate.groovy")
	static aRecordsScript = DnsResources.class.getResource("ARecords.groovy")
	static dnsZoneCnameRecordsScript = DnsResources.class.getResource("DnsZoneCNAMERecords.groovy")
	static dnsZoneMxRecordsScript = DnsResources.class.getResource("DnsZoneMXRecords.groovy")
	static dnsZoneNsRecordsScript = DnsResources.class.getResource("DnsZoneNSRecords.groovy")
	static dnsAutomaticARecordZoneScript = DnsResources.class.getResource("DnsAutomaticARecordForSoa.groovy")
	static dnsNoAutomaticARecordsScript = DnsResources.class.getResource("DnsNoAutomaticARecords.groovy")
	static dnsOriginShortcutScript = DnsResources.class.getResource("DnsOriginShortcut.groovy")
	static dnsBindOneAddress = DnsResources.class.getResource("DnsBindOneAddress.groovy")
	static dnsBindMultipleAddressString = DnsResources.class.getResource("DnsBindMultipleAddressString.groovy")
	static dnsBindMultipleAddressArray = DnsResources.class.getResource("DnsBindMultipleAddressArray.groovy")
	static dnsBindLoopback = DnsResources.class.getResource("DnsBindLoopback.groovy")
	static dnsBindAll = DnsResources.class.getResource("DnsBindAll.groovy")
	static dnsRecursive = DnsResources.class.getResource("DnsRecursive.groovy")
}
