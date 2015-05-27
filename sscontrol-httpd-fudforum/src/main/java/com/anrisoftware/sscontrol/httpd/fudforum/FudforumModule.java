/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.fudforum;

import com.anrisoftware.sscontrol.httpd.webservice.WebService;
import com.anrisoftware.sscontrol.scripts.unix.UnixScriptsModule;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the <i>FUDForum</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class FudforumModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new UnixScriptsModule());
        install(new UnixScriptsModule.UnixScriptsDefaultsModule());
        install(new FactoryModuleBuilder().implement(WebService.class,
                FudforumServiceImpl.class).build(FudforumServiceFactory.class));
    }

}
