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
    }

}
