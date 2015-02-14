/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.hostname.service;

import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.hostname_empty_null;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.hostname_provider_found;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.hostname_provider_found_message;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.hostname_set;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.hostname_set_info;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.provider_name;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.set_profile;
import static com.anrisoftware.sscontrol.hostname.service.HostnameServiceImplLogger._.set_profile_info;
import static org.apache.commons.lang3.Validate.notEmpty;
import groovy.lang.Script;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.google.inject.Provider;

/**
 * Logging messages for {@link HostnameServiceImpl}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HostnameServiceImplLogger extends AbstractLogger {

    enum _ {

        hostname_empty_null("Hostname must not be empty or null for %s."),

        hostname_set_info("Hostname '{}' set."),

        hostname_set("Hostname '{}' set for {}."),

        set_profile_info("Set profile {} for hostname service."),

        set_profile("Set profile {} for {}."),

        hostname_provider_found("Hostname provider not found"),

        hostname_provider_found_message("Hostname provider '{}' not found."),

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
     * Create logger for {@link HostnameServiceImpl}.
     */
    HostnameServiceImplLogger() {
        super(HostnameServiceImpl.class);
    }

    void profileSet(HostnameServiceImpl service, ProfileService profile) {
        if (isDebugEnabled()) {
            debug(set_profile, profile, service);
        } else {
            info(set_profile_info, profile.getProfileName());
        }
    }

    void hostnameSet(HostnameServiceImpl service, String name) {
        if (isDebugEnabled()) {
            debug(hostname_set, name, service);
        } else {
            info(hostname_set_info, name);
        }
    }

    void checkHostname(HostnameServiceImpl service, String name) {
        notEmpty(name, hostname_empty_null.toString(), service);
    }

    void checkScript(HostnameServiceImpl service, Provider<Script> provider,
            String name) throws ServiceException {
        if (provider != null) {
            return;
        }
        throw logException(new ServiceException(hostname_provider_found).add(
                provider_name, name), hostname_provider_found_message, name);
    }
}
