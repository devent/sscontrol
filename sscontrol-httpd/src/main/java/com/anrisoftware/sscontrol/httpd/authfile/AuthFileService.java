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
package com.anrisoftware.sscontrol.httpd.authfile;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService;
import com.anrisoftware.sscontrol.httpd.auth.AuthServiceLogger;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceArgs;
import com.google.inject.assistedinject.Assisted;

/**
 * HTTP/authentication file service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthFileService extends AbstractAuthService {

    /**
     * HTTP/authentication service name.
     */
    public static final String AUTH_FILE_NAME = "auth-file";

    /**
     * @see AuthFileServiceFactory#create(Domain, Map)
     */
    @Inject
    AuthFileService(@Assisted Domain domain,
            @Assisted Map<String, Object> args, WebServiceArgs aargs,
            AuthServiceLogger log) {
        super(domain, args, aargs, log);
    }

    @Override
    public String getName() {
        return AUTH_FILE_NAME;
    }

}
