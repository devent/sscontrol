/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.list.StringToListFactory;
import com.anrisoftware.sscontrol.hosts.api.HostsService;

/**
 * Parses host arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostArgs {

    private static final String ALIAS = "alias";

    private static final String HOST = "host";

    @Inject
    private HostLogger log;

    @Inject
    private StringToListFactory toListFactory;

    String address(HostsService service, Map<String, Object> args) {
        Object address = args.get("address");
        log.checkAddress(service, address);
        return address.toString();
    }

    String hostname(HostsService service, Map<String, Object> args) {
        Object name = args.get(HOST);
        log.checkHostname(service, name);
        return name.toString();
    }

    boolean haveAliases(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    List<String> aliases(HostsService service, Map<String, Object> args) {
        Object alias = args.get(ALIAS);
        log.checkAlias(service, alias);
        return toListFactory.create(alias).getList();
    }

}
