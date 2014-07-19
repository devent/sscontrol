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
package com.anrisoftware.sscontrol.httpd.domain;

import static java.lang.String.format;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * SSL/domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class SslDomainImpl extends DomainImpl implements SslDomain {

    private static final String HTTPS = "https://";

    @Inject
    private DomainCertificate certificate;

    /**
     * @see SslDomainFactory#create(Map, String)
     */
    @Inject
    SslDomainImpl(@Assisted Map<String, Object> args, @Assisted String name) {
        super(args, 443, name);
    }

    @Override
    public String getFileName() {
        return format("100-robobee-%s-ssl.conf", getName());
    }

    public void certificate(Map<String, Object> args) {
        certificate.certFile(this, args);
        certificate.keyFile(this, args);
        certificate.caFile(this, args);
    }

    @Override
    public URI getCertResource() {
        return certificate.getCertResource();
    }

    @Override
    public URI getKeyResource() {
        return certificate.getKeyResource();
    }

    @Override
    public URI getCaResource() {
        return certificate.getCaResource();
    }

    @Override
    public String getProto() {
        return HTTPS;
    }

}
