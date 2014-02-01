package com.anrisoftware.sscontrol.httpd.auth;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Imports authentication types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthImportsModule extends AbstractModule {

    private static final String SatisfyType = "com.anrisoftware.sscontrol.httpd.auth.SatisfyType";
    private static final String AuthProvider = "com.anrisoftware.sscontrol.httpd.auth.AuthProvider";
    private static final String AuthType = "com.anrisoftware.sscontrol.httpd.auth.AuthType";

    @Override
    protected void configure() {
        Multibinder<ClassImporter> b = newSetBinder(binder(),
                ClassImporter.class);
        b.addBinding().toInstance(new ClassImporter() {

            @Override
            public void importClass(ImportCustomizer customizer) {
                customizer.addStaticImport(AuthProvider, "file");
                customizer.addStaticImport(AuthProvider, "ldap");
                customizer.addStaticImport(AuthType, "digest");
                customizer.addStaticImport(AuthType, "basic");
                customizer.addStaticImport(SatisfyType, "all");
                customizer.addStaticImport(SatisfyType, "any");
            }
        });
    }

}
