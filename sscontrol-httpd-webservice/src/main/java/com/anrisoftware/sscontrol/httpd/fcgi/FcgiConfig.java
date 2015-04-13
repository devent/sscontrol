/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.fcgi;

import java.util.List;

import com.anrisoftware.sscontrol.core.service.LinuxScript;
import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.anrisoftware.sscontrol.httpd.webservice.WebService;

/**
 * Configures the <i>php-fcgi</i>.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface FcgiConfig {

    /**
     * Sets the parent script.
     *
     * @param script
     *            the {@link LinuxScript}.
     */
    void setScript(Object parent);

    /**
     * @see ServiceConfig#deployService(Domain, WebService, List)
     */
    void deployService(Domain domain, WebService service, List<String> config);

    /**
     * Enables the <i>php-fcgi</i>.
     */
    void enableFcgi();

    /**
     * Setups the domain for <i>php-fcgi</i>.
     *
     * @param domain
     *            the {@link Domain}.
     */
    void deployConfig(Domain domain);

}
