/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security-clamav.
 *
 * sscontrol-security-clamav is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security-clamav is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security-clamav. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.clamav;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.security.service.SecServiceFactory;
import com.anrisoftware.sscontrol.security.service.SecServiceFactoryFactory;
import com.anrisoftware.sscontrol.security.service.SecServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>ClamAV</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(SecServiceFactoryFactory.class)
public class ClamavFactoryFactory implements SecServiceFactoryFactory {

    /**
     * <i>ClamAV</i> service name.
     */
    public static final String SERVICE_NAME = "clamav";

    /**
     * <i>ClamAV</i> service information.
     */
    @SuppressWarnings("serial")
    public static final SecServiceInfo INFO = new SecServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new ClamavModule() };

    private Injector injector;

    public ClamavFactoryFactory() {
    }

    @Override
    public SecServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(ClamavServiceFactory.class);
    }

    @Override
    public SecServiceInfo getInfo() {
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
        c.addCompilationCustomizers(customizer);
    }
}
