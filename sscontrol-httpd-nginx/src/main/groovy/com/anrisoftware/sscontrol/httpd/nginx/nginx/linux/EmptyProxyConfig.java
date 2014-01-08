/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import java.util.List;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.nginx.api.ProxyConfig;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;

/**
 * Empty proxy.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class EmptyProxyConfig implements ProxyConfig {

    /**
     * Empty Proxy name.
     */
    public static final String SERVICE_NAME = "proxy.empty";

    @Override
    public String getProfile() {
        return null;
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public void setScript(LinuxScript script) {
    }

    @Override
    public LinuxScript getScript() {
        return null;
    }

    @Override
    public void deployService(Domain domain, WebService service,
            List<String> config) {
    }

    @Override
    public void deployDomain(Domain domain, Domain refDomain,
            WebService service, List<String> config) {
    }

    @Override
    public String getProxyService() {
        return null;
    }

}
