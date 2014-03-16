/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceImplLogger._.dhclient_provider_found;
import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceImplLogger._.dhclient_provider_found_message;
import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceImplLogger._.prepend_added_debug;
import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceImplLogger._.prepend_added_info;
import static com.anrisoftware.sscontrol.dhclient.service.DhclientServiceImplLogger._.provider_name;
import groovy.lang.Script;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.dhclient.statements.OptionDeclaration;
import com.google.inject.Provider;

/**
 * Logging messages for {@link DhclientServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DhclientServiceImplLogger extends AbstractLogger {

    enum _ {

        prepend_added_info("Prepend '{}' added for dhclient."),

        prepend_added_debug("Prepend '{}' added for {}."),

        dhclient_provider_found("Dhclient provider not found"),

        dhclient_provider_found_message("Dhclient provider '{}' not found."),

        provider_name("name");

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
     * Create logger for {@link DhclientServiceImpl}.
     */
    DhclientServiceImplLogger() {
        super(DhclientServiceImpl.class);
    }

    public void prependAdded(DhclientService service,
            OptionDeclaration declaration) {
        if (isDebugEnabled()) {
            debug(prepend_added_debug, declaration, service);
        } else {
            info(prepend_added_info, declaration);
        }
    }

    void checkScript(DhclientService service, Provider<Script> provider,
            String name) throws ServiceException {
        if (provider != null) {
            return;
        }
        throw logException(new ServiceException(dhclient_provider_found).add(
                provider_name, name), dhclient_provider_found_message, name);
    }
}
