/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.webserviceargs;

import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.alias_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.alias_set_debug;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.alias_set_info;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.id_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.id_set_debug;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.id_set_info;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.prefix_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.prefix_set_debug;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.prefix_set_info;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.proxyname_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.ref_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.ref_set_debug;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.ref_set_info;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.refdomain_null;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.refdomain_set_debug;
import static com.anrisoftware.sscontrol.httpd.webserviceargs.WebServiceLogger._.refdomain_set_info;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Map;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Parses and checks arguments for {@link WebService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebServiceLogger extends AbstractLogger {

    private static final String PREFIX = "prefix";

    private static final String PROXYNAME = "proxyname";

    private static final String REFDOMAIN = "refdomain";

    private static final String SLASH = "/";

    private static final String REF = "ref";

    private static final String ID = "id";

    private static final String ALIAS = "alias";

    enum _ {

        alias_null("Alias cannot be null or blank for {}."),

        alias_set_debug("Alias '{}' set for {}."),

        alias_set_info("Alias '{}' set for service '{}'."),

        id_null("Identifier cannot be null or blank for {}."),

        id_set_debug("Identifier '{}' set for {}."),

        id_set_info("Identifier '{}' set for service '{}'."),

        ref_null("Reference cannot be null or blank for {}."),

        ref_set_debug("Reference '{}' set for {}."),

        ref_set_info("Reference '{}' set for service '{}'."),

        refdomain_null("Domain reference cannot be null or blank for {}."),

        proxyname_null("Proxy name cannot be null or blank for {}."),

        prefix_null("Prefix cannot be null for {}."),

        refdomain_set_debug("Referenced domain '{}' set for {}."),

        refdomain_set_info("Reference domain '{}' set for service '{}'."),

        prefix_set_debug("Prefix '{}' set for {}."),

        prefix_set_info("Prefix '{}' set for service '{}'.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Creates a logger for {@link WebService}.
     */
    public WebServiceLogger() {
        super(WebService.class);
    }

    public boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    public String alias(WebService service, Map<String, Object> args) {
        Object alias = args.get(ALIAS);
        checkAlias(service, alias);
        String str = alias.toString();
        if (str.startsWith(SLASH)) {
            str = str.substring(1);
        }
        return str;
    }

    public boolean haveId(Map<String, Object> args) {
        return args.containsKey(ID);
    }

    public String id(WebService service, Map<String, Object> args) {
        Object id = args.get(ID);
        checkId(service, id);
        return id.toString();
    }

    public boolean haveRef(Map<String, Object> args) {
        return args.containsKey(REF);
    }

    public String ref(WebService service, Map<String, Object> args) {
        Object ref = args.get(REF);
        checkRef(service, ref);
        return ref.toString();
    }

    public boolean haveRefDomain(Map<String, Object> args) {
        return args.containsKey(REFDOMAIN);
    }

    public String refDomain(WebService service, Map<String, Object> args) {
        Object ref = args.get(REFDOMAIN);
        checkRefDomain(service, ref);
        return ref.toString();
    }

    public boolean haveProxyName(Map<String, Object> args) {
        return args.containsKey(PROXYNAME);
    }

    public String proxyName(WebService service, Map<String, Object> args) {
        Object name = args.get(PROXYNAME);
        checkProxyName(service, name);
        return name.toString();
    }

    public boolean havePrefix(Map<String, Object> args) {
        return args.containsKey(PREFIX);
    }

    public String prefix(WebService service, Map<String, Object> args) {
        Object prefix = args.get(PREFIX);
        checkPrefix(service, prefix);
        return prefix.toString();
    }

    void checkAlias(WebService service, Object alias) {
        notNull(alias, alias_null.toString(), service);
        notBlank(alias.toString(), alias_null.toString(), service);
    }

    public void aliasSet(WebService service, String alias) {
        if (isDebugEnabled()) {
            debug(alias_set_debug, alias, service);
        } else {
            info(alias_set_info, alias, service.getName());
        }
    }

    void checkId(WebService service, Object id) {
        notNull(id, id_null.toString(), service);
        notBlank(id.toString(), id_null.toString(), service);
    }

    public void idSet(WebService service, String id) {
        if (isDebugEnabled()) {
            debug(id_set_debug, id, service);
        } else {
            info(id_set_info, id, service.getName());
        }
    }

    void checkRef(WebService service, Object ref) {
        notNull(ref, ref_null.toString(), service);
        notBlank(ref.toString(), ref_null.toString(), service);
    }

    public void refSet(WebService service, String ref) {
        if (isDebugEnabled()) {
            debug(ref_set_debug, ref, service);
        } else {
            info(ref_set_info, ref, service.getName());
        }
    }

    void checkRefDomain(WebService service, Object ref) {
        notNull(ref, refdomain_null.toString(), service);
        notBlank(ref.toString(), refdomain_null.toString(), service);
    }

    public void refDomainSet(WebService service, String domain) {
        if (isDebugEnabled()) {
            debug(refdomain_set_debug, domain, service);
        } else {
            info(refdomain_set_info, domain, service.getName());
        }
    }

    void checkProxyName(WebService service, Object name) {
        notNull(name, proxyname_null.toString(), service);
        notBlank(name.toString(), proxyname_null.toString(), service);
    }

    void checkPrefix(WebService service, Object prefix) {
        notNull(prefix, prefix_null.toString(), service);
    }

    public void prefixSet(WebService service, String prefix) {
        if (isDebugEnabled()) {
            debug(prefix_set_debug, prefix, service);
        } else {
            info(prefix_set_info, prefix, service.getName());
        }
    }

}
