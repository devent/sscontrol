/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.auth;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Imports HTTP/authentication types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthImportsModule extends AbstractModule {

    private static final String SatisfyType = "com.anrisoftware.sscontrol.httpd.auth.SatisfyType";
    private static final String AuthProvider = "com.anrisoftware.sscontrol.httpd.auth.AuthProvider";
    private static final String AuthType = "com.anrisoftware.sscontrol.httpd.auth.AuthType";
    private static final String RequireUpdate = "com.anrisoftware.sscontrol.httpd.auth.RequireUpdate";
    private static final String RequireValidMode = "com.anrisoftware.sscontrol.httpd.auth.RequireValidMode";

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
                customizer.addStaticImport(RequireUpdate, "nop");
                customizer.addStaticImport(RequireUpdate, "password");
                customizer.addStaticImport(RequireUpdate, "rewrite");
                customizer.addStaticImport(RequireUpdate, "append");
                customizer.addStaticImport(RequireValidMode, "valid_user");
            }
        });
    }

}
