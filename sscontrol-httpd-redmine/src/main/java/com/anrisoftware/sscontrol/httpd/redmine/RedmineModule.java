/*
 * Copyright ${project.inceptionYear] Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.redmine;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the <i>Redmine</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RedmineModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(WebService.class,
                RedmineServiceImpl.class).build(RedmineServiceFactory.class));
    }

}
