/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service;

import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.binding_set_debug;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.binding_set_info;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.domainAdded;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.domainAddedDebug;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.profileSet;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.profileSetDebug;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.sslDomainAdded;
import static com.anrisoftware.sscontrol.httpd.service.HttpdServiceImplLogger._.sslDomainAddedDebug;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * Logging messages for {@link HttpdServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdServiceImplLogger extends AbstractLogger {

	enum _ {

		domainAddedDebug("Domain {} added for {}."),

		domainAdded("Domain '{}' added for service '{}'."),

		profileSetDebug("Profile {} set for {}."),

		profileSet("Profile '{}' set for DNS service."),

		sslDomainAddedDebug("SSL domain {} added for {}."),

        sslDomainAdded("SSL domain '{}' added for service '{}'."),

        binding_set_debug("Binding address {} set {}."),

        binding_set_info("Binding address {} set for httpd service.");

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
	 * Create logger for {@link HttpdServiceImpl}.
	 */
	HttpdServiceImplLogger() {
		super(HttpdServiceImpl.class);
	}

	void profileSet(Service service, ProfileService profile) {
		if (isDebugEnabled()) {
			debug(profileSetDebug, profile, service);
		} else {
			info(profileSet, profile.getProfileName());
		}
	}

	void domainAdded(Service service, Domain domain) {
		if (isDebugEnabled()) {
			debug(domainAddedDebug, domain, service);
		} else {
			info(domainAdded, domain.getName(), service.getName());
		}
	}

	void sslDomainAdded(Service service, Domain domain) {
		if (isDebugEnabled()) {
			debug(sslDomainAddedDebug, domain, service);
		} else {
			info(sslDomainAdded, domain.getName(), service.getName());
		}
	}

    void bindingSet(HttpdServiceImpl service, Binding binding) {
        if (isDebugEnabled()) {
            debug(binding_set_debug, binding, service);
        } else {
            info(binding_set_info, binding.getAddresses());
        }
    }

}
