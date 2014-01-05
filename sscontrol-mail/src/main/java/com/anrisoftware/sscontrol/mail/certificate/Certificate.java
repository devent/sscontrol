/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.mail.api.MailService;
import com.google.inject.assistedinject.Assisted;

/**
 * Certificate files.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Certificate {

    private static final String PEM = "pem";

    private static final String CA = "ca";

    private static final String KEY = "key";

    private static final String CERT = "cert";

    private final URI cert;

    private final URI key;

    private URI ca;

    private URI pem;

    @Inject
    Certificate(CertificateArgs aargs, @Assisted MailService service,
            @Assisted Map<String, Object> args) throws ServiceException {
        this.cert = aargs.cert(service, args);
        this.key = aargs.key(service, args);
        if (aargs.haveCa(args)) {
            this.ca = aargs.ca(service, args);
        }
        if (aargs.havePem(args)) {
            this.pem = aargs.pem(service, args);
        }
    }

    public URI getCert() {
        return cert;
    }

    public URI getKey() {
        return key;
    }

    public URI getCa() {
        return ca;
    }

    public URI getPem() {
        return pem;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(CERT, cert).append(KEY, key);
        if (ca != null) {
            builder.append(CA, ca);
        }
        if (pem != null) {
            builder.append(PEM, pem);
        }
        return builder.toString();
    }
}
