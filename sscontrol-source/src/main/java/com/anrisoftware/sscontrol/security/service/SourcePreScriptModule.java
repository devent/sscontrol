/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.anrisoftware.sscontrol.core.groovy.groovyloader.ClassImporter;
import com.anrisoftware.sscontrol.core.yesno.YesNoFlag;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Installs the <i>httpd</i> pre-script modules.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SourcePreScriptModule extends AbstractModule {

    private static final String NO = "no";
    private static final String YES = "yes";

    @Override
    protected void configure() {
        bindClassImporter();
    }

    private void bindClassImporter() {
        Multibinder<ClassImporter> b;
        b = newSetBinder(binder(), ClassImporter.class);
        b.addBinding().toInstance(new ClassImporter() {

            @Override
            public void importClass(ImportCustomizer customizer) {
                customizer.addImports(YesNoFlag.class.getName());
                customizer.addStaticImport(YesNoFlag.class.getName(), YES);
                customizer.addStaticImport(YesNoFlag.class.getName(), NO);
            }
        });
    }
}
