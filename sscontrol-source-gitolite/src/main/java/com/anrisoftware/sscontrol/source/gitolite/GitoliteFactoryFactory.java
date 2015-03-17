/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-gitolite.
 *
 * sscontrol-source-gitolite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-gitolite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-gitolite. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.gitolite;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.overridemode.OverrideMode;
import com.anrisoftware.sscontrol.source.service.SourceServiceFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceFactoryFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Gitolite</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(SourceServiceFactoryFactory.class)
public class GitoliteFactoryFactory implements SourceServiceFactoryFactory {

    /**
     * <i>Gitolite</i> service name.
     */
    public static final String SERVICE_NAME = "gitolite";

    /**
     * <i>Gitolite</i> service information.
     */
    @SuppressWarnings("serial")
    public static final SourceServiceInfo INFO = new SourceServiceInfo() {

        @Override
        public String getServiceName() {
            return SERVICE_NAME;
        }

    };

    private static final Module[] MODULES = new Module[] { new GitoliteModule() };

    private Injector injector;

    public GitoliteFactoryFactory() {
    }

    @Override
    public SourceServiceFactory getFactory() throws ServiceException {
        return injector.getInstance(GitoliteServiceFactory.class);
    }

    @Override
    public SourceServiceInfo getInfo() {
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
        customizer.addImports(OverrideMode.class.getName());
        c.addCompilationCustomizers(customizer);
    }
}
