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
package com.anrisoftware.sscontrol.httpd.statements.webserviceargs;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;

/**
 * Parses arguments for {@link WebService}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebServiceArgs {

    private static final String PROXYNAME = "proxyname";

    private static final String REFDOMAIN = "refdomain";

    private static final String SLASH = "/";

    private static final String REF = "ref";

    private static final String ID = "id";

    private static final String ALIAS = "alias";

    @Inject
    private WebServiceLogger log;

    public boolean haveAlias(Map<String, Object> args) {
        return args.containsKey(ALIAS);
    }

    public String alias(WebService service, Map<String, Object> args) {
        Object alias = args.get(ALIAS);
        log.checkAlias(service, alias);
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
        log.checkId(service, id);
        return id.toString();
    }

    public boolean haveRef(Map<String, Object> args) {
        return args.containsKey(REF);
    }

    public String ref(WebService service, Map<String, Object> args) {
        Object ref = args.get(REF);
        log.checkRef(service, ref);
        return ref.toString();
    }

    public boolean haveRefDomain(Map<String, Object> args) {
        return args.containsKey(REFDOMAIN);
    }

    public String refDomain(WebService service, Map<String, Object> args) {
        Object ref = args.get(REFDOMAIN);
        log.checkRefDomain(service, ref);
        return ref.toString();
    }

    public boolean haveProxyName(Map<String, Object> args) {
        return args.containsKey(PROXYNAME);
    }

    public String proxyName(WebService service, Map<String, Object> args) {
        Object name = args.get(PROXYNAME);
        log.checkProxyName(service, name);
        return name.toString();
    }

}
