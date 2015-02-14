/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine.redmine_2_6_nginx_thin_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.domain.Domain
import com.anrisoftware.sscontrol.httpd.thin.ubuntu_12_04.Ubuntu_12_04_ThinConfig
import com.anrisoftware.sscontrol.httpd.webservice.WebService

/**
 * <i>Ubuntu 12.04 Nginx Thin Redmine 2.6</i> service configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu_12_04_Thin_Redmine_Config extends Ubuntu_12_04_ThinConfig {

    @Override
    String serviceDir(Domain domain, Domain refDomain, WebService service) {
        script.serviceDir domain, refDomain, service
    }

    @Override
    String getServiceName() {
        RedmineConfigFactory.WEB_NAME
    }
}
