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

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.globalpom.posixlocale.PosixLocale;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.database.DatabaseDriver;
import com.anrisoftware.sscontrol.core.database.DatabaseType;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>FUDForum</i> web service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(WebServiceFactoryFactory.class)
public class FudforumFactoryFactory implements WebServiceFactoryFactory {

    /**
     * <i>FUDForum</i> service name.
     */
    public static final String SERVICE_NAME = "fudforum";

    /**
     * <i>FUDForum</i> service information.
     */
    @SuppressWarnings("serial")
    public static final WebServiceInfo INFO = new WebServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new FudforumModule() };

    private Injector injector;

    public FudforumFactoryFactory() {
    }

    @Override
    public WebServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(FudforumServiceFactory.class);
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
        customizer.addImports(PosixLocale.class.getName());
        customizer.addImports(OverrideMode.class.getName());
        customizer.addImports(DatabaseType.class.getName());
        customizer.addImports(DatabaseDriver.class.getName());
        c.addCompilationCustomizers(customizer);
    }
}
