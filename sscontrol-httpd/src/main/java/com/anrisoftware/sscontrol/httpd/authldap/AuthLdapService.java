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
package com.anrisoftware.sscontrol.httpd.authldap;

import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.ATTRIBUTE_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.AUTHORITATIVE_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.CREDENTIALS_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.GROUPDN_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.HOST_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.PASSWORD_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.REQUIRE_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.SATISFY_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.TYPE_KEY;
import static com.anrisoftware.sscontrol.httpd.authldap.AuthLdapServiceStatement.URL_KEY;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.groovy.StatementsException;
import com.anrisoftware.sscontrol.core.groovy.StatementsMap;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapFactory;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.anrisoftware.sscontrol.httpd.auth.AbstractAuthService;
import com.anrisoftware.sscontrol.httpd.auth.AuthType;
import com.anrisoftware.sscontrol.httpd.auth.SatisfyType;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.google.inject.assistedinject.Assisted;

/**
 * LDAP authentication service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthLdapService extends AbstractAuthService {

    /**
     * HTTP/authentication service name.
     */
    public static final String AUTH_LDAP_NAME = "auth-ldap";

    private final Map<String, Object> attributes;

    @Inject
    private AuthLdapServiceLogger log;

    private StatementsMap statementsMap;

    /**
     * @see AuthLdapServiceFactory#create(Map, Domain)
     */
    @Inject
    AuthLdapService(@Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(AUTH_LDAP_NAME, args, domain);
        this.attributes = new HashMap<String, Object>();
    }

    @Inject
    public final void setStatementsMap(StatementsMapFactory factory) {
        StatementsMap map = factory.create(this, AUTH_LDAP_NAME);
        map.addAllowed(TYPE_KEY, HOST_KEY, CREDENTIALS_KEY, REQUIRE_KEY);
        map.setAllowValue(true, TYPE_KEY, HOST_KEY, CREDENTIALS_KEY);
        map.addAllowedKeys(TYPE_KEY, SATISFY_KEY, AUTHORITATIVE_KEY);
        map.addAllowedKeys(HOST_KEY, URL_KEY);
        map.addAllowedKeys(CREDENTIALS_KEY, PASSWORD_KEY);
        map.addAllowedKeys(REQUIRE_KEY, GROUPDN_KEY);
        this.statementsMap = map;
    }

    /**
     * Returns the authentication type.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
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
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
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

    /**
     * Returns whether authorization and authentication are passed to lower
     * level modules.
     *
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             type AuthType.basic, satisfy: AuthType.any, authoritative: no
     *         }
     *     }
     * }
     * </pre>
     *
     * @return {@code true} if enabled or {@code null}.
     */
    public Boolean getAuthoritative() {
        Object value = statementsMap.mapValue(TYPE_KEY, AUTHORITATIVE_KEY);
        if (value instanceof YesNoFlag) {
            return ((YesNoFlag) value).asBoolean();
        }
        return (Boolean) value;
    }

    /**
     * Returns the LDAP host.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             host "ldap://127.0.0.1:389", url: "o=deventorg,dc=ubuntutest,dc=com?cn"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} host or {@code null}.
     */
    public String getHost() {
        return statementsMap.value(HOST_KEY);
    }

    /**
     * Returns the LDAP URL.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             host "ldap://127.0.0.1:389", url: "o=deventorg,dc=ubuntutest,dc=com?cn"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} URL or {@code null}.
     */
    public String getHostUrl() {
        return statementsMap.mapValue(HOST_KEY, URL_KEY);
    }

    /**
     * Returns the LDAP credentials.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             credentials "cn=admin,dc=ubuntutest,dc=com", password: "adminpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} credentials or {@code null}.
     */
    public String getCredentials() {
        return statementsMap.value(CREDENTIALS_KEY);
    }

    /**
     * Returns the LDAP credentials password.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             credentials "cn=admin,dc=ubuntutest,dc=com", password: "adminpass"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link String} password or {@code null}.
     */
    public String getCredentialsPassword() {
        return statementsMap.mapValue(CREDENTIALS_KEY, PASSWORD_KEY);
    }

    /**
     * Returns the required attributes.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             require attribute: [group: "uniqueMember"]
     *             require attribute: [dn: no]
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the {@link Map} map of attributes or {@code null}.
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Returns the required group DN.
     * <p>
     *
     * <pre>
     * httpd {
     *     domain "test1.com", address: "192.168.0.51", {
     *         setup "auth-ldap", auth: "Private Directory", alias: "/private", {
     *             require groupdn: "cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com"
     *         }
     *     }
     * }
     * </pre>
     *
     * @return the required group {@link String} DN or {@code null}.
     */
    public String getRequireGroupDn() {
        return statementsMap.mapValue(REQUIRE_KEY, GROUPDN_KEY);
    }

    public void require(Map<String, Object> args) {
        if (args.containsKey(ATTRIBUTE_KEY.toString())) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = convertYesno((Map<String, Object>) args
                    .get(ATTRIBUTE_KEY.toString()));
            attributes.putAll(map);
            log.requiredAttributesAdded(this, map);
            return;
        } else {
            methodMissing("require", args);
        }
    }

    @Override
    public Object methodMissing(String name, Object args) {
        try {
            return super.methodMissing(name, args);
        } catch (StatementsException e) {
            return statementsMap.methodMissing(name, args);
        }
    }

    private Map<String, Object> convertYesno(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>(map);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof YesNoFlag) {
                YesNoFlag flag = (YesNoFlag) entry.getValue();
                result.put(entry.getKey(), flag.asBoolean());
            }
        }
        return result;
    }

}
