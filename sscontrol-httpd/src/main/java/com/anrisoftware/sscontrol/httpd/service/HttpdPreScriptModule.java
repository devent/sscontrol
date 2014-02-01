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
package com.anrisoftware.sscontrol.httpd.service;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.ClassImporter;
import com.anrisoftware.sscontrol.httpd.auth.AuthImportsModule;
import com.anrisoftware.sscontrol.httpd.wordpress.WordpressImportsModule;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Installs the <i>httpd</i> pre-script modules.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdPreScriptModule extends AbstractModule {

    protected static final String YesNoFlag = "com.anrisoftware.sscontrol.core.yesno.YesNoFlag";

    @Override
    protected void configure() {
        install(new AuthImportsModule());
        install(new WordpressImportsModule());
        bindClassImporter();
    }

    private void bindClassImporter() {
        Multibinder<ClassImporter> b;
        b = newSetBinder(binder(), ClassImporter.class);
        b.addBinding().toInstance(new ClassImporter() {

            @Override
            public void importClass(ImportCustomizer customizer) {
                customizer.addStaticImport(YesNoFlag, "yes");
                customizer.addStaticImport(YesNoFlag, "no");
            }
        });
    }
}
