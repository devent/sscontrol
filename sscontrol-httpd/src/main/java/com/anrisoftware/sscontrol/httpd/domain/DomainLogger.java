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
package com.anrisoftware.sscontrol.httpd.domain;

import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.address_set;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.address_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.certification_key_resource_null;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.certification_resource_null;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.document_root_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.document_root_set;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.memory_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.port_set;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.port_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.redirect_added_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.redirect_added_info;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.service_added_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.service_added_info;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.service_not_found;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.use_domain;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.use_domain_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.user_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainLogger._.user_set_info;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.memory.Memory;
import com.anrisoftware.sscontrol.httpd.redirect.Redirect;
import com.anrisoftware.sscontrol.httpd.user.DomainUser;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;

/**
 * Logging messages for {@link DomainImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DomainLogger extends AbstractLogger {

    enum _ {

        address_set_debug("Address '{}' set for {}."),

        address_set("Address '{}' set for domain '{}'."),

        port_set_debug("Port '{}' set for {}."),

        port_set("Port '{}' set for domain '{}'."),

        document_root_debug("Document root '{}' set for {}."),

        document_root_set("Document root '{}' set for domain '{}'."),

        use_domain_debug("Use domain '{}' set for {}."),

        use_domain("Use domains '{}' set for domain '{}'."),

        redirect_added_debug("Redirect {} added for {}."),

        redirect_added_info("Redirect added to '{}' for domain '{}'."),

        service_added_debug("Service {} added for {}."),

        service_added_info("Service '{}' added for domain '{}'."),

        certification_resource_null("Certificate resource must be set for %s."),

        certification_key_resource_null(
                "Certificate key resource must be set for %s."),

        service_not_found("Service '%s' not found for %s."),

        user_set_debug("Domain user {} set for {}."),

        user_set_info("Domain user '{}' set for domain '{}'."),

        memory_set_debug("Domain memory limit {} set for {}.");

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
     * Creates a logger for {@link DomainImpl}.
     */
    public DomainLogger() {
        super(DomainImpl.class);
    }

    void addressSet(DomainImpl domain, String address) {
        if (isDebugEnabled()) {
            debug(address_set_debug, address, domain);
        } else {
            info(address_set, address, domain.getName());
        }
    }

    void portSet(DomainImpl domain, int port) {
        if (isDebugEnabled()) {
            debug(port_set_debug, port, domain);
        } else {
            info(port_set, port, domain.getName());
        }
    }

    void documentRootSet(DomainImpl domain, String root) {
        if (isDebugEnabled()) {
            debug(document_root_debug, root, domain);
        } else {
            info(document_root_set, root, domain.getName());
        }
    }

    void useDomainSet(DomainImpl domain, String use) {
        if (isDebugEnabled()) {
            debug(use_domain_debug, use, domain);
        } else {
            info(use_domain, use, domain.getName());
        }
    }

    void redirectAdded(DomainImpl domain, Redirect redirect) {
        if (isDebugEnabled()) {
            debug(redirect_added_debug, redirect, domain);
        } else {
            info(redirect_added_info, redirect.getDestination(),
                    domain.getName());
        }
    }

    void servicesAdded(DomainImpl domain, WebService service) {
        if (isDebugEnabled()) {
            debug(service_added_debug, service, domain);
        } else {
            info(service_added_info, service.getName(), domain.getName());
        }
    }

    void checkCertificationResource(SslDomain domain, Object resource) {
        notNull(resource, certification_resource_null.toString(), domain);
    }

    void checkCertificationKeyResource(SslDomain domain, Object resource) {
        notNull(resource, certification_key_resource_null.toString(), domain);
    }

    void checkService(Domain domain, WebServiceFactory factory, String name) {
        notNull(factory, service_not_found.toString(), name, domain);
    }

    void userSet(DomainImpl domain, DomainUser user) {
        if (isDebugEnabled()) {
            debug(user_set_debug, user, domain);
        } else {
            info(user_set_info, user.getName(), domain.getName());
        }
    }

    void memorySet(Domain domain, Memory memory) {
        debug(memory_set_debug, memory, domain);
    }

}
