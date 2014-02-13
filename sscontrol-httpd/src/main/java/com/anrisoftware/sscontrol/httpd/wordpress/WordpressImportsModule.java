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