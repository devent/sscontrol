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

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.resources.ToURIFactory;

/**
 * SSL/Domain certificate.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DomainCertificate {

    private static final String CA = "ca";

    private static final String KEY = "key";

    private static final String FILE = "file";

    @Inject
    private DomainCertificateLogger log;

    @Inject
    private ToURIFactory uriFactory;

    private URI certResource;

    private URI keyResource;

    private URI caResource;

    public void certFile(SslDomain domain, Map<String, Object> args) {
        log.checkFile(args.get(FILE));
        URI res = uriFactory.create().convert(args.get(FILE).toString());
        log.certFileSet(domain, res);
        this.certResource = res;
    }

    public URI getCertResource() {
        return certResource;
    }

    public void keyFile(SslDomain domain, Map<String, Object> args) {
        log.checkKey(args.get(KEY));
        URI res = uriFactory.create().convert(args.get(KEY).toString());
        log.certKeySet(domain, res);
        this.keyResource = res;
    }

    public URI getKeyResource() {
        return keyResource;
    }

    public void caFile(SslDomain domain, Map<String, Object> args) {
        if (!args.containsKey(CA)) {
            return;
        }
        log.checkCa(args.get(CA));
        URI res = uriFactory.create().convert(args.get(CA).toString());
        log.certCaSet(domain, res);
        this.caResource = res;
    }

    public URI getCaResource() {
        return caResource;
    }
}
