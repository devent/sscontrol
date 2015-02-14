/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Map;

import com.anrisoftware.sscontrol.mail.api.MailService;

/**
 * Factory to create the certificate files.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface CertificateFactory {

    /**
     * Sets the arguments for the certificate files.
     * 
     * @param service
     *            the {@link MailService}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Certificate}.
     */
    Certificate create(MailService service, Map<String, Object> args);
}
