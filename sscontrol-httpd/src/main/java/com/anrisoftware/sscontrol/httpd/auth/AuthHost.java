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
package com.anrisoftware.sscontrol.httpd.auth;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Authentication host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthHost {

    private static final String URL = "url";

    private static final String HOST = "host";

    private final AbstractAuthService service;

    private final String host;

    private String url;

    /**
     * @see RequireDomainFactory#create(AbstractAuthService, Map)
     */
    @Inject
    AuthHost(AuthHostLogger log, @Assisted AbstractAuthService service,
            @Assisted Map<String, Object> args) {
        this.service = service;
        this.host = log.host(service, args);
        if (log.haveUrl(args)) {
            this.url = log.url(service, args);
        }
    }

    public AbstractAuthService getService() {
        return service;
    }

    public String getHost() {
        return host;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(HOST, host);
        builder.append(URL, url);
        return builder.toString();
    }

}
