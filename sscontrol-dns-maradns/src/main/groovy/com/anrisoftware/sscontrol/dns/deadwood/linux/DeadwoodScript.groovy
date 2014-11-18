/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.deadwood.linux

import java.util.regex.Pattern

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.resources.templates.api.Templates
import com.anrisoftware.sscontrol.core.api.Service
import com.anrisoftware.sscontrol.core.service.LinuxScript

/**
 * <i>MaraDNS-Deadwood</i> service script.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class DeadwoodScript extends LinuxScript {

    /**
     * Returns the current <i>Deadwood</i> configuration.
     */
    String getCurrentDeadwoodConfiguration() {
        currentConfiguration deadwoodrcFile
    }

    /**
     * Lookups servers groups.
     *
     * <ul>
     * <li>profile property key {@code servers_group_<group>}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List lookupServersGroup(String group) {
        if (ipv4Pattern.matcher(group).matches() || ipv6Pattern.matcher(group).matches()) {
            return [group]
        } else {
            group = StringUtils.replaceChars(group, "-", "_")
            return profileListProperty("servers_group_$group", defaultProperties)
        }
    }

    /**
     * Returns the file of the Deadwood configuration file.
     *
     * <ul>
     * <li>profile property key {@code configuration_file}</li>
     * </ul>
     *
     * @see #getConfigurationDir()
     * @see #getDefaultProperties()
     */
    File getDeadwoodrcFile() {
        profileFileProperty "configuration_file", configurationDir, defaultProperties
    }

    /**
     * Returns the startup script file of the <i>Deadwood</i> service.
     *
     * <ul>
     * <li>profile property key {@code deadwood_script_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    File getDeadwoodScriptFile() {
        profileProperty("deadwood_script_file", defaultProperties) as File
    }

    /**
     * Returns the user name under which <i>Deadwood</i> is run.
     *
     * <ul>
     * <li>profile property key {@code deadwood_user}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDeadwoodUser() {
        profileProperty "deadwood_user", defaultProperties
    }

    /**
     * Returns the user name under which <i>Deadwood</i> is run.
     *
     * <ul>
     * <li>profile property key {@code deadwood_group}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDeadwoodGroup() {
        profileProperty "deadwood_group", defaultProperties
    }

    /**
     * Returns the default binding addresses, for example {@code "127.0.0.1"}.
     *
     * <ul>
     * <li>profile property key {@code default_binding_addresses}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    List getDefaultBindingAddresses() {
        profileListProperty "default_binding_addresses", defaultProperties
    }

    /**
     * Returns the maximum requests, for example {@code 8}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_max_requests}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    int getMaxRequests() {
        profileNumberProperty "deadwood_max_requests", defaultProperties
    }

    /**
     * Returns handle overload enabled, for example {@code true}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_handle_overload}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getHandleOverload() {
        profileBooleanProperty "deadwood_handle_overload", defaultProperties
    }

    /**
     * Returns the maximum cache, for example {@code 60000}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_max_cache}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    long getMaxCache() {
        profileNumberProperty "deadwood_max_cache", defaultProperties
    }

    /**
     * Returns the cache file name.
     *
     * <ul>
     * <li>profile property key {@code deadwood_cache_file}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getCacheFile() {
        profileProperty "deadwood_cache_file", defaultProperties
    }

    /**
     * Returns fetch expired records enabled, for example {@code true}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_resurrections}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getResurrection() {
        profileBooleanProperty "deadwood_resurrections", defaultProperties
    }

    /**
     * Returns filter RFC1928 enabled, for example {@code true}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_filter_rfc1918}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    boolean getFilterRfc1918() {
        profileBooleanProperty "deadwood_filter_rfc1918", defaultProperties
    }

    /**
     * Returns the IPv4 match pattern.
     *
     * <ul>
     * <li>profile property key {@code ipv4_pattern}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Pattern getIpv4Pattern() {
        Pattern.compile profileProperty("ipv4_pattern", defaultProperties)
    }

    /**
     * Returns the IPv6 match pattern.
     *
     * <ul>
     * <li>profile property key {@code ipv6_pattern}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    Pattern getIpv6Pattern() {
        Pattern.compile profileProperty("ipv6_pattern", defaultProperties)
    }

    /**
     * Returns path of the <i>Deadwood</i> command, for
     * example {@code /usr/sbin/deadwood}.
     *
     * <ul>
     * <li>profile property key {@code deadwood_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDeadwoodCommand() {
        profileProperty "deadwood_command", defaultProperties
    }

    /**
     * Returns path of the <i>Duende</i> command, for
     * example {@code /usr/sbin/duende}.
     *
     * <ul>
     * <li>profile property key {@code duende_command}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String getDuendeCommand() {
        profileProperty "duende_command", defaultProperties
    }
}
