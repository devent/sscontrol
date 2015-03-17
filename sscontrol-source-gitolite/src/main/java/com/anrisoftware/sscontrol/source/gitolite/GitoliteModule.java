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

import com.anrisoftware.globalpom.resources.ResourcesModule;
import com.anrisoftware.sscontrol.core.bindings.BindingAddressesStatementsTableModule;
import com.anrisoftware.sscontrol.core.groovy.StatementsMapModule;
import com.anrisoftware.sscontrol.core.list.ListModule;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Installs the <i>Gitolite</i> service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class GitoliteModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new StatementsMapModule());
        install(new ListModule());
        install(new ResourcesModule());
        install(new BindingAddressesStatementsTableModule());
        install(new FactoryModuleBuilder().implement(SourceSetupService.class,
                GitoliteServiceImpl.class).build(GitoliteServiceFactory.class));
    }

}
