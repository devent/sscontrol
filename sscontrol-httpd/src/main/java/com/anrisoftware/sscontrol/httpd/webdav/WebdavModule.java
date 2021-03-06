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
package com.anrisoftware.sscontrol.httpd.webdav;

import static com.anrisoftware.sscontrol.httpd.webdav.WebdavServiceImpl.SERVICE_NAME;
import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>WebDAV</i> module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WebdavModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(WebService.class,
                WebdavServiceImpl.class).build(WebdavServiceFactory.class));
        bindService();
    }

    private void bindService() {
        MapBinder<String, WebServiceFactory> mapbinder;
        mapbinder = newMapBinder(binder(), String.class,
                WebServiceFactory.class);
        mapbinder.addBinding(SERVICE_NAME).toProvider(
                WebdavServiceProvider.class);
    }
}
