/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-gitit.
 *
 * sscontrol-httpd-gitit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-gitit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-gitit. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.gitit;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Web service factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(WebServiceFactoryFactory.class)
public class HordeFactoryFactory implements WebServiceFactoryFactory {

    private static final String AUTH_METHOD = "com.anrisoftware.sscontrol.httpd.gitit.AuthMethod";

    private static final String LOGIN_REQUIRED = "com.anrisoftware.sscontrol.httpd.gitit.LoginRequired";

    private static final String REPOSITORY_TYPE = "com.anrisoftware.sscontrol.httpd.gitit.RepositoryType";

    /**
     * <i>Gitit</i> service name.
     */
    public static final String SERVICE_NAME = "gitit";

    /**
     * <i>Gitit</i> service information.
     */
    @SuppressWarnings("serial")
    public static final WebServiceInfo INFO = new WebServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new HordeModule() };

    private Injector injector;

    public HordeFactoryFactory() {
    }

    @Override
    public WebServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(HordeServiceFactory.class);
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
        CompilerConfiguration c = (CompilerConfiguration) compiler;
        importClasses(c);
    }

    private void importClasses(CompilerConfiguration c) {
        ImportCustomizer customizer = new ImportCustomizer();
        customizer.addImports(LOGIN_REQUIRED);
        customizer.addImports(AUTH_METHOD);
        customizer.addImports(REPOSITORY_TYPE);
        c.addCompilationCustomizers(customizer);
    }
}
