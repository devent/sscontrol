package com.anrisoftware.sscontrol.dns.maradns.linux

import static org.apache.commons.io.FileUtils.*

import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.service.LinuxScript
import com.anrisoftware.sscontrol.dns.statements.DnsZone
import com.anrisoftware.sscontrol.workers.text.tokentemplate.TokenTemplate

/**
 * Script to configure MaraDNS 1.2.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class MaraDns_1_2Script extends LinuxScript {

	Templates maradnsTemplates

	TemplateResource maradnsConfiguration

	TemplateResource zoneConfiguration

	@Override
	def run() {
		super.run()
		maradnsTemplates = templatesFactory.create("Maradns_ubuntu_10_04")
		maradnsConfiguration = maradnsTemplates.getResource("configuration")
		zoneConfiguration = maradnsTemplates.getResource("zonedb")
		deployMaraDnsConfiguration()
		deployZoneDbConfigurations()
		restartService restartCommand
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
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_directory}</li>
	 * </ul>
	 */
	abstract File getConfigurationDir()

	/**
	 * Returns the file of the {@code mararc} configuration file.
	 *
	 * <ul>
	 * <li>profile property key {@code configuration_file}</li>
	 * </ul>
	 */
	abstract File getMararcFile()

	/**
	 * Returns the current {@code mararc} configuration.
	 */
	String getMararcConfiguration() {
		currentConfiguration mararcFile
	}

	/**
	 * Returns the restart command for the MaraDNS server.
	 *
	 * <ul>
	 * <li>profile property key {@code restart_command}</li>
	 * </ul>
	 */
	abstract String getRestartCommand()
}
