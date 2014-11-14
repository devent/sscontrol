/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.maradns_1_2

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.dns.maradns.linux.MaradnsScript
import com.anrisoftware.sscontrol.dns.zone.DnsZone

/**
 * MaraDNS 1.2 service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Maradns_1_2_Script extends MaradnsScript {

    Templates maradnsTemplates

    TemplateResource maradnsConfiguration

    TemplateResource zoneConfiguration

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.maradnsTemplates = factory.create("Maradns_1_2")
        this.maradnsConfiguration = maradnsTemplates.getResource("configuration")
        this.zoneConfiguration = maradnsTemplates.getResource("zonedb")
    }

    /**
     * Deploys the MaraDNS configuration.
     */
    void deployMaraDnsConfiguration() {
        deployConfiguration configurationTokens(), mararcCurrentConfiguration, mararcConfigurations, mararcFile
    }

    /**
     * Returns the MaraDNS configurations.
     */
    List getMararcConfigurations() {
        [
            bindAddressConfiguration,
            ipv4BindAddressConfiguration,
            ipv4AliasesConfiguration,
            rootServersConfiguration,
            aclsConfiguration,
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
        def replace = maradnsConfiguration.getText(true, "ip4_bind_address", "service", service, "properties", this)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the IP aliases.
     */
    def getIpv4AliasesConfiguration() {
        def list = []
        def search = maradnsConfiguration.getText(true, "ip4_aliases_search")
        def replace = maradnsConfiguration.getText(true, "ip4_aliases")
        list << new TokenTemplate(search, replace)
        service.aliases.each { k, v ->
            list << getIpv4AliasConfiguration(k, v)
        }
        return list
    }

    /**
     * Adds the IP alias to the configuration.
     */
    def getIpv4AliasConfiguration(String name, List addresses) {
        def search = maradnsConfiguration.getText(true, "ip4_alias_search", "name", name, "addresses", addresses)
        def replace = maradnsConfiguration.getText(true, "ip4_alias", "name", name, "addresses", addresses)
        new TokenTemplate(search, replace)
    }

    /**
     * Activates and sets the root servers.
     */
    def getRootServersConfiguration() {
        if (service.rootServers.empty) {
            return []
        }
        def list = []
        def search = maradnsConfiguration.getText(true, "root_servers_search")
        def replace = maradnsConfiguration.getText(true, "root_servers")
        list << new TokenTemplate(search, replace)
        list << getRootServerConfiguration(service.rootServers)
    }

    /**
     * Sets the root servers.
     */
    def getRootServerConfiguration(List roots) {
        def search = maradnsConfiguration.getText(true, "root_servers_list_search", "roots", roots)
        def replace = maradnsConfiguration.getText(true, "root_servers_list", "roots", roots)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets ACLs.
     */
    def getAclsConfiguration() {
        if (service.acls.empty) {
            return []
        }
        def list = []
        def search = maradnsConfiguration.getText(true, "recursive_acl_search")
        def replace = maradnsConfiguration.getText(true, "recursive_acl", "recursive", service.acls)
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
            def zoneconfig = zoneConfiguration.getText(true, "properties", this, "zone", zone)
            write zoneFile(zone), zoneconfig, charset
        }
    }

    /**
     * Returns the zone file for the specified zone.
     */
    File zoneFile(DnsZone zone) {
        new File(configurationDir, "db.${zone.name}")
    }

    /**
     * Returns the current {@code mararc} configuration.
     */
    String getMararcCurrentConfiguration() {
        currentConfiguration mararcFile
    }
}
