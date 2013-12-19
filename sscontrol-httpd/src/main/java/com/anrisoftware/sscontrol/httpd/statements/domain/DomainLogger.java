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
package com.anrisoftware.sscontrol.httpd.statements.domain;

import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.address_set;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.address_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.certification_key_resource_null;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.certification_resource_null;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.document_root_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.document_root_set;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.port_set;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.port_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.proxy_set_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.proxy_set_info;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirect_http_to_https_added;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirect_http_to_https_added_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirect_to_www_added;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.redirect_to_www_added_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.service_added_debug;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.service_added_info;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.use_domain;
import static com.anrisoftware.sscontrol.httpd.statements.domain.DomainLogger._.use_domain_debug;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.redirect.Redirect;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebServiceFactory;

/**
 * Logging messages for {@link Domain}.
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

        redirect_to_www_added("Redirect to www added for {}."),

        redirect_to_www_added_debug("Redirect {} added for {}."),

        redirect_http_to_https_added_debug("Redirect {} added for {}."),

        redirect_http_to_https_added("Redirect http to https added for {}."),

        service_added_debug("Service {} added for {}."),

        service_added_info("Service '{}' added for domain '{}'."),

        certification_resource_null("Certificate resource must be set for %s."),

        certification_key_resource_null(
                "Certificate key resource must be set for %s."),

        service_not_found("Service '%s' not found for %s."),

        proxy_set_debug("Proxy {} set for {}."),

        proxy_set_info("Proxy '{}' set for domain '{}'.");

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
     * Creates a logger for {@link Domain}.
     */
    public DomainLogger() {
        super(Domain.class);
    }

    void addressSet(Domain domain, String address) {
        if (isDebugEnabled()) {
            debug(address_set_debug, address, domain);
        } else {
            info(address_set, address, domain.getName());
        }
    }

    void portSet(Domain domain, int port) {
        if (isDebugEnabled()) {
            debug(port_set_debug, port, domain);
        } else {
            info(port_set, port, domain.getName());
        }
    }

    void documentRootSet(Domain domain, String root) {
        if (isDebugEnabled()) {
            debug(document_root_debug, root, domain);
        } else {
            info(document_root_set, root, domain.getName());
        }
    }

    void useDomainSet(Domain domain, String use) {
        if (isDebugEnabled()) {
            debug(use_domain_debug, use, domain);
        } else {
            info(use_domain, use, domain.getName());
        }
    }

    void redirectToWwwAdded(Domain domain, Redirect redirect) {
        if (isDebugEnabled()) {
            debug(redirect_to_www_added_debug, redirect, domain);
        } else {
            info(redirect_to_www_added, domain.getName());
        }
    }

    void redirectHttpToHttpsAdded(Domain domain, Redirect redirect) {
        if (isDebugEnabled()) {
            debug(redirect_http_to_https_added_debug, redirect, domain);
        } else {
            info(redirect_http_to_https_added, domain.getName());
        }
    }

    void servicesAdded(Domain domain, WebService service) {
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
        notNull(factory, _.service_not_found.toString(), name, domain);
    }

    void proxySet(Domain domain, Proxy proxy) {
        if (isDebugEnabled()) {
            debug(proxy_set_debug, proxy, domain);
        } else {
            info(proxy_set_info, proxy, domain.getName());
        }
    }

}
