/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.linux

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject
import javax.inject.Named

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.dns.statements.DnsZone
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Deploys the MaraDNS service on a general Linux system.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MaraDnsScript extends LinuxScript {

	@Inject
	@Named("maradns-ubuntu_10_04-default-properties")
	ContextProperties defaultProperties

	Templates maradnsTemplates

	TemplateResource maradnsConfiguration

	TemplateResource zoneConfiguration

	def run() {
		super.run()
		maradnsTemplates = templatesFactory.create("Maradns_ubuntu_10_04")
		maradnsConfiguration = maradnsTemplates.getResource("configuration")
		zoneConfiguration = maradnsTemplates.getResource("zonedb")
		distributionSpecificConfiguration()
		deployMaraDnsConfiguration()
		deployZoneDbConfigurations()
		restartService restartCommand
	}

	/**
	 * Run the distribution specific configuration.
	 */
	void distributionSpecificConfiguration() {
	}

	/**
	 * Deploys the MaraDNS configuration.
	 */
	void deployMaraDnsConfiguration() {
		deployConfiguration configurationTokens(), mararcConfiguration, mararcConfigurations, mararcFile
	}

	/**
	 * Returns the MaraDNS configurations.
	 */
	List getMararcConfigurations() {
		[
			bindAddressConfiguration,
			ipv4BindAddressConfiguration,
			csvHashConfiguration,
			zonesConfiguration
		]
	}

	def getCsvHashConfiguration() {
		def search = maradnsConfiguration.getText(true, "csv_hash_search")
		def replace = maradnsConfiguration.getText(true, "csv_hash")
		new TokenTemplate(search, replace)
	}

	def getBindAddressConfiguration() {
		def search = maradnsConfiguration.getText(true, "bind_address_search")
		def replace = maradnsConfiguration.getText(true, "bind_address", "service", service)
		new TokenTemplate(search, replace)
	}

	def getIpv4BindAddressConfiguration() {
		def search = maradnsConfiguration.getText(true, "ip4_bind_address_search")
		def replace = maradnsConfiguration.getText(true, "ip4_bind_address", "service", service)
		new TokenTemplate(search, replace)
	}

	def getZonesConfiguration() {
		service.zones.inject([]) { list, DnsZone zone ->
			list << zoneHashConfiguration(zone)
		}
	}

	def zoneHashConfiguration(DnsZone zone) {
		def search = maradnsConfiguration.getText(true, "zone_search", "zone", zone)
		def replace = maradnsConfiguration.getText(true, "zone", "zone", zone)
		new TokenTemplate(search, replace)
	}

	/**
	 * Deploys the zone database files.
	 */
	void deployZoneDbConfigurations() {
		service.zones.each { DnsZone zone ->
			def zoneconfig = zoneConfiguration.getText(true, "zone", zone)
			write zoneFile(zone), zoneconfig, system.charset
		}
	}

	/**
	 * Returns the zone file for the specified zone.
	 */
	File zoneFile(DnsZone zone) {
		new File(configurationDir, "db.${zone.name}")
	}

	/**
	 * Returns path of the MaraDNS configuration directory.
	 */
	File getConfigurationDir() {
		profileProperty("configuration_dir", defaultProperties) as File
	}

	/**
	 * Returns the file of the {@code mararc} configuration file.
	 */
	File getMararcFile() {
		def file = profileProperty "configuration_file", defaultProperties
		new File(configurationDir, file)
	}

	/**
	 * Returns the current {@code mararc} configuration.
	 */
	String getMararcConfiguration() {
		currentConfiguration mararcFile
	}

	/**
	 * Returns the restart command for the MaraDNS server.
	 */
	String getRestartCommand() {
		profileProperty "restart_command", defaultProperties
	}
}
