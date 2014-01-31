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
package com.anrisoftware.sscontrol.httpd.roundcube;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * Roundcube mail host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Host {

    private static final String DOMAIN = "domain";

    private static final String ALIAS = "alias";

    private static final String HOST = "host";

    private String host;

    private String alias;

    private String domain;

    /**
     * @see HostFactory#create(Object, Map)
     */
    @Inject
    Host(HostArgs argss, @Assisted Object service,
            @Assisted Map<String, Object> args) {
        setHost(argss.host(service, args));
        if (argss.haveAlias(args)) {
            setAlias(argss.alias(args));
        }
        if (argss.haveDomain(args)) {
            setDomain(argss.domain(args));
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this).append(HOST, host);
        builder = alias != null ? builder.append(ALIAS, alias) : builder;
        builder = domain != null ? builder.append(DOMAIN, domain) : builder;
        return builder.toString();
    }
}
