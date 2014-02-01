package com.anrisoftware.sscontrol.httpd.gitit;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Imports <i>Gitit</i> types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class GititImportsModule extends AbstractModule {

    private static final String REPOSITORY_TYPE = "com.anrisoftware.sscontrol.httpd.gitit.RepositoryType";

    @Override
    protected void configure() {
        Multibinder<ClassImporter> b = newSetBinder(binder(),
                ClassImporter.class);
        b.addBinding().toInstance(new ClassImporter() {

            @Override
            public void importClass(ImportCustomizer customizer) {
                customizer.addStaticImport(REPOSITORY_TYPE, "git");
                customizer.addStaticImport(REPOSITORY_TYPE, "darcs");
                customizer.addStaticImport(REPOSITORY_TYPE, "mercurial");
            }
        });
    }

}
