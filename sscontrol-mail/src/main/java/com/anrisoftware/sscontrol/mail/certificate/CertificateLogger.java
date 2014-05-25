/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.certificate;

import static com.anrisoftware.sscontrol.mail.certificate.CertificateLogger._.ca_null;
import static com.anrisoftware.sscontrol.mail.certificate.CertificateLogger._.cert_null;
import static com.anrisoftware.sscontrol.mail.certificate.CertificateLogger._.key_null;
import static com.anrisoftware.sscontrol.mail.certificate.CertificateLogger._.syntax_error;
import static com.anrisoftware.sscontrol.mail.certificate.CertificateLogger._.syntax_error_message;
import static org.apache.commons.lang3.Validate.notNull;

import java.net.URISyntaxException;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.mail.api.MailService;

/**
 * Logging messages for {@link Certificate}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CertificateLogger extends AbstractLogger {

    enum _ {

        cert_null("Certificate file cannot be null for %s."),

        key_null("Certificate key file cannot be null for %s."),

        ca_null("Certificate authority file cannot be null for %s."),

        syntax_error("URL syntax error"),

        syntax_error_message("URL {} syntax error for service '{}'.");

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
     * Creates a logger for {@link Certificate}.
     */
    public CertificateLogger() {
        super(Certificate.class);
    }

    void checkCert(MailService service, Object cert) {
        notNull(cert, cert_null.toString(), service);
    }

    ServiceException syntaxError(MailService service, URISyntaxException e,
            Object cert) {
        return logException(new ServiceException(syntax_error, e),
                syntax_error_message, cert, service.getName());
    }

    void checkKey(MailService service, Object key) {
        notNull(key, key_null.toString(), service);
    }

    void checkCa(MailService service, Object ca) {
        notNull(ca, ca_null.toString(), service);
    }

}
