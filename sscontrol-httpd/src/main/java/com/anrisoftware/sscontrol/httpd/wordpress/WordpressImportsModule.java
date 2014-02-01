package com.anrisoftware.sscontrol.httpd.wordpress;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Imports <i>Wordpress</i> types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class WordpressImportsModule extends AbstractModule {

    private static final String MULTI_SITE = "com.anrisoftware.sscontrol.httpd.wordpress.MultiSite";
    private static final String OVERRIDE_MODE = "com.anrisoftware.sscontrol.httpd.webservice.OverrideMode";

    @Override
    protected void configure() {
        Multibinder<ClassImporter> b = newSetBinder(binder(),
                ClassImporter.class);
        b.addBinding().toInstance(new ClassImporter() {

            @Override
            public void importClass(ImportCustomizer customizer) {
                customizer.addStaticImport(OVERRIDE_MODE, "update");
                customizer.addStaticImport(MULTI_SITE, "none");
                customizer.addStaticImport(MULTI_SITE, "subdir");
                customizer.addStaticImport(MULTI_SITE, "none");
            }
        });
    }

}
