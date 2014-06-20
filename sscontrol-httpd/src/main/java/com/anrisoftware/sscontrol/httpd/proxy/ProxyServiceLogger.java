/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.proxy;

import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.address_null;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.address_set_debug;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.address_set_info;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.proxyname_set_debug;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.proxyname_set_info;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.service_null;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.service_set_debug;
import static com.anrisoftware.sscontrol.httpd.proxy.ProxyServiceLogger._.service_set_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;

/**
 * Logging for {@link ProxyServiceImpl}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProxyServiceLogger extends AbstractLogger {

    enum _ {

        address_null("Proxy address cannot be null or empty for %s."),

        address_set_debug("Proxy address '{}' set for {}."),

        address_set_info("Proxy address '{}' set for domain '{}'."),

        service_null("Proxy service cannot be null or empty for %s."),

        service_set_debug("Proxy service '{}' set for {}."),

        service_set_info("Proxy service '{}' set for domain '{}'."),

        proxyname_set_debug("Proxy name '{}' set for {}."),

        proxyname_set_info("Proxy name '{}' set for domain '{}'."),

        target_null("Proxy target cannot be null or empty for %s.");

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
     * Sets the context of the logger to {@link ProxyServiceImpl}.
     */
    public ProxyServiceLogger() {
        super(ProxyServiceImpl.class);
    }

    void checkAddress(Domain domain, Object address) {
        notNull(address, address_null.toString(), domain);
        notBlank(address.toString(), address_null.toString(), domain);
    }

    void addressSet(Domain domain, String address) {
        if (isDebugEnabled()) {
            debug(address_set_debug, address, domain);
        } else {
            info(address_set_info, address, domain.getName());
        }
    }

    void checkService(Domain domain, Object service) {
        notNull(service, service_null.toString(), domain);
        notBlank(service.toString(), service_null.toString(), domain);
    }

    void serviceSet(Domain domain, String service) {
        if (isDebugEnabled()) {
            debug(service_set_debug, service, domain);
        } else {
            info(service_set_info, service, domain.getName());
        }
    }

    void proxyNameSet(Domain domain, String name) {
        if (isDebugEnabled()) {
            debug(proxyname_set_debug, name, domain);
        } else {
            info(proxyname_set_info, name, domain.getName());
        }
    }

    void checkTarget(Domain domain, Object service) {
        notNull(service, _.target_null.toString(), domain);
        notBlank(service.toString(), _.target_null.toString(), domain);
    }
}
