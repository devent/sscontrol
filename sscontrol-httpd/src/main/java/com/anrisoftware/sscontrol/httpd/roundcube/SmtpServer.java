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
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Roundcube SMTP server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SmtpServer {

    private static final String HOST = "host";

    private String host;

    private String user;

    private String password;

    /**
     * @see SmtpServerFactory#createDefault()
     */
    @AssistedInject
    SmtpServer() {
    }

    /**
     * @see SmtpServerFactory#create(Object, Map)
     */
    @AssistedInject
    SmtpServer(SmtpServerArgs aargs, @Assisted Object service,
            @Assisted Map<String, Object> args) {
        setHost(aargs.host(service, args));
        if (aargs.haveUser(args)) {
            setUser(aargs.user(service, args));
        }
        if (aargs.havePassword(args)) {
            setPassword(aargs.password(service, args));
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(HOST, host).toString();
    }
}
