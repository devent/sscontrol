/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.globalpom.textmatch.tokentemplate.TokenTemplate
import com.anrisoftware.resources.templates.api.TemplateResource
import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.resources.templates.api.TemplatesFactory
import com.anrisoftware.sscontrol.dns.deadwood.linux.DeadwoodScript
import com.anrisoftware.sscontrol.dns.service.DnsService
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileModFactory
import com.anrisoftware.sscontrol.scripts.changefile.ChangeFileOwnerFactory

/**
 * <i>MaraDNS-Deadwood 3.2.x</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Deadwood_3_2_Script extends DeadwoodScript {

    @Inject
    ChangeFileOwnerFactory changeFileOwnerFactory

    @Inject
    ChangeFileModFactory changeFileModFactory

    Templates deadwoodTemplates

    TemplateResource deadwoodConfiguration

    @Inject
    final void setTemplatesFactory(TemplatesFactory factory) {
        this.deadwoodTemplates = factory.create("Deadwood_3_2")
        this.deadwoodConfiguration = deadwoodTemplates.getResource("configuration")
    }

    /**
     * Setups the default binding addresses and port.
     *
     * @param service
     *            the {@link DnsService} DNS service.
     */
    void setupDefaultBinding(DnsService service) {
        if (service.bindingAddresses == null) {
            service.bind defaultBindingAddress, port: defaultBindingPort
        }
        service.bindingAddresses.each { address, ports ->
            if (ports == null || ports.size() == 0) {
                service.bind address, port: defaultBindingPort
            }
        }
    }

    /**
     * Deploys the <i>Deadwood</i> configuration.
     *
     * @param service
     *            the {@link DnsService} DNS service.
     */
    void deployDeadwoodConfiguration(DnsService service) {
        configurationDir.mkdirs()
        deployConfiguration configurationTokens(), currentDeadwoodConfiguration, deadwoodConfigurations(service), deadwoodrcFile
    }

    /**
     * Returns the <i>Deadwood</i> configurations.
     *
     * @param service
     *            the {@link DnsService} DNS service.
     */
    List deadwoodConfigurations(DnsService service) {
        [
            bindAddressConfiguration(service),
            ipv4BindAddressConfiguration(service),
            chrootConfiguration(service),
            upstreamServersConfiguration(service),
            rootServersConfiguration(service),
            namedRootServersConfiguration(service),
            aclsConfiguration(service),
            maxprocsConfiguration(service),
            handleOverloadConfiguration(service),
            maximumCacheElementsConfiguration(service),
            cacheFileConfiguration(service),
            resurrectionsConfiguration(service),
            filterRfc1918Configuration(service),
            rejectMxConfiguration(service),
        ]
    }

    /**
     * Deploys the <i>Deadwood</i> user configuration.
     */
    void deployDeadwoodUserConfiguration(int uid, int gid) {
        deployConfiguration configurationTokens(), currentDeadwoodConfiguration, getDeadwoodUserConfigurations(uid, gid), deadwoodrcFile
    }

    /**
     * Returns the <i>Deadwood</i> user configurations.
     */
    List getDeadwoodUserConfigurations(int uid, int gid) {
        [
            userConfiguration(uid),
            groupConfiguration(gid),
        ]
    }

    /**
     * Creates the <i>Deadwood</i> cache file.
     */
    def createDeadwoodCacheFile() {
        if (StringUtils.isBlank(cacheFile)) {
            return
        }
        File cacheFile = new File(configurationDir, cacheFile)
        cacheFile.isFile() == false ? cacheFile.createNewFile() : false
        changeFileOwnerFactory.create(
                log: log,
                runCommands: runCommands,
                command: chownCommand,
                files: cacheFile,
                owner: deadwoodUser,
                ownerGroup: deadwoodGroup,
                this, threads)()
        changeFileModFactory.create(
                log: log,
                runCommands: runCommands,
                command: chmodCommand,
                files: cacheFile,
                mod: "o-rw",
                this, threads)()
    }

    def bindAddressConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "bind_address", "service", service)
        new TokenTemplate(search, replace)
    }

    def ipv4BindAddressConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "ip4_bind_address_search")
        def replace = deadwoodConfiguration.getText(true, "ip4_bind_address", "addresses", service.bindingAddresses)
        new TokenTemplate(search, replace)
    }

    def chrootConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "chroot_search")
        def replace = deadwoodConfiguration.getText(true, "chroot", "directory", configurationDir)
        new TokenTemplate(search, replace)
    }

    /**
     * Activates and sets the upstream servers.
     */
    def upstreamServersConfiguration(DnsService service) {
        if (service.upstreamServers == null) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "upstream_servers_search")
        def replace = deadwoodConfiguration.getText(true, "upstream_servers")
        list << new TokenTemplate(search, replace)
        list << upstreamServerConfiguration(service.upstreamServers)
    }

    /**
     * Sets the upstream servers.
     */
    def upstreamServerConfiguration(List servers) {
        def search = deadwoodConfiguration.getText(true, "upstream_servers_list_search", "servers", servers)
        def replace = deadwoodConfiguration.getText(true, "upstream_servers_list", "servers", servers)
        new TokenTemplate(search, replace)
    }

    /**
     * Activates and sets the root servers.
     */
    def rootServersConfiguration(DnsService service) {
        if (service.rootServers == null) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "root_servers_search")
        def replace = deadwoodConfiguration.getText(true, "root_servers")
        list << new TokenTemplate(search, replace)
        list << rootServerConfiguration(service.rootServers)
    }

    /**
     * Sets the root servers.
     */
    def rootServerConfiguration(List roots) {
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
    def namedRootServersConfiguration(DnsService service) {
        if (service.servers == null) {
            return []
        }
        def list = []
        def search = deadwoodConfiguration.getText(true, "root_servers_search")
        def replace = deadwoodConfiguration.getText(true, "root_servers")
        list << new TokenTemplate(search, replace)
        list << namedRootServerConfiguration(service.servers)
    }

    /**
     * Sets the named root servers.
     */
    def namedRootServerConfiguration(Map servers) {
        def list = []
        servers.each { name, address ->
            def search = deadwoodConfiguration.getText(true, "named_root_servers_list_search", "name", name)
            def replace = deadwoodConfiguration.getText(true, "named_root_servers_list", "name", name, "address", address)
            list << new TokenTemplate(search, replace)
        }
        return list
    }

    /**
     * Sets ACLs.
     */
    def aclsConfiguration(DnsService service) {
        if (service.acls == null) {
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
    def maxprocsConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "maxprocs_search")
        def replace = deadwoodConfiguration.getText(true, "maxprocs", "requests", maxRequests)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets handle overload.
     */
    def handleOverloadConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "handle_overload_search")
        def replace = deadwoodConfiguration.getText(true, "handle_overload", "enabled", handleOverload)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets maximum cache elements.
     */
    def maximumCacheElementsConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "maximum_cache_elements_search")
        def replace = deadwoodConfiguration.getText(true, "maximum_cache_elements", "max", maxCache)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the cache file.
     */
    def cacheFileConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "cache_file_search")
        def replace = deadwoodConfiguration.getText(true, "cache_file", "file", cacheFile)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the resurrections.
     */
    def resurrectionsConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "resurrections_search")
        def replace = deadwoodConfiguration.getText(true, "resurrections", "enabled", resurrection)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the filter rfc1918.
     */
    def filterRfc1918Configuration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "filter_rfc1918_search")
        def replace = deadwoodConfiguration.getText(true, "filter_rfc1918", "enabled", filterRfc1918)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the reject MX lookup.
     */
    def rejectMxConfiguration(DnsService service) {
        def search = deadwoodConfiguration.getText(true, "reject_mx_search")
        def replace = deadwoodConfiguration.getText(true, "reject_mx", "reject", rejectMx)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the user uid.
     */
    def userConfiguration(int uid) {
        def search = deadwoodConfiguration.getText(true, "maradns_uid_search")
        def replace = deadwoodConfiguration.getText(true, "maradns_uid", "uid", uid)
        new TokenTemplate(search, replace)
    }

    /**
     * Sets the group gid.
     */
    def groupConfiguration(int gid) {
        def search = deadwoodConfiguration.getText(true, "maradns_gid_search")
        def replace = deadwoodConfiguration.getText(true, "maradns_gid", "gid", gid)
        new TokenTemplate(search, replace)
    }
}
