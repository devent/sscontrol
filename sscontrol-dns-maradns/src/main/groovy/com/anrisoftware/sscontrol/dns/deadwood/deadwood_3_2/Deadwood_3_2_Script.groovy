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
package com.anrisoftware.sscontrol.dns.deadwood.deadwood_3_2

import javax.inject.Inject

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.dns.deadwood.linux.DeadwoodScript

/**
 * <i>MaraDNS-Deadwood 3.2.x</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Deadwood_3_2_Script extends DeadwoodScript {

    Templates deadwoodTemplates

    TemplateResource deadwoodConfiguration

    @Inject
    void setTemplatesFactory(TemplatesFactory factory) {
        this.deadwoodTemplates = factory.create("Deadwood_3_2")
        this.deadwoodConfiguration = deadwoodTemplates.getResource("configuration")
    }

    /**
     * Deploys the <i>Deadwood</i> configuration.
     */
    void deployDeadwoodConfiguration() {
        deployConfiguration configurationTokens(), currentDeadwoodConfiguration, deadwoodConfigurations, deadwoodrcFile
    }

    /**
     * Returns the <i>Deadwood</i> configurations.
     */
    List getDeadwoodConfigurations() {
        [
            bindAddressConfiguration,
            ipv4BindAddressConfiguration,
            chrootConfiguration,
            upstreamServersConfiguration,
            rootServersConfiguration,
            namedRootServersConfiguration,
            aclsConfiguration,
            maxprocsConfiguration,
            handleOverloadConfiguration,
            maximumCacheElementsConfiguration,
            cacheFileConfiguration,
            resurrectionsConfiguration,
            filterRfc1918Configuration,
        ]
    }

    def getBindAddressConfiguration() {
        def search = deadwoodConfiguration.getText(true, "bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "bind_address", "service", service)
        new TokenTemplate(search, replace)
    }

    def getIpv4BindAddressConfiguration() {
        def search = deadwoodConfiguration.getText(true, "ip4_bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "ip4_bind_address", "service", service, "properties", this)
        new TokenTemplate(search, replace)
    }

    def getChrootConfiguration() {
        def search = deadwoodConfiguration.getText(true, "chroot_search")
        def replace = deadwoodConfiguration.getText(true, "chroot", "directory", configurationDir)
        new TokenTemplate(search, replace)
    }

    /**
     * Activates and sets the upstream servers.
     */
    def getUpstreamServersConfiguration() {
        if (service.upstreamServers.empty) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "upstream_servers_search")
        def replace = deadwoodConfiguration.getText(true, "upstream_servers")
        list << new TokenTemplate(search, replace)
        list << getUpstreamServerConfiguration(service.upstreamServers)
    }

    /**
     * Sets the upstream servers.
     */
    def getUpstreamServerConfiguration(List servers) {
        def search = deadwoodConfiguration.getText(true, "upstream_servers_list_search", "servers", servers)
        def replace = deadwoodConfiguration.getText(true, "upstream_servers_list", "servers", servers)
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
        def search = deadwoodConfiguration.getText(true, "root_servers_search")
        def replace = deadwoodConfiguration.getText(true, "root_servers")
        list << new TokenTemplate(search, replace)
        list << getRootServerConfiguration(service.rootServers)
    }

    /**
     * Sets the root servers.
     */
    def getRootServerConfiguration(List roots) {
        roots.inject([]) { l, root ->
            def servers = lookupServersGroup(root)
            def search = deadwoodConfiguration.getText(true, "root_servers_list_search", "roots", servers)
            def replace = deadwoodConfiguration.getText(true, "root_servers_list", "roots", servers)
            l << new TokenTemplate(search, replace)
        }
    }

    /**
     * Activates and sets the named root servers.
     */
    def getNamedRootServersConfiguration() {
        if (service.rootServers.empty) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "root_servers_search")
        def replace = deadwoodConfiguration.getText(true, "root_servers")
        list << new TokenTemplate(search, replace)
        list << getNamedRootServerConfiguration(service.servers)
    }

    /**
     * Sets the named root servers.
     */
    def getNamedRootServerConfiguration(Map servers) {
        def list = []
        servers.each { name, address ->
            def search = deadwoodConfiguration.getText(true, "named_root_servers_list_search", "name", name, "address", address)
            def replace = deadwoodConfiguration.getText(true, "named_root_servers_list", "name", name, "address", address)
            list << new TokenTemplate(search, replace)
        }
        return list
    }

    /**
     * Sets ACLs.
     */
    def getAclsConfiguration() {
        if (service.acls.empty) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "recursive_acl_search")
        def replace = deadwoodConfiguration.getText(true, "recursive_acl", "acls", service.acls)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets maximum requests.
     */
    def getMaxprocsConfiguration() {
        def search = deadwoodConfiguration.getText(true, "maxprocs_search")
        def replace = deadwoodConfiguration.getText(true, "maxprocs", "requests", maxRequests)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets handle overload.
     */
    def getHandleOverloadConfiguration() {
        def search = deadwoodConfiguration.getText(true, "handle_overload_search")
        def replace = deadwoodConfiguration.getText(true, "handle_overload", "enabled", handleOverload)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets maximum cache elements.
     */
    def getMaximumCacheElementsConfiguration() {
        def search = deadwoodConfiguration.getText(true, "maximum_cache_elements_search")
        def replace = deadwoodConfiguration.getText(true, "maximum_cache_elements", "max", maxCache)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the cache file.
     */
    def getCacheFileConfiguration() {
        def search = deadwoodConfiguration.getText(true, "cache_file_search")
        def replace = deadwoodConfiguration.getText(true, "cache_file", "file", cacheFile)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the resurrections.
     */
    def getResurrectionsConfiguration() {
        def search = deadwoodConfiguration.getText(true, "resurrections_search")
        def replace = deadwoodConfiguration.getText(true, "resurrections", "enabled", resurrection)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the filter rfc1918.
     */
    def getFilterRfc1918Configuration() {
        def search = deadwoodConfiguration.getText(true, "filter_rfc1918_search")
        def replace = deadwoodConfiguration.getText(true, "filter_rfc1918", "enabled", filterRfc1918)
        new TokenTemplate(search, replace)
    }
}
