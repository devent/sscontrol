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
package com.anrisoftware.sscontrol.httpd.authfile;

import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileServiceStatement.DOMAIN_KEY;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileServiceStatement.SATISFY_KEY;
import static com.anrisoftware.sscontrol.httpd.authfile.AuthFileServiceStatement.TYPE_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService;
import com.anrisoftware.sscontrol.httpd.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.auth.RequireUser;
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.google.inject.assistedinject.Assisted;

/**
 * File authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthFileService extends AbstractAuthService {

    /**
     * HTTP/authentication service name.
     */
    public static final String AUTH_FILE_NAME = "auth-file";

    private final List<RequireDomain> requireDomains;

    @Inject
    private AuthFileServiceLogger log;

    @Inject
    private RequireDomainFactory requireDomainFactory;

    private StatementsMap statementsMap;

    /**
     * @see AuthFileServiceFactory#create(Map, Domain)
     */
    @Inject
    AuthFileService(@Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(AUTH_FILE_NAME, args, domain);
        this.requireDomains = new ArrayList<RequireDomain>();
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, AUTH_FILE_NAME);
        map.addAllowed(TYPE_KEY);
        map.setAllowValue(true, TYPE_KEY);
        map.addAllowedKeys(TYPE_KEY, SATISFY_KEY);
        this.statementsMap = map;
    }

    /**
     * Returns the authentication type.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-file", auth: "Private Directory", location: "/private", {
     *             type AuthType.digest
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link AuthType} type or {@code null}.
     */
    public AuthType getType() {
        return statementsMap.value(TYPE_KEY);
    }

    /**
     * Returns the criteria that must be to allow access to a resource.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-file", auth: "Private Directory", location: "/private", {
     *             type AuthType.digest, satisfy: SatisfyType.any
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link SatisfyType} type or {@code null}.
     */
    public SatisfyType getSatisfy() {
        return statementsMap.mapValue(TYPE_KEY, SATISFY_KEY);
    }

    @Override
    public void require(Map<String, Object> args) {
        super.require(args);
        if (args.containsKey(DOMAIN_KEY.toString())) {
            RequireDomain domain = requireDomainFactory.create(this, args);
            requireDomains.add(domain);
            log.requireDomainAdded(this, domain);
            return;
        }
    }

    /**
     * Returns the required users.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-file", auth: "Private Directory", location: "/private", {
     *             require domain: "https://%"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} list of {@link RequireUser} users or
     *         {@code null}.
     */
    public List<RequireDomain> getRequireDomains() {
        return requireDomains;
    }

    @Override
    public Object methodMissing(String name, Object args) {
        try {
            return super.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsMap.methodMissing(name, args);
        }
    }

}
