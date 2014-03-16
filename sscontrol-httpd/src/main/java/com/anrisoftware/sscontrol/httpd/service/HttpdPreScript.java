/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Set;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;

/**
 * Pre-script service for <i>Httpd</i> service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class HttpdPreScript implements ServicePreScript {

    @Inject
    private Set<ClassImporter> classImporters;

    @Inject
    private WebServicesProvider webServicesProvider;

    @Override
    public void configureCompiler(Object compiler) throws Exception {
        CompilerConfiguration c = (CompilerConfiguration) compiler;
        importClasses(c);
        for (WebServiceFactoryFactory factory : webServicesProvider.get()) {
            factory.configureCompiler(compiler);
        }
    }

    private void importClasses(CompilerConfiguration c) {
        ImportCustomizer customizer = new ImportCustomizer();
        for (ClassImporter classImporter : classImporters) {
            classImporter.importClass(customizer);
        }
        c.addCompilationCustomizers(customizer);
    }

}
