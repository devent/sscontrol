package com.anrisoftware.sscontrol.httpd.service;

import java.util.Set;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.groovy.ClassImporter;

/**
 * Pre-script service for <i>Httpd</i> service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdPreScript implements ServicePreScript {

    @Inject
    private Set<ClassImporter> classImporters;

    @Override
    public void configureCompiler(Object compiler) {
        CompilerConfiguration c = (CompilerConfiguration) compiler;
        importClasses(c);
    }

    private void importClasses(CompilerConfiguration c) {
        ImportCustomizer customizer = new ImportCustomizer();
        for (ClassImporter classImporter : classImporters) {
            classImporter.importClass(customizer);
        }
        c.addCompilationCustomizers(customizer);
    }

}
