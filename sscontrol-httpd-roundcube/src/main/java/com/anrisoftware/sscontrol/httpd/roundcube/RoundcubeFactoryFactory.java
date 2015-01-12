/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Roundcube</i> web service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(WebServiceFactoryFactory.class)
public class RoundcubeFactoryFactory implements WebServiceFactoryFactory {

    /**
     * <i>Roundcube</i> service name.
     */
    public static final String SERVICE_NAME = "roundcube";

    /**
     * <i>Roundcube</i> service information.
     */
    @SuppressWarnings("serial")
    public static final WebServiceInfo INFO = new WebServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new RoundcubeModule() };

    private Injector injector;

    public RoundcubeFactoryFactory() {
    }

    @Override
    public WebServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(RoundcubeServiceFactory.class);
    }

    @Override
    public WebServiceInfo getInfo() {
        return INFO;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

    @Override
    public void configureCompiler(Object compiler) throws Exception {
    }
}
