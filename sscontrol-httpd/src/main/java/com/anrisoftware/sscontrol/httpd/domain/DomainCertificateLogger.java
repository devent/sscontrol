/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_ca_null;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_ca_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_ca_set_info;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_file_null;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_file_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_file_set_info;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_key_null;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_key_set_debug;
import static com.anrisoftware.sscontrol.httpd.domain.DomainCertificateLogger._.cert_key_set_info;
import static org.apache.commons.lang3.Validate.notNull;

import java.net.URI;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link DomainCertificate}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class DomainCertificateLogger extends AbstractLogger {

    enum _ {

        cert_file_null("Certificate file cannot be null"),

        cert_file_set_debug("Certificate file '{}' set for {}"),

        cert_file_set_info("Certificate file '{}' set for the domain '{}'"),

        cert_key_null("Certificate key file cannot be null"),

        cert_key_set_debug("Certificate key file '{}' set for {}"),

        cert_key_set_info("Certificate key file '{}' set for the domain '{}'"),

        cert_ca_null("Certificate CA file cannot be null"),

        cert_ca_set_debug("Certificate CA file '{}' set for {}"),

        cert_ca_set_info("Certificate CA file '{}' set for the domain '{}'");

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
     * Sets the context of the logger to {@link DomainCertificate}.
     */
    public DomainCertificateLogger() {
        super(DomainCertificate.class);
    }

    void checkFile(Object object) {
        notNull(object, cert_file_null.toString());
    }

    void certFileSet(SslDomain domain, URI res) {
        if (isDebugEnabled()) {
            debug(cert_file_set_debug, res, domain);
        } else {
            info(cert_file_set_info, res, domain.getName());
        }
    }

    void checkKey(Object object) {
        notNull(object, cert_key_null.toString());
    }

    void certKeySet(SslDomain domain, URI res) {
        if (isDebugEnabled()) {
            debug(cert_key_set_debug, res, domain);
        } else {
            info(cert_key_set_info, res, domain.getName());
        }
    }

    void checkCa(Object object) {
        notNull(object, cert_ca_null.toString());
    }

    void certCaSet(SslDomain domain, URI res) {
        if (isDebugEnabled()) {
            debug(cert_ca_set_debug, res, domain);
        } else {
            info(cert_ca_set_info, res, domain.getName());
        }
    }
}
