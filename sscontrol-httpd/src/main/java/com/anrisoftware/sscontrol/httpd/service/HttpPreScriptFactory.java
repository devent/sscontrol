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
package com.anrisoftware.sscontrol.httpd.service;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Factory providing pre-script service for <i>Httpd</i> service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServicePreScriptFactory.class)
public class HttpPreScriptFactory implements ServicePreScriptFactory {

    private static final ServiceScriptInfo INFO = new ServiceScriptInfo() {

        @Override
        public String getServiceName() {
            return HttpdFactory.NAME;
        }

        @Override
        public String getProfileName() {
            return null;
        }
    };

    private static final Module[] MODULES = new Module[] { new HttpdPreScriptModule() };

    private Injector injector;

    @Override
    public ServicePreScript getPreScript() throws ServiceException {
        HttpdPreScript service;
        service = injector.getInstance(HttpdPreScript.class);
        return service;
    }

    @Override
    public ServiceScriptInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

}
