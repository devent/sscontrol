/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hosts.
 *
 * sscontrol-hosts is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hosts is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hosts. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hosts.host;

import static com.anrisoftware.sscontrol.hosts.host.HostLogger._.address_null;
import static com.anrisoftware.sscontrol.hosts.host.HostLogger._.alias_added_debug;
import static com.anrisoftware.sscontrol.hosts.host.HostLogger._.alias_added_info;
import static com.anrisoftware.sscontrol.hosts.host.HostLogger._.alias_null;
import static com.anrisoftware.sscontrol.hosts.host.HostLogger._.name_null;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.hosts.api.HostsService;

/**
 * Logging messages for {@link Host}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostLogger extends AbstractLogger {

    enum _ {

        address_null("Host address cannot be null or blank for %s."),

        name_null("Host name cannot be null or blank for %s."),

        alias_null("Alias cannot be null for %s."),

        alias_added_debug("Host alias '{}' added for {}."),

        alias_added_info("Host alias '{}' added for host '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Create logger for {@link Host}.
     */
    HostLogger() {
        super(Host.class);
    }

    void aliasAdded(Host host, String alias) {
        if (isDebugEnabled()) {
            debug(alias_added_debug, alias, host);
        } else if (isInfoEnabled()) {
            info(alias_added_info, alias, host.getHostname());
        }
    }

    void checkAddress(HostsService service, Object address) {
        notNull(address, address_null.toString(), service);
        notBlank(address.toString(), address_null.toString(), service);
    }

    void checkHostname(HostsService service, Object name) {
        notNull(name, name_null.toString(), service);
        notBlank(name.toString(), name_null.toString(), service);
    }

    void checkAlias(HostsService service, Object alias) {
        notNull(alias, alias_null.toString(), service);
    }

}
