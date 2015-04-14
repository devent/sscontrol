/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.apt;

import com.anrisoftware.sscontrol.scripts.signrepo.SignRepoModule;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * <i>Apt</i> configuration module.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RepoAptModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new SignRepoModule());
        install(new FactoryModuleBuilder().implement(ParseAptSourcesList.class,
                ParseAptSourcesList.class).build(
                ParseAptSourcesListFactory.class));
        install(new FactoryModuleBuilder().implement(
                DeployAptSourcesList.class, DeployAptSourcesList.class).build(
                DeployAptSourcesListFactory.class));
        install(new FactoryModuleBuilder().implement(DeployAptProxy.class,
                DeployAptProxy.class).build(DeployAptProxyFactory.class));
    }

}
