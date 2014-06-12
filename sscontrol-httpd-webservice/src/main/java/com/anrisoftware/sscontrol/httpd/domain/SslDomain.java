/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.domain;

import java.net.URL;

/**
 * SSL/TLS domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SslDomain extends Domain {

    /**
     * Returns the certificate resource.
     * 
     * @return the the certificate {@link URL} resource.
     */
    URL getCertificationResource();

    /**
     * Returns the certificate file name.
     * 
     * @return the the certificate file name.
     */
    String getCertificationFile();

    /**
     * Returns the certificate key resource.
     * 
     * @return the the certificate key {@link URL} resource.
     */
    URL getCertificationKeyResource();

    /**
     * Returns the certificate key file path.
     * 
     * @return the the certificate key path.
     */
    String getCertificationKeyFile();

}
