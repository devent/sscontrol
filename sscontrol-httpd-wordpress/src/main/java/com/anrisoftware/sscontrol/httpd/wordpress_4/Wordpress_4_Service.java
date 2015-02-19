/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress_4;

import static com.anrisoftware.sscontrol.httpd.wordpress_4.WordpressFactoryFactory.SERVICE_NAME;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.httpd.domain.Domain;
import com.anrisoftware.sscontrol.httpd.webserviceargs.DefaultWebServiceFactory;
import com.anrisoftware.sscontrol.httpd.wordpress.DefaultWordpressService;
import com.google.inject.assistedinject.Assisted;

/**
 * <i>Wordpress 4.x</i> service.
 *
 * @see <a href='http://wordpress.org/">http://wordpress.org/</a>
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Wordpress_4_Service extends DefaultWordpressService {

    /**
     * @see Wordpress_4_ServiceFactory#create(Map, Domain)
     */
    @Inject
    Wordpress_4_Service(DefaultWebServiceFactory webServiceFactory,
            @Assisted Map<String, Object> args, @Assisted Domain domain) {
        super(SERVICE_NAME, webServiceFactory, args, domain);
    }

}
