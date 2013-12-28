package com.anrisoftware.sscontrol.remote.user;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Local user module.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(User.class, User.class)
                .build(UserFactory.class));
        install(new FactoryModuleBuilder().implement(Key.class, Key.class)
                .build(KeyFactory.class));
        install(new FactoryModuleBuilder().implement(Group.class, Group.class)
                .build(GroupFactory.class));
    }

}
