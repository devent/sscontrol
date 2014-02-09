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
package com.anrisoftware.sscontrol.httpd.auth;

import static com.anrisoftware.sscontrol.httpd.auth.AuthService.SERVICE_NAME;
import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

/**
 * Installs the HTTP/authentication factories.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(WebService.class,
                AuthService.class).build(AuthServiceFactory.class));
        install(new FactoryModuleBuilder().implement(RequireDomain.class,
                RequireDomain.class).build(RequireDomainFactory.class));
        install(new FactoryModuleBuilder().implement(RequireUser.class,
                RequireUser.class).build(RequireUserFactory.class));
        install(new FactoryModuleBuilder().implement(RequireGroup.class,
                RequireGroup.class).build(RequireGroupFactory.class));
        install(new FactoryModuleBuilder().implement(AuthHost.class,
                AuthHost.class).build(AuthHostFactory.class));
        install(new FactoryModuleBuilder().implement(AuthCredentials.class,
                AuthCredentials.class).build(AuthCredentialsFactory.class));
        install(new FactoryModuleBuilder().implement(RequireValid.class,
                RequireValid.class).build(RequireValidFactory.class));
        install(new FactoryModuleBuilder().implement(GroupAttribute.class,
                GroupAttribute.class).build(GroupAttributeFactory.class));
        bindService();
    }

    private void bindService() {
        MapBinder<String, WebServiceFactory> mapbinder;
        mapbinder = newMapBinder(binder(), String.class,
                WebServiceFactory.class);
        mapbinder.addBinding(SERVICE_NAME)
                .toProvider(AuthServiceProvider.class);
    }
}
