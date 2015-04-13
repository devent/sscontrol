/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine_2_6;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.redmine.DefaultRedmineService;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Redmine 2.6</i> service.
 *
 * @see <a href="http://www.redmine.org/">http://www.redmine.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Redmine_2_6_Service extends DefaultRedmineService {

    private static final String SERVICE_NAME = "redmine_2_6";

    /**
     * @see Redmine_2_6_ServiceFactory#create(Map, Domain)
     */
    @Inject
    Redmine_2_6_Service(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(webServiceFactory, args, domain, SERVICE_NAME);
    }

}
