/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.services;

import java.util.Map;

import javax.inject.Inject;

/**
 * Parses arguments for the service to secure.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ServiceArgs {

    private static final String NOTIFY = "notify";

    private static final String NAME = "name";

    @Inject
    private ServiceArgsLogger log;

    String name(com.anrisoftware.sscontrol.core.api.Service service,
            Map<String, Object> args) {
        Object name = args.get(NAME);
        log.checkName(name, service);
        return name.toString();
    }

    boolean haveNotify(Map<String, Object> args) {
        return args.containsKey(NOTIFY);
    }

    String notify(com.anrisoftware.sscontrol.core.api.Service service,
            String name, Map<String, Object> args) {
        Object notify = args.get(NOTIFY);
        log.checkNotify(notify, service, name);
        return notify.toString();
    }

}
